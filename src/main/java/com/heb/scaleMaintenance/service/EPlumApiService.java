package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.invatron.EPlumLogin;
import com.invatron.invatronlib.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds the business logic for ePlum Api.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class EPlumApiService {
	private static final Logger logger = LoggerFactory.getLogger(EPlumApiService.class);

	// transaction types
	private static final String LOGIN_TRANSACTION_TYPE = "LOGIN";
	private static final String LOGOUT_TRANSACTION_TYPE = "LOGOUT";
	private static final String BACK_TRANSACTION_TYPE = "BACK";

	// message types
	private static final String SESSION_MESSAGE_TYPE = "SESN";
	private static final String BATCH_MESSAGE_TYPE = "BTCH";

	// batch commands
	private static final String BATCH_IMPORT_COMMAND = "CCOIMPORT";
	private static final String SESSION_COMMAND = "SSN";
	private static final String BATCH_LOGOUT_COMMAND = "CCOLOGOUT";

	// message keys
	private static final String KEY_MESSAGE_KEY = "KEY";
	private static final String DESCRIPTION_MESSAGE_KEY = "DES";

	// time
	private static final int DEFAULT_TIMEOUT = 1000;
	private static final int FIFTEEN_MINUTE_TIMEOUT = 900;

	// message constants
	private static final String EPLUM_MESSAGE_CHUNK_DELIMITER = ";";
	private static final String EPLUM_MESSAGE_DELIMITER = ",";

	// log messages
	private static final String SEND_EPLUM_MESSAGE = "Sending message to ePlum: %s.";
	private static final String RETRY_EPLUM_ALTERNATE_IP_MESSAGE = "Connection to first message broker ip failed; " +
			"retrying connection with alternate ip.";
	private static final String EPLUM_RESPONSE_MESSAGE = "Response for ePlum batch number: %d, store: %d, was: %s.";
	private static final String EPLUM_UNKNOWN_RESPONSE_LOG_MESSAGE = "Response unknown for ePlum batch number: " +
			"%d, store: %d.";
	private static final String EPLUM_UNKNOWN_RESPONSE_MESSAGE = "Response unknown for this batch. Please contact " +
			"production support for further information.";

	// error messages
	private static final String ACCESS_DENIED_ERROR_MESSAGE = "Access Denied.";
	private static final String MQ_CLIENT_CREATE_ERROR_MESSAGE = "Create MQ_CLIENT FAILED.";
	private static final String RESPONSE_TIMEOUT_ERROR_MESSAGE = "Response timed out for this transaction. Please refer" +
			" to ePlum application for further information.";

	@Value("${ePlum.ip}")
	private String ip;

	@Value("${ePlum.backUpIp}")
	private String backUpIp;

	@Value("${ePlum.port}")
	private int port;

	@Value("${ePlum.username}")
	private String userName;

	@Value("${ePlum.password}")
	private String password;

	/**
	 * Sends messages to ePlum Api.
	 *  @param scaleMaintenanceTransmits List of scale maintenance transmits that contain the messages.
	 * @param userIp User connecting/ logging into ePLum API.
	 */
	public void sendMessagesToEPlum(List<ScaleMaintenanceTransmit> scaleMaintenanceTransmits, String userIp) {

		if(scaleMaintenanceTransmits == null || scaleMaintenanceTransmits.isEmpty()){
			return;
		}
		MESSAGE_DATA loginResponse = null;
		try {
			loginResponse = this.connectAndLoginToEPlumApi(ip, userIp);
			TRANSACTION ePlumTransaction;
			String responseValue;
			MESSAGE_DATA ePlumMessage;
			MESSAGE_DATA response;
			for (ScaleMaintenanceTransmit scaleMaintenanceTransmit : scaleMaintenanceTransmits) {

				EPlumApiService.logger.info(
						String.format(
								EPlumApiService.SEND_EPLUM_MESSAGE,
								scaleMaintenanceTransmit.getePlumMessage().toString()));

				ePlumTransaction = new TRANSACTION(BACK_TRANSACTION_TYPE);
				String currentMessage = BATCH_IMPORT_COMMAND
						.concat(EPLUM_MESSAGE_DELIMITER)
						.concat(SESSION_COMMAND)
						.concat(loginResponse.GetField(0, KEY_MESSAGE_KEY))
						.concat(EPLUM_MESSAGE_DELIMITER)
						.concat(scaleMaintenanceTransmit.getePlumMessage().getHeaderData())
						.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
						.concat(scaleMaintenanceTransmit.getePlumMessage().getPluData())
						.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
						.concat(scaleMaintenanceTransmit.getePlumMessage().getIngredientData())
						.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
						.concat(scaleMaintenanceTransmit.getePlumMessage().getNutrientData());

				ePlumMessage = new MESSAGE_DATA(currentMessage);

				ePlumTransaction.SendRequest(new MESSAGE_TYPE(BATCH_MESSAGE_TYPE), ePlumMessage);
				scaleMaintenanceTransmit.setePlumTransmissionId(String.valueOf(ePlumTransaction.GetId()));
				response = ePlumTransaction.GetResponse(FIFTEEN_MINUTE_TIMEOUT);
				// if response did not time out set response message, and status as completed
				if(response != null){
					responseValue = response.GetField(0, DESCRIPTION_MESSAGE_KEY);
					if(responseValue != null){
						logger.info(String.format(
								EPLUM_RESPONSE_MESSAGE, scaleMaintenanceTransmit.getePlumBatchId(),
								scaleMaintenanceTransmit.getKey().getStore(),
								responseValue));
						scaleMaintenanceTransmit.setResponseMessage(responseValue);
					} else {
						logger.info(String.format(
								EPLUM_UNKNOWN_RESPONSE_LOG_MESSAGE, scaleMaintenanceTransmit.getePlumBatchId(),
								scaleMaintenanceTransmit.getKey().getStore()));
						scaleMaintenanceTransmit.setResponseMessage(EPLUM_UNKNOWN_RESPONSE_MESSAGE);
					}
					scaleMaintenanceTransmit.setStatusCode(Status.Code.COMPLETED.getId());
				}
				// else set response as timed out, and status as timed out
				else {
					logger.error(RESPONSE_TIMEOUT_ERROR_MESSAGE);
					scaleMaintenanceTransmit.setResponseMessage(RESPONSE_TIMEOUT_ERROR_MESSAGE);
					scaleMaintenanceTransmit.setStatusCode(Status.Code.TIMED_OUT.getId());
				}
			}
		} finally {
			// always log out from ePlum API
			if(loginResponse != null){
				// log out
				TRANSACTION ePlumTransaction = new TRANSACTION(LOGOUT_TRANSACTION_TYPE);
				MESSAGE_DATA logoutMessage = new MESSAGE_DATA(BATCH_LOGOUT_COMMAND);
				logoutMessage.SetField(0, KEY_MESSAGE_KEY, loginResponse.GetField(0, KEY_MESSAGE_KEY));
				ePlumTransaction.SendRequest(new MESSAGE_TYPE(SESSION_MESSAGE_TYPE), logoutMessage);
			}
		}
	}

	/**
	 * Create a connection and log in to ePlum API.
	 *
	 * @param clientIp IP of the client to connect/ log in from.
	 * @param userIp User connecting/ logging into ePLum API.
	 * @return Login response from ePlum API.
	 */
	private MESSAGE_DATA connectAndLoginToEPlumApi(String clientIp, String userIp) {
		this.connectToEPlumApi(clientIp, userIp);
		return this.loginToEPlumApi(clientIp, userIp);
	}

	/**
	 * Logs in to the ePlum API with the supplied username and password. If the user is unable to, or if any other
	 * exception occurs, this method throws an Illegal Argument exception.
	 *
	 * @param clientIp IP of the client trying to log in.
	 * @param userIp User trying to log in.
	 * @return Response of logging in to the ePlum API.
	 */
	private MESSAGE_DATA loginToEPlumApi(String clientIp, String userIp) {
		// Login and authenticate
		TRANSACTION loginTransObj = new TRANSACTION(LOGIN_TRANSACTION_TYPE);
		MESSAGE_DATA loginMsgData = new MESSAGE_DATA();
		EPlumLogin loginObj = new EPlumLogin(userName, password, null);
		loginObj.Update(loginMsgData.At(0));
		try {
			loginTransObj.SendRequest(new MESSAGE_TYPE(SESSION_MESSAGE_TYPE), loginMsgData);
			MESSAGE_DATA loginResponse = loginTransObj.GetResponse(DEFAULT_TIMEOUT);
			if (loginResponse.GetField(0, KEY_MESSAGE_KEY, null) == null) {
				if(!backUpIp.equals(clientIp)){
					logger.info(RETRY_EPLUM_ALTERNATE_IP_MESSAGE);
					return this.connectAndLoginToEPlumApi(backUpIp, userIp);
				} else {
					logger.error(ACCESS_DENIED_ERROR_MESSAGE);
					throw new IllegalArgumentException(ACCESS_DENIED_ERROR_MESSAGE);
				}
			} else {
				return loginResponse;
			}

		} catch (INVATRON_EXCEPTION Ex) {
			logger.error(Ex.getMessage());
			throw new IllegalArgumentException(Ex.getMessage());
		}
	}

	/**
	 * Creates a connection to the ePlum API.
	 *
	 * @param clientIp Client ip to connect to.
	 * @param userIp User ip connecting from.
	 */
	private void connectToEPlumApi(String clientIp, String userIp) {
		try {
			new MQ_CLIENT(clientIp, port, userIp);
		} catch (INVATRON_EXCEPTION exception){
			logger.error(MQ_CLIENT_CREATE_ERROR_MESSAGE);
			throw new IllegalArgumentException(MQ_CLIENT_CREATE_ERROR_MESSAGE);
		}
	}
}
