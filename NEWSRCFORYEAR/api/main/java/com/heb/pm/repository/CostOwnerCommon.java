/*
 *  CostOwnerCommon
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CostOwner;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about CostOwnerCommon.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface CostOwnerCommon extends JpaRepository<CostOwner, Integer> {
	/**
	 * SQL statement that filter cost owner by its description and id.
	 */
	String FIND_COST_OWNER_BY_ID_AND_NAME_SQL = "from CostOwner costOwner " +
			"where costOwner.costOwnerId like concat('%', :costOwnerId, '%') " +
			"or upper(costOwner.costOwnerName) like concat('%', upper(:costOwnerName), '%')";

}
