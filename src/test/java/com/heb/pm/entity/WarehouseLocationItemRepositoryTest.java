/*
 *  WarehouseLocationItemRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to support testing the WarehouseLocationItem class.
 *
 * @author d116773
 * @since 2.0.1
 */
public interface WarehouseLocationItemRepositoryTest
		extends JpaRepository<WarehouseLocationItem, WarehouseLocationItemKey>{
}
