/*
 * UpcService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.product;

import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Holds business logic related to upc.
 *
 * @author m314029
 * @since 2.0.3
 */
@Service
public class UpcService {

	private static final Logger logger = LoggerFactory.getLogger(UpcService.class);

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	/**
	 * Searches for a selling unit record based on a upc.
	 *
	 * @param upc The upc to search for records on.
	 * @return A selling unit record based on search criteria.
	 */
	public SellingUnit find(Long upc){

		return this.sellingUnitRepository.findOne(upc);
	}

	/**
	 * Set the SellingUnitRepository for this class (used for testing).
	 *
	 * @param repository SellingUnitRepository used by this class.
	 */
	public void setRepository(SellingUnitRepository repository) {
		this.sellingUnitRepository = repository;
	}
}
