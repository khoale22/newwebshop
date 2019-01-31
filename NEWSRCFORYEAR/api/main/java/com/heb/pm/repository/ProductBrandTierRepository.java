/*
 * ProductBrandTierRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductBrandTier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of the ProductBrandTier entity.
 *
 * @author vn00602
 * @since 2.12.0
 */
public interface ProductBrandTierRepository extends JpaRepository<ProductBrandTier, Integer> {

}
