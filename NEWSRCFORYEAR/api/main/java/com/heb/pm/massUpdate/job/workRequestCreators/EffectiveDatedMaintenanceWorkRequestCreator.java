package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.EffectiveDatedMaintenance;
import com.heb.pm.entity.EffectiveDatedMaintenanceKey;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.EffectiveDatedMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.time.LocalDateTime;

/**
 * Creates candidates that write to the effective dated maintenance table.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class EffectiveDatedMaintenanceWorkRequestCreator implements WorkRequestCreator {

	@Autowired
	private EffectiveDatedMaintenanceRepository effectiveDatedMaintenanceRepository;

	private long nextSequenceNumber;

	/**
	 * Initializes this class.
	 */
	public void init() {
		// Not ideal, this should be a sequence. Get the max sequence number from the table. this will  be incremented
		// for each record.
		this.nextSequenceNumber = this.effectiveDatedMaintenanceRepository.getMaxKeySequenceNumber() + 1;
	}

	/**
	 * Creates work requests for multiple attributes all that go to EFF_DTD_MAINT. This includes food stamp, fsa,
	 * and the tax flag.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A candidate work request with all data populated to write to the effective dated maintenance table.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
												  int sourceSystem) {

		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);
		candidateWorkRequest.setEffectiveDatedMaintenances(new LinkedList<>());


		// Create the effective dated maintenance
		EffectiveDatedMaintenanceKey key = new EffectiveDatedMaintenanceKey();
		this.populateTableAndField(parameters.getAttribute(), key);
		key.setSequenceNumber(this.nextSequenceNumber++);
		EffectiveDatedMaintenance effectiveDatedMaintenance = new EffectiveDatedMaintenance();
		effectiveDatedMaintenance.setKey(key);
		effectiveDatedMaintenance.setTextValue(parameters.getBooleanValue().equals(Boolean.TRUE) ? "Y" : "N");
		effectiveDatedMaintenance.setCreateUserId(parameters.getUserId());
		effectiveDatedMaintenance.setLastUpdateUserId(parameters.getUserId());
		effectiveDatedMaintenance.setEffectiveDate(parameters.getEffectiveDate());
		effectiveDatedMaintenance.setProductId(productId);
		effectiveDatedMaintenance.setWorkRequest(candidateWorkRequest);
		effectiveDatedMaintenance.setCreateTimeStamp(LocalDateTime.now());
		effectiveDatedMaintenance.setLastUpdateTimeStamp(LocalDateTime.now());
		candidateWorkRequest.getEffectiveDatedMaintenances().add(effectiveDatedMaintenance);

		return candidateWorkRequest;
	}

	/**
	 * Adds the table and column that needs to be updated based on the attribute the user is trying to
	 * update.
	 *
	 * @param attribute The attribute the user is trying to update.
	 * @param key They key to add the column and table to.
	 */
	private void populateTableAndField(MassUpdateParameters.Attribute attribute, EffectiveDatedMaintenanceKey key) {

		if (attribute.equals(MassUpdateParameters.Attribute.FOOD_STAMP)) {
			key.setTableName(EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT);
			key.setColumnName(EffectiveDatedMaintenance.COLUMN_NAME_FOOD_STAMP_SWITCH);
			return;
		}

		if (attribute.equals(MassUpdateParameters.Attribute.FSA)) {
			key.setTableName(EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT);
			key.setColumnName(EffectiveDatedMaintenance.COLUMN_NAME_FSA_CODE);
			return;
		}

		if (attribute.equals(MassUpdateParameters.Attribute.TAX_FLAG)) {
			key.setTableName(EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT);
			key.setColumnName(EffectiveDatedMaintenance.COLUMN_NAME_SALES_TAX_SWITCH);
		}

	}
}
