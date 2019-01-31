package com.heb.pm.repository;

import com.heb.pm.entity.EffectiveDatedMaintenance;
import com.heb.pm.entity.EffectiveDatedMaintenanceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * JPA repository for EffectiveDatedMaintenance objects.
 *
 * @author d116773
 * @since 2.13.0
 */
public interface EffectiveDatedMaintenanceRepository extends JpaRepository<EffectiveDatedMaintenance,
		EffectiveDatedMaintenanceKey> {

	/**
	 * Returns the maximum sequence number in the key.
	 *
	 * @return The maximum sequence number in the key.
	 */
	@Query("select max(edm.key.sequenceNumber) from EffectiveDatedMaintenance edm")
	long getMaxKeySequenceNumber();

	/**
	 * Finds retail tax effective dated information by searching for all effective dated maintenance by table name,
	 * column names, and product id.
	 *
	 * @param tableName Table name to search for.
	 * @param columnNames List of column names to search for.
	 * @param productId Product id to search for.
	 * @return All effective dated maintenance that match the given parameters.
	 */
	List<EffectiveDatedMaintenance> findByKeyTableNameAndKeyColumnNameInAndProductIdOrderByEffectiveDate(String tableName, List<String> columnNames, Long productId);
}
