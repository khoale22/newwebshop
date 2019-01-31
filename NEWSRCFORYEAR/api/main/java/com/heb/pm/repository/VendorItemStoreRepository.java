/*
 * VendorItemStoreRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.VendorItemStore;
import com.heb.pm.entity.VendorItemStoreKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about VendorItemStore.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface VendorItemStoreRepository extends JpaRepository<VendorItemStore, VendorItemStoreKey> {
}
