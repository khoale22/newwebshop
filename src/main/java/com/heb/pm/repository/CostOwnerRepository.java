/*
 *  CostOwnerRepository
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CostOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about query.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface CostOwnerRepository extends CostOwnerCommon {

	/**
	 * find by cost owner id and name contain
	 * @param costOwnerId the cost owner id
	 * @param costOwnerName the cost owner name
	 * @param pageable paging data
	 * @return page data
	 */
	@Query(FIND_COST_OWNER_BY_ID_AND_NAME_SQL)
	Page<CostOwner> findAllByCostOwnerIdAndCostOwnerName(@Param("costOwnerId")String costOwnerId,
														 @Param("costOwnerName") String costOwnerName,
														 Pageable pageable);
}
