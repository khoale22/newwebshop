/*
 * TransactionTrackingRepositoryCommon
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TransactionTracker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface TransactionTrackingRepositoryCommon {
	/**
	 * find batch status query
	 */
	final String FIND_TRACKING = "SELECT distinct trk " +
			"FROM TransactionTracker trk JOIN trk.candidateWorkRequest b " +
			"WHERE b.sourceSystem IN(:listSource) " +
			"AND trk.fileNm NOT IN (:listFileName) " +
			"AND trk.trackingId LIKE CONCAT('%', :requestId, '%')";

}
