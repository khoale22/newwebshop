/*
 * TopToTopRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TopToTop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about TopToTop.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface TopToTopRepository  extends JpaRepository<TopToTop, Integer> {
	/**
	 * SQL statement that filter TOP TO TOP by name or id.
	 */
	String FIND_TOP_TO_TOP_BY_ID_AND_DESCRIPTION_SQL =
			"from TopToTop topToTop " +
					"where (topToTop.topToTopId like concat('%', :id, '%') " +
					"or upper(topToTop.topToTopName) like concat('%', upper(:name), '%')) ";

	@Query(value = FIND_TOP_TO_TOP_BY_ID_AND_DESCRIPTION_SQL)
	Page<TopToTop> findByIdOrName(@Param("id") String id,
														 @Param("name") String name,
														 Pageable pageRequest);
}
