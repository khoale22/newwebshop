/*
 *
 * NLEA16NutrientStatementService.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientStatementPanelHeader;
import com.heb.pm.repository.NutrientStatementPanelHeaderRepository;
import com.heb.pm.repository.NutrientStatementPanelHeaderRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related for NLEA nutrient statements.
 *
 * @author vn70633
 * @since 2.20.0
 */
@Service
public class NLEA16NutrientStatementService {

    private static final long SRC_SYSTEM_ID_SCALE = 17;

    @Autowired
    private NutrientStatementPanelHeaderRepository nutrientStatementPanelHeaderRepository;

    @Autowired
    private NutrientStatementPanelHeaderRepositoryWithCount nutrientStatementHeaderRepositoryWithCount;


    /**
     * Find all by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @return the  List<NutrientStatementPanelHeader>
     */
    public List<NutrientStatementPanelHeader> getNutrientPanelBySourceSystemReferenceId(String sourceSystemReferenceId) {
        return this.nutrientStatementPanelHeaderRepository.findAllBySourceSystemReferenceIdAndSourceSystemId(sourceSystemReferenceId, SRC_SYSTEM_ID_SCALE);
    }

    /**
     * Find nutrient statement by statement id pageable result.
     *
     * @param statementIds the statement id
     * @param ic           the ic
     * @param ps           the ps
     * @param pg           the pg
     * @return the pageable result
     */
    public PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementId(List<String> statementIds, boolean ic, int ps, int pg) {
        List<String> statIds = new ArrayList<String>();
        for (int i = 0; i < statementIds.size(); i++) {
            statIds.add(String.valueOf(statementIds.get(i)));
        }
        Pageable nutrientSatementsRequest = new PageRequest(pg, ps, NutrientStatementPanelHeader.getDefaultSort());
        return ic ? this.findNutrientStatementByStatementIdWithCount(
                statIds, nutrientSatementsRequest) :
                this.findNutrientStatementByStatementIdWithoutCount(statIds, nutrientSatementsRequest);

    }

    /**
     * Find nutrient statement information by id.
     *
     * @param statementIds             id that will search by.
     * @param nutrientStatementRequest the request
     * @return the pageable result
     */
    private PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementIdWithCount(
            List<String> statementIds, Pageable nutrientStatementRequest) {

        Page<NutrientStatementPanelHeader> data = this.nutrientStatementHeaderRepositoryWithCount.
                findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitchNot(statementIds, SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW, nutrientStatementRequest);

        return new PageableResult<>(nutrientStatementRequest.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Find nutrient statement information by id.
     *
     * @param statementIds             id to search by
     * @param nutrientStatementRequest the request
     * @return the pageable result
     */
    private PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementIdWithoutCount(
            List<String> statementIds, Pageable nutrientStatementRequest) {
        List<NutrientStatementPanelHeader> data = this.nutrientStatementPanelHeaderRepository.
                findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitchNot(statementIds, SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW, nutrientStatementRequest);
        return new PageableResult<>(nutrientStatementRequest.getPageNumber(), data);
    }

    /**
     * Find all statement id pageable result.
     *
     * @param includeCount the include count
     * @param page         the page
     * @return the pageable result
     */
    public PageableResult<NutrientStatementPanelHeader> findAllNutrientStatements(boolean includeCount, int page, int pageSize) {
        Pageable statementIdsRequest = new PageRequest(page, pageSize,
                NutrientStatementPanelHeader.getDefaultSort());

        return includeCount ? this.findAllStatementIdWithCount(statementIdsRequest) :
                this.findAllStatementIdWithoutCount(statementIdsRequest);
    }

    /**
     * Find all nutrient statement information based on statement id with count.
     *
     * @param statementIdsRequest
     * @return the pagable result
     */
    private PageableResult<NutrientStatementPanelHeader> findAllStatementIdWithCount(Pageable statementIdsRequest) {

        Page<NutrientStatementPanelHeader> data = this.nutrientStatementHeaderRepositoryWithCount.findBySourceSystemIdAndStatementMaintenanceSwitchNot(SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW, statementIdsRequest);

        return new PageableResult<>(statementIdsRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
                data.getContent());
    }

    /**
     * Find all nutrient Statement details via statement id without count.
     *
     * @param statementIdsRequest
     * @return the pageable result
     */
    private PageableResult<NutrientStatementPanelHeader> findAllStatementIdWithoutCount(Pageable statementIdsRequest) {
        List<NutrientStatementPanelHeader> data =
                this.nutrientStatementPanelHeaderRepository.findBySourceSystemIdAndStatementMaintenanceSwitchNot(SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW, statementIdsRequest);

        return new PageableResult<>(statementIdsRequest.getPageNumber(), data);
    }

}
