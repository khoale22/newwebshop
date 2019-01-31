/*
 *  ProductLineService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productLine;

import com.heb.pm.entity.ProductLine;
import com.heb.pm.repository.ProductLineRepository;
import com.heb.pm.repository.ProductLineRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Holds all business logic related to code table product line.
 *
 * @author m314029
 * @since 2.26.0
 */
@Service
public class ProductLineService {

    @Autowired
    private ProductLineRepository repository;

    @Autowired
    private ProductLineRepositoryWithCount repositoryWithCount;

    /**
     * Get all records of PROD_SUB_BRND table by heb pagination.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @return the page of product brands.
     */
    public PageableResult<ProductLine> findByPage(int page, int pageSize, String id,
                                                                String description, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize, ProductLine.getDefaultSort());
        PageableResult<ProductLine> results;
        if (includeCount) {
            Page<ProductLine> prodSubBrands = this.findProductSubBrandsWithCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), prodSubBrands.getTotalPages(),
                    prodSubBrands.getTotalElements(), prodSubBrands.getContent());
        } else {
            List<ProductLine> productSubBrands = this.findProductSubBrandsWithoutCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productSubBrands);
        }
        return results;
    }

    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private Page<ProductLine> findProductSubBrandsWithCount(String id, String description, Pageable pageRequest) {
        Page<ProductLine> productBrands;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines.
            productBrands = this.repositoryWithCount.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines filter by product line id.
            productBrands = this.repositoryWithCount.findByIdIgnoreCaseContaining(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all product lines filter by product line name.
            productBrands = this.repositoryWithCount.findByDescriptionIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all product lines filter by product line id and product line name.
            productBrands = this.repositoryWithCount.findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(
                    id, description, pageRequest);
        }
        return productBrands;
    }

    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductLine> findProductSubBrandsWithoutCount(String id, String description, Pageable pageRequest) {
        List<ProductLine> productBrands;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines.
            productBrands = this.repository.findAllByPage(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines filter by product line id.
            productBrands = this.repository.findByIdIgnoreCaseContaining(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all product lines filter by product line name.
            productBrands = this.repository.findByDescriptionIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all product lines filter by product line id and product line name.
            productBrands = this.repository.findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(
                    id, description, pageRequest);
        }
        return productBrands;
    }
}
