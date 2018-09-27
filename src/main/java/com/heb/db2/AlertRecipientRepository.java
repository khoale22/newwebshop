/*
 * AlertRecipientRepository
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
 * JPA Repository for AlertRecipientRepository.
 *
 * @author vn70529
 * @since 2.17.0
 */
public interface AlertRecipientRepository extends JpaRepository<AlertRecipient, AlertRecipientKey> {
}
