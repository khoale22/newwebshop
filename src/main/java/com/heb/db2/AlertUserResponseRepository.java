/*
 * AlertUserResponseRepository.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.db2;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for AlertUserResponse entity.
 *
 * @author vn40486
 * @since 2.16.0
 */
public interface AlertUserResponseRepository extends JpaRepository<AlertUserResponse, AlertUserResponseKey>{}