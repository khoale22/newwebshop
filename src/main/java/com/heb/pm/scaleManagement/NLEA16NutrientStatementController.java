/*
 *
 * NLEA16NutrientStatementController.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.NutrientStatementPanelHeader;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.lang.*;

/**
 * REST endpoint for accessing NLEA Nutrient Statement information.
 *
 * @author vn70633
 * @since 2.20.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NLEA16NutrientStatementController.NUTRIENT_STATEMENT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT)
public class NLEA16NutrientStatementController {

    private static final Logger logger = LoggerFactory.getLogger(NLEA16NutrientStatementController.class);

    protected static final String NUTRIENT_STATEMENT_URL = "/NLEA16NutrientStatement";

    private static final String FIND_BY_SOURCE_SYSTEM_REFERENCE_MESSAGE =
            "User %s from IP %s requested the nutrient statements with the following " +
                    "source system reference [%s]";
    private static final String DEFAULT_NO_NUTRIENT_STATEMENT_MESSAGE =
            "Must search for at least one NLEA nutrient statement.";
    private static final String NUTRIENT_STATEMENT_PANEL_MESSAGE_KEY =
            "NLEANutrientStatementController.missingNutrientStatementPanel";
    private static final String FIND_BY_NUTRIENT_STATEMENT_MESSAGE =
            "User %s from IP %s requested the nutrient statements with the following " +
                    "nutrient statements codes [%s]";

    @Autowired
    private NLEA16NutrientStatementService nutrientStatementService;

    // Defaults
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private NonEmptyParameterValidator parameterValidator;

    @Autowired
    private UserInfo userInfo;

    private LazyObjectResolver<NutrientStatementPanelHeader> resolver = new NutrientStatementPanelHeaderResolver();


    /**
     * Find all by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @return the  List<NutrientStatementPanelHeader>
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.PUT, value = "getNutrientPanelBySourceSystemReferenceId/{sourceSystemReferenceId}")
    public List<NutrientStatementPanelHeader> getNutrientPanelBySourceSystemReferenceId(
            @PathVariable("sourceSystemReferenceId") String sourceSystemReferenceId, HttpServletRequest request) {
        this.logFindBySourceSystemReferenceId(request.getRemoteAddr(), sourceSystemReferenceId);
        return this.nutrientStatementService.getNutrientPanelBySourceSystemReferenceId(sourceSystemReferenceId);
    }

    /**
     * Logs a users request for find nutrient panel by source system reference id.
     *
     * @param ipAddress               The IP address of the logged in user.
     * @param sourceSystemReferenceId the source system reference id.
     */
    private void logFindBySourceSystemReferenceId(String ipAddress, String sourceSystemReferenceId) {
        NLEA16NutrientStatementController.logger.info(String.format(
                NLEA16NutrientStatementController.FIND_BY_SOURCE_SYSTEM_REFERENCE_MESSAGE, this.userInfo.getUserId(),
                ipAddress, sourceSystemReferenceId));
    }

    /**
     * Find all by nutrient statement panels pageable result.
     *
     * @param statementIds  the statement id
     * @param page          the page
     * @param includeCounts the include counts
     * @param request       the request
     * @return the pageable result
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "")
    public PageableResult<NutrientStatementPanelHeader> findAllByNutrientStatementIds(
            @RequestParam("nutrientStatementId") List<String> statementIds,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts, HttpServletRequest request) {

        this.parameterValidator.validate(statementIds, NLEA16NutrientStatementController.DEFAULT_NO_NUTRIENT_STATEMENT_MESSAGE,
                NLEA16NutrientStatementController.NUTRIENT_STATEMENT_PANEL_MESSAGE_KEY, request.getLocale());
        this.logFindByNutrientStatements(request.getRemoteAddr(), statementIds);

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? NLEA16NutrientStatementController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? NLEA16NutrientStatementController.DEFAULT_PAGE_SIZE : pageSize;

        PageableResult<NutrientStatementPanelHeader> rs = this.nutrientStatementService.findNutrientStatementByStatementId(statementIds, ic, ps, pg);
        return this.resolveResults(rs);
    }

    /**
     * Find all statement ids pageable result.
     *
     * @param page          the page
     * @param includeCounts the include counts
     * @param request       the request
     * @return the pageable result
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/all")

    public PageableResult<NutrientStatementPanelHeader> findAllStatementIds(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            HttpServletRequest request) {
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? NLEA16NutrientStatementController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? NLEA16NutrientStatementController.DEFAULT_PAGE_SIZE : pageSize;
        return this.resolveResults(this.nutrientStatementService.findAllNutrientStatements(ic, pg, ps));
    }

    /*
     * Logs a user's request to get nutrient statements by nutrient statement code.
     * @param ip The IP address th user is logged in from.
     * @param nutrientStatements The nutrients statements to search on.
     */
    private void logFindByNutrientStatements(String ip, List<String> nutrientStatements) {
        NLEA16NutrientStatementController.logger.info(String.format(NLEA16NutrientStatementController.
                FIND_BY_NUTRIENT_STATEMENT_MESSAGE, this.userInfo.getUserId(), ip, nutrientStatements));
    }

    /**
     * Find by nutrient statements export.
     *
     * @param nutrientStatements the nutrient statements
     * @param totalRecordCount   the total record count
     * @param downloadId         the download id
     * @param request            the request
     * @param response           the response
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportNutrientStatementPanelByIdsToCsv",
            headers = "Accept=text/csv")
    public void findByNutrientStatementsExport(
            @RequestParam(name = "nutrientStatements", required = false) List<String> nutrientStatements,
            @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
            @RequestParam(value = "downloadId", required = false) String downloadId,
            HttpServletRequest request, HttpServletResponse response) {
        double pageCount = Math.ceil((float) totalRecordCount / DEFAULT_PAGE_SIZE);
        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        try {
            for (int x = 0; x < pageCount; x++) {

                if (x == 0) {
                    response.getOutputStream().println(CreateNutrientStatementPanelHeaderCsv.getHeading());
                }
                response.getOutputStream().print(CreateNutrientStatementPanelHeaderCsv.createCsv(
                        this.findAllByNutrientStatementIds(nutrientStatements, x,
                                NLEA16NutrientStatementController.DEFAULT_PAGE_SIZE, false, request)));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Find all nutrient statements export.
     *
     * @param totalRecordCount the total record count
     * @param downloadId       the download id
     * @param request          the request
     * @param response         the response
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportAllNutrientStatementPanelToCsv",
            headers = "Accept=text/csv")
    public void findAllNutrientStatementsExport(
            @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
            @RequestParam(value = "downloadId", required = false) String downloadId,
            HttpServletRequest request, HttpServletResponse response) {
        double pageCount = Math.ceil((float) totalRecordCount / DEFAULT_PAGE_SIZE);
        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        try {
            for (int x = 0; x < pageCount; x++) {

                if (x == 0) {
                    response.getOutputStream().println(CreateNutrientStatementPanelHeaderCsv.getHeading());
                }
                response.getOutputStream().print(CreateNutrientStatementPanelHeaderCsv.createCsv(
                        this.nutrientStatementService.findAllNutrientStatements(false, x,
                                NLEA16NutrientStatementController.DEFAULT_PAGE_SIZE)));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    j

    /**
     * Fetch details for the resolver
     *
     * @param results a pageable result of nutrient statement panel header.
     * @return a nutrient statement panel header.
     */
    private PageableResult<NutrientStatementPanelHeader> resolveResults(PageableResult<NutrientStatementPanelHeader> results) {
        results.getData().forEach(this.resolver::fetch);
        return results;
    }
}

