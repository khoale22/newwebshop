/*
 * NutrientStatementPanelHeaderRepositoryWithCount
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementPanelHeader;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for NutrientStatementPanelHeader
 *
 * @author vn7033
 * @since 2.20.0
 */
public interface NutrientStatementPanelHeaderRepositoryWithCount  extends JpaRepository<NutrientStatementPanelHeader, Long> {

    /**
     * Find by nutrient statement number in page.
     *
     * @param statementIds the statement ids
     * @param sourceSystemId the source system id
     * @param activeSw the active status
     * @param pageRequest  the page request
     * @return the page
     */
    Page<NutrientStatementPanelHeader> findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitchNot(List<String> statementIds, Long sourceSystemId, char activeSw, Pageable pageRequest);

    /**
     * Find by statement maintenance switch not page.
     *
     * @param statementMaintenanceSwitch the statement maintenance switch
     * @param statementIdsRequest        the statement ids request
     * @param sourceSystemId the source system id
     * @return the page
     */
    Page<NutrientStatementPanelHeader> findBySourceSystemIdAndStatementMaintenanceSwitchNot(Long sourceSystemId, char statementMaintenanceSwitch, Pageable statementIdsRequest);
}

