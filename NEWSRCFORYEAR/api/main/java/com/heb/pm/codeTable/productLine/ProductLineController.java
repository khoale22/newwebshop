/*
 *  ProductLineController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productLine;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductLine;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest endpoint for product line.
 *
 * @author m314029
 * @since 2.26.0
 */
@RestController()
@RequestMapping(ProductLineController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_LINE)
public class ProductLineController {

    private static final Logger logger = LoggerFactory.getLogger(ProductLineController.class);

    protected static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/codeTable/productLine";

    private static final String URL_GET_PRODUCT_LINE_PAGE = "/findPage";

    private static final String FIND_PRODUCT_LINE_MESSAGE = "User %s from IP %s requested to find product " +
			"lines by page: %d, page size: %d, id: '%s', description: '%s', and include count: %s.";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";

    @Autowired
    private ProductLineService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Get all product line records.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     * @param request The http servlet request.
     * @return The page of product lines.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineController.URL_GET_PRODUCT_LINE_PAGE)
    public PageableResult<ProductLine> findByPage(@RequestParam(value = "page", required = false) Integer page,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                               @RequestParam(value = "id", required = false, defaultValue = "") String id,
                                                               @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                                               @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                               HttpServletRequest request) {

        int pageNo = page == null ? ProductLineController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductLineController.DEFAULT_PAGE_SIZE : pageSize;
        id = StringUtils.isEmpty(id) ? ProductLineController.DEFAULT_NO_FILTER : id;
        description = StringUtils.isEmpty(description) ? ProductLineController.DEFAULT_NO_FILTER : description;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        this.logGetProductLinesPage(request.getRemoteAddr(), pageNo, size, id, description, count);

        return this.service.findByPage(pageNo, size, id, description, count);
    }

    /**
     * Logs a user's request to get all product lines.
     *
     * @param ipAddress The user's ip.
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     */
    private void logGetProductLinesPage(String ipAddress, int page, int pageSize, String id, String description, boolean includeCount) {
        ProductLineController.logger.info(String.format(ProductLineController.FIND_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, id, description, includeCount));
    }
}
