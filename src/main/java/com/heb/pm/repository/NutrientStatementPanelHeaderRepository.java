/*
 *
 * NutrientStatementPanelHeaderRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementPanelHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for NutrientStatementPanelHeader.
 *
 * @author vn7033
 * @since 2.20.0
 */
public interface NutrientStatementPanelHeaderRepository extends JpaRepository<NutrientStatementPanelHeader, Long> {

    /**
     * Find all by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @return the  List<NutrientStatementPanelHeader>
     */
    List<NutrientStatementPanelHeader> findAllBySourceSystemReferenceIdAndSourceSystemId(String sourceSystemReferenceId, Long sourceSystemId);

    /**
     * Find by nutrient statement number in list.
     *
     * @param statementIds the statement ids
     * @param sourceSystemId the source system id
     * @param activeSw the active status
     * @param pageRequest  the page request
     * @return the list
     */
    List<NutrientStatementPanelHeader> findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitchNot(List<String> statementIds, Long sourceSystemId, char activeSw, Pageable pageRequest);
    /**
     * Find all by page list.
     *
     * @param statementIdsRequest the statement ids request
     * @param sourceSystemId the source system id
     * @param statementMaintenanceSwitch the statement maintenance switch
     * @return the list
     */
    List<NutrientStatementPanelHeader> findBySourceSystemIdAndStatementMaintenanceSwitchNot(Long sourceSystemId, char statementMaintenanceSwitch,  Pageable statementIdsRequest);

    /**
     *
     * @param statementIds
     * @param statementMaintenanceSwitch
     * @param sourceSystemId
     * @return
     */
    List<NutrientStatementPanelHeader> findBySourceSystemReferenceIdAndStatementMaintenanceSwitchNotAndSourceSystemId (String statementIds,char statementMaintenanceSwitch,Long sourceSystemId);
}
