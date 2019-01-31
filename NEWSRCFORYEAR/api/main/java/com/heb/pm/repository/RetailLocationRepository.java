/*
 * RetailLocationRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.RetailLocation;
import com.heb.pm.entity.RetailLocationKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about RetailLocation.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface RetailLocationRepository extends JpaRepository<RetailLocation, RetailLocationKey> {

	/**
	 * Returns the list retail locations.
	 * @param linOfBusId the lin of bus id.
	 * @return the list retail locations.
	 */
	List<RetailLocation> findByLinOfBusId(Integer linOfBusId);

	/**
	 * Returns the list of retail locations by lin of bus id and location number.
	 * @param linOfBusId the lin of bus id.
	 * @param locationNumber the location number.
	 * @return the list of retail locations.
	 */
	List<RetailLocation> findByLinOfBusIdAndKeyLocationNumber(Integer linOfBusId, Integer locationNumber);
}
