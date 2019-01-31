package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.*;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import com.heb.scaleMaintenance.service.ScaleMaintenanceAuthorizeRetailService;
import com.heb.scaleMaintenance.service.ScaleMaintenanceUpcService;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Processor for converting batch numbers into ePlum Messages.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceTransmitProcessor implements ItemProcessor<ScaleMaintenanceTracking, WrappedScaleMaintenanceTracking> {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceTransmitProcessor.class);

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	@Autowired
	private ScaleMaintenanceUpcService scaleMaintenanceUpcService;

	@Autowired
	private EPlumApiUtils ePlumApiUtils;

	@Autowired
	private ScaleMaintenanceTransmitRepository scaleMaintenanceTransmitRepository;

	@Override
	public WrappedScaleMaintenanceTracking process(ScaleMaintenanceTracking item) throws Exception {
		if(item == null){
			return null;
		}
		return this.getEPlumMessagesForRelatedTransmits(item);
	}

	/**
	 * Gets ePlum messages for a scale maintenance tracking and related transmits.
	 *
	 * @param tracking Tracking read into this processor.
	 * @return Wrapped tracking containing the given scale maintenance tracking and related transmits.
	 */
	private WrappedScaleMaintenanceTracking getEPlumMessagesForRelatedTransmits(ScaleMaintenanceTracking tracking) {

		List<ScaleMaintenanceTransmit> transmits = this.findRelatedTransmits(tracking);
		Long ePlumBatchNumber = tracking.getTransactionId() % 10000;
		List<ScaleMaintenanceUpc> currentUpcs =
				this.scaleMaintenanceUpcService.getByTransactionId(tracking.getTransactionId());
		List<ScaleMaintenanceAuthorizeRetail> currentAuthorizeRetails;
		boolean hasEPlumMessage = false;
		for(ScaleMaintenanceTransmit transmit : transmits){
			currentAuthorizeRetails = this.scaleMaintenanceAuthorizeRetailService
					.getAuthorizedByTransactionIdAndStore(tracking.getTransactionId(), transmit.getKey().getStore());
			if(CollectionUtils.isNotEmpty(currentAuthorizeRetails)){
				if(!hasEPlumMessage){
					hasEPlumMessage = this.setEPlumMessageOnTransmit(ePlumBatchNumber, transmit, currentUpcs, currentAuthorizeRetails);
				} else {
					this.setEPlumMessageOnTransmit(ePlumBatchNumber, transmit, currentUpcs, currentAuthorizeRetails);
				}
			} else {
				transmit.setStatusCode(Status.Code.COMPLETED.getId());
			}
			transmit.setLastUpdatedTime(LocalDateTime.now());
		}
		tracking.setStatusCode(Status.Code.COMPLETED.getId())
				.setLastUpdatedTime(LocalDateTime.now());
		return new WrappedScaleMaintenanceTracking(tracking, transmits);
	}

	/**
	 * Sets ePlum message on a given scale maintenance transmit.
	 *
	 * @param ePlumBatchNumber ePlum batch number.
	 * @param transmit Scale maintenance transmit with information to send to ePlum.
	 * @param currentUpcs List of current upcs.
	 * @param currentAuthorizeRetails List of current authorize and retails.
	 * @return Whether or not there is a message to send to ePlum.
	 */
	private boolean setEPlumMessageOnTransmit(Long ePlumBatchNumber, ScaleMaintenanceTransmit transmit,
										   List<ScaleMaintenanceUpc> currentUpcs,
										   List<ScaleMaintenanceAuthorizeRetail> currentAuthorizeRetails) {
		try{
			transmit.setePlumMessage(
					this.ePlumApiUtils.generateEPlumMessage(
							ePlumBatchNumber, transmit.getKey().getStore(), currentUpcs, currentAuthorizeRetails));
			return true;
		} catch (IllegalArgumentException e){
			logger.info(e.getLocalizedMessage());
			transmit.setResult(e.getLocalizedMessage())
					.setStatusCode(Status.Code.COMPLETED.getId());
			return false;
		}
	}

	/**
	 * Gets all scale maintenance transmits that share the same tracking id as the given scale maintenance tracking.
	 *
	 * @param tracking Scale maintenance tracking to look for related transmits by.
	 * @return List of scale maintenance transmits that share the transaction id.
	 */
	private List<ScaleMaintenanceTransmit> findRelatedTransmits(ScaleMaintenanceTracking tracking) {
		return this.scaleMaintenanceTransmitRepository.findByKeyTransactionId(tracking.getTransactionId());
	}
}
