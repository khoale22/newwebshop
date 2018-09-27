package com.heb.scaleMaintenance.endpoint;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.pm.ResourceConstants;
import com.heb.scaleMaintenance.ScaleMaintenanceConstants;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.service.ScaleMaintenanceAuthorizeRetailService;
import com.heb.scaleMaintenance.service.ScaleMaintenanceTrackingService;
import com.heb.scaleMaintenance.service.ScaleMaintenanceTransmitService;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest endpoint for scale maintenance transactions.
 *
 * @author m314029
 * @since 2.17.8
 */
@RestController()
@RequestMapping(ScaleMaintenanceConstants.BASE_SCALE_MAINTENANCE_URL + CheckStatusEndPoint.BASE_CHECK_STATUS_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_CHECK_STATUS)
public class CheckStatusEndPoint {

	private static final Logger logger = LoggerFactory.getLogger(CheckStatusEndPoint.class);

	// urls
	static final String BASE_CHECK_STATUS_URL = "/checkStatus";
	private static final String TRANSACTION_ID_PARAMETER = "/{transactionId}";
	private static final String FIND_ALL_TRANSACTIONS_URL = "/findAllTransactions";
	private static final String FIND_ALL_TRANSMITS_URL = "/findAllTransmits";
	private static final String FIND_ALL_RETAILS_URL = "/findAllRetails";

	// log messages
	private static final String FIND_TRACKING_LOG =
			"User %s from IP %s requested to get scale maintenance tracking " +
					"with paging info (page: %d , page size: %d).";
	private static final String FIND_TRACKING_BY_ID_LOG =
			"User %s from IP %s requested to get scale maintenance tracking with tracking id : %d.";

	// error messages
	private static final String TRANSACTION_ID_NOT_FOUND_ERROR = "Transaction id required to search for.";
	private static final String TRANSACTION_ID_NOT_FOUND_ERROR_KEY ="CheckStatusEndPoint.missingTransactionId";
	private static final String STORE_NUMBER_NOT_FOUND_ERROR = "Store Number required to search for.";
	private static final String STORE_NUMBER_NOT_FOUND_ERROR_KEY ="CheckStatusEndPoint.missingStore";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceTrackingService scaleMaintenanceTrackingService;

	@Autowired
	private ScaleMaintenanceTransmitService scaleMaintenanceTransmitService;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	/**
	 * Gets the scale maintenance tracking matching the given transaction id.
	 *
	 * @param transactionId Transaction id to look for.
	 * @param request The HTTP request that initiated this call.
	 * @return Scale maintenance tracking matching the transaction id.
	 */
	@RequestMapping(method = RequestMethod.GET,  value = TRANSACTION_ID_PARAMETER)
	public ScaleMaintenanceTracking findTransactionById(
			@PathVariable Long transactionId, HttpServletRequest request) {

		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_BY_ID_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), transactionId));
		return this.scaleMaintenanceTrackingService.findByTransactionId(transactionId);
	}

	/**
	 * Finds the requested page of all scale maintenance tracking.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance tracking based on given parameters.
	 */
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_TRANSACTIONS_URL)
	public PageableResult<ScaleMaintenanceTracking> findTransactions(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			HttpServletRequest request) {

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceTrackingService.findAllByCreatedTime(page, pageSize, includeCount);
	}

	/**
	 * Finds the requested page of all scale maintenance transmits.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance tracking based on given parameters.
	 */
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_TRANSMITS_URL)
	public PageableResult<ScaleMaintenanceTransmit> findTransmits(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			@RequestParam(value = "transactionId") Long transactionId,
			HttpServletRequest request) {
		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceTransmitService.
				findAllByTransactionIdOrderedByStore(page, pageSize, includeCount, transactionId);
	}

	/**
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance retail based on given parameters.
	 */
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_RETAILS_URL)
	public PageableResult<ScaleMaintenanceAuthorizeRetail> findRetail(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			@RequestParam(value = "transactionId") Long transactionId,
			@RequestParam(value = "store") Integer store,
			HttpServletRequest request) {
		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(
				store, STORE_NUMBER_NOT_FOUND_ERROR, STORE_NUMBER_NOT_FOUND_ERROR_KEY, request.getLocale());


		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceAuthorizeRetailService.findAllByTransactionIdAndStore(
				page, pageSize, includeCount, transactionId, store);
	}
}
