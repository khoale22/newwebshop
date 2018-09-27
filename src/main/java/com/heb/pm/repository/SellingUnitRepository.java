/*
 * SellingUnitRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.SellingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for product scan codes (UPC).
 *
 * @author m314029
 * @since 2.0.3
 */
public interface SellingUnitRepository extends JpaRepository<SellingUnit, Long>{

    /**
     * Find the list of selling unit by product id.
     * @param prodId - The prduct id
     * @return List<SellingUnit>
     */
    List<SellingUnit> findByProdId(long prodId);

    /**
     * Find the list of selling unit by product id.
     * @param prodId - The prduct id
     * @return List<SellingUnit>
     */
    List<SellingUnit> findByProdIdOrderByUpcAsc(long prodId);

    /**
     * Returns the list of UPCs that exist that are part of a user's search criteria in their search session.
     *
     * @param sessionId The ID of the user's session.
     * @return A list of UPCs that match the list the user has provided.
     */
    @Query(value = "select sellingUnit.upc from SellingUnit sellingUnit where exists (select searchCriteria.upc from SearchCriteria searchCriteria where sellingUnit.upc = searchCriteria.upc and searchCriteria.sessionId = :sessionId)")
    List<Long> findAllForSearch(@Param("sessionId") String sessionId);

    @Query(value = "select sellingUnit.upc from SellingUnit sellingUnit where sellingUnit.upc in :upcsGenLst")
    List<Long> findCaseUpcsByUpcGenList(@Param("upcsGenLst") List<Long> upcsGenLst);
}
