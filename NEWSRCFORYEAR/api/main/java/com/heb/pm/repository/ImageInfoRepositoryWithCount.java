/*
 *  ImageInfoRepositoryWithCount
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.ProductScanImageURIKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for the Product Scan Image URI table with count.
 *
 * @author vn70529
 * @since 2.27.0
 */
public interface ImageInfoRepositoryWithCount extends JpaRepository<ProductScanImageURI, ProductScanImageURIKey>,  ImageInfoRepositoryCommon {

    /**
     * Get submitted image by upcs with rejected image or not.
     * If includeRejectedImage is true, then the query return the submitted image and rejected image,
     * or the query is just return the submitted image.
     *
     * @param upcs                 the list of upc.
     * @param includeRejectedImage the include rejected image or not.
     * @param page                 the Pageable.
     * @return the List<ProductScanImageURI>
     */
    @Query(value = QUERY_FIND_SUBMITTED_IMAGE_BY_UPCS)
    Page<ProductScanImageURI> findSubmittedImageByUpcsAndIncludeRejectedImageOrNot(@Param("upcs") List<Long> upcs, @Param("includeRejectedImage") Boolean includeRejectedImage, Pageable page);
}
