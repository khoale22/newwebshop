package com.heb.pm.repository;

import com.heb.pm.entity.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by thanhtran on 8/29/2017.
 */
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

	/**
	 * Find Attribute by code.
	 *
	 * @param attrNm the attribute name
	 * @return the attribute.
	 */
	@Query("select a from Attribute a where attributeName like ?1")
	List<Attribute> findByAttributeNameEquals(String attrNm);

	/**
	 * Finds a list of attributes that have a given external ID in an external system.
	 *
	 * @param externalId The ID of the attribute in the other system.
	 * @param sourceSystemId The ID of that system.
	 * @return The attributes that have that ID.
	 */
	List<Attribute> findByExternalIdAndSourceSystemId(String externalId, long sourceSystemId);

	/**
	 * Returns all dynamic attributes.
	 *
	 * @return the attributes.
	 */
	@Query("select a from Attribute a where dyn_attr_sw = 'Y' order by attributeName")
	Page<Attribute> findDynamicAttributes(Pageable pageable);

	/**
	 * Returns all dynamic attributes with name.
	 *
	 * @return the attributes.
	 */
	@Query("select a from Attribute a where dyn_attr_sw = 'Y' and lower(attributeName) like lower(?1) order by attributeName")
	Page<Attribute> findDynamicAttributes(Pageable pageable, String attributeName);

	/**
	 * Find Attribute by id.
	 *
	 * @param attrId the attribute id
	 * @return the attribute.
	 */
	List<Attribute> findByAttributeId(String attrId);
	List<Attribute> findByAttributeIdIn(List<Long> attributeIds);

}
