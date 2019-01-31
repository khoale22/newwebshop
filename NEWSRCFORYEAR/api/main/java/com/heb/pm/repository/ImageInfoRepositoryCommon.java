/*
 *  ImageInfoRepositoryCommon
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Repository constants for queries to retrieve image information.
 *
 * @author vn70529
 * @since 2.27.0
 */
public interface ImageInfoRepositoryCommon {

    /**
     * The query for fetching the submitted image with rejected image or not
     * by the list of upcs and active switch equals true and active online equals false and imageFormat is not equals 'TGA'
     * and isShowRejected. If includeRejectedImage is true, then the query return the submitted image and rejected image,
     * or the query is just return the submitted image.
     */
    String QUERY_FIND_SUBMITTED_IMAGE_BY_UPCS ="FROM ProductScanImageURI i WHERE" +
            " i.key.id in (:upcs) AND i.activeSwitch = 'Y'" +
            " AND (true = :includeRejectedImage OR TRIM(i.imageStatusCode) != 'R') AND" +
            " i.activeOnline = 'N' AND i.imageType.imageFormat != 'TGA'";

    /**
     * The query find all active online image
     * by the list of upcs and active switch equals true and active online equals true
     * and imageFormat is not equals 'TGA'.
     */
    String QUERY_FIND_ACTIVE_ONLINE_IMAGE_BY_UPCS = "FROM ProductScanImageURI i WHERE" +
            " i.key.id in (:upcs) AND i.activeSwitch = 'Y'" +
            " AND i.activeOnline = 'Y' AND i.imageType.imageFormat != 'TGA'";

    /**
     * The query find all priamry image
     * by the list of upcs and active switch equals true and image priority code equals 'P'
     * and imageFormat is not equals 'TGA'.
     */
    String QUERY_FIND_PRIMARY_IMAGE_BY_UPCS = "FROM ProductScanImageURI i WHERE" +
            " i.key.id in (:upcs) AND i.activeSwitch = 'Y'" +
            " AND TRIM(i.imagePriorityCode) = 'P' AND i.imageType.imageFormat != 'TGA'";
}
