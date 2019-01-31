package com.heb.pm.repository;

import com.heb.pm.entity.EntityAttributeCode;
import com.heb.pm.entity.EntityAttributeCodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * A repository to query the ENTY_ATTR_CD table in the database
 * @author s753601
 * @version 2.14.0
 */
public interface EntityAttributeCodeRepository extends JpaRepository<EntityAttributeCode, EntityAttributeCodeKey> {

	/**
	 * This method will get the list of all attribute codes tied to an attribute for a tags and specs attribute
	 * @param attributeId the parent attribute id
	 * @param entityId the qualifier for tags and specs
	 * @return list
	 */
	List<EntityAttributeCode> findByKeyAttributeIdAndKeyEntityId(Long attributeId, Long entityId);

	/**
	 * This method will get the list of all entity attribute codes by attribute id
	 * @param attributeId the entity id
	 * @return list
	 */
	List<EntityAttributeCode> findByKeyAttributeId(Long attributeId);

	/**
	 * Finds all entity attribute codes based on the entity id
 	 * @param entityId
	 * @return list
	 */
	List<EntityAttributeCode> findByKeyEntityId(Long entityId);

	/**
	 * Get count of records for attribute
	 * @param entityId
	 * @param attributeId
	 * @return count
	 */
	@Query("select count(e) from EntityAttributeCode e where e.key.entityId = :entityId and e.key.attributeId = :attributeId")
	int findCountForEntityandAttribute(@Param("entityId")Long entityId, @Param("attributeId")long attributeId);
}
