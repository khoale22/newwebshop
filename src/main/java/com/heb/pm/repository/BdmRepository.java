/*
 * BdmRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Bdm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Bdm.
 *
 * @author m314029
 * @since 2.0.6
 */
public interface BdmRepository extends JpaRepository<Bdm, String> {
}
