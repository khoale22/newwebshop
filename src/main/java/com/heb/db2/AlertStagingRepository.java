/*
 * AlertStagingRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.db2;

import com.heb.db2.AlertStaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA Repository for AlertStagingRepository.
 *
 * @author vn70633
 * @since 2.7.0
 */
public interface AlertStagingRepository extends JpaRepository<AlertStaging, Integer>{

    static final String COUNT_ALERTS_BY_ASSIGNEE = "SELECT count(DISTINCT a.alertID) FROM AlertStaging a  LEFT JOIN a.alertRecipients r" +
            " WHERE a.alertTypeCD = :alertTypeCd AND a.alertStatusCD = :alertStatusCD " +
            " AND (" +
            "   UPPER(a.assignedUserID) = UPPER(:userId) OR UPPER(a.delegatedByUserID) = UPPER(:userId) " +
            "   OR (UPPER(r.key.recipientId) = UPPER(:userId) AND r.key.alertRecipientTypeCode = 'USRID')" +
            " )";

    static final String QUERY_ALL_ALERTS_BY_ASSIGNEE = "SELECT DISTINCT a FROM AlertStaging a  LEFT JOIN a.alertRecipients r" +
            " WHERE a.alertTypeCD = :alertTypeCd" +
            " AND (" +
            "   UPPER(a.assignedUserID) = UPPER(:userId) OR UPPER(a.delegatedByUserID) = UPPER(:userId) " +
            "   OR (UPPER(r.key.recipientId) = UPPER(:userId) AND r.key.alertRecipientTypeCode = 'USRID')" +
            " )  ORDER BY a.responseByDate desc";

    static final String QUERY_ALERTS_BY_ASSIGNEE = "SELECT DISTINCT a FROM AlertStaging a   LEFT JOIN a.alertRecipients r " +
            " WHERE a.alertTypeCD = :alertTypeCd AND a.alertStatusCD = :alertStatusCD " +
            " AND (" +
            "   UPPER(a.assignedUserID) = UPPER(:userId) OR UPPER(a.delegatedByUserID) = UPPER(:userId) " +
            "   OR (UPPER(r.key.recipientId) = UPPER(:userId) AND r.key.alertRecipientTypeCode = 'USRID')" +
            " )  ORDER BY a.responseByDate desc";

    /**
     * Find alert staging by key alert key and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @param alertKey the alert key.
     * @return the alert staging.
     */
    AlertStaging findByAlertTypeCDAndAlertStatusCDAndAlertKey(String alertTypeCd, String alertStatus, String alertKey);

    /**
     * Get count of all alerts filtered by alert status and alert type.
     *
     * @param alertStatusCD alert status code. Typical values are ACTIV and CLOSD.
     * @param alertTypeCD   alert type code. Typical values are GENAP, PRUPD, NWIMG etc.
     * @return  returns count of alerts matching the input filters.
     */
    @Query(value = COUNT_ALERTS_BY_ASSIGNEE)
    Long countByUser(@Param("alertTypeCd") String alertTypeCD,
                     @Param("alertStatusCD") String alertStatusCD, @Param("userId") String userId);

    /**
     * Get count of all alerts filtered by alert status and alert type.
     *
     * @param alertStatusCD alert status code. Typical values are ACTIV and CLOSD.
     * @param alertTypeCD   alert type code. Typical values are GENAP, PRUPD, NWIMG etc.
     * @return  returns count of alerts matching the input filters.
     */
    Long countByAlertTypeCDAndAlertStatusCD(String alertTypeCD, String alertStatusCD);

    /**
     * Find alert staging by alert type and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @param pageRequest
     * @return list of alert staging.
     */
    List<AlertStaging> findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
            String alertTypeCd, String alertStatus, Pageable pageRequest);

    /**
     * Find all alerts (active and closed) by alert type and assignee.
     * @param alertTypeCd alert type code.
     * @param pageRequest pagination info.
     * @return list of alerts.
     */
    @Query(value = QUERY_ALL_ALERTS_BY_ASSIGNEE)
    List<AlertStaging> findAllAlertsByAssignee(@Param("alertTypeCd") String alertTypeCd,
                                               @Param("userId") String userId, Pageable pageRequest);

    /**
     * Find alerts by alert type, status and assignee.
     * @param alertTypeCd alert type code.
     * @param alertStatusCD alert status code.
     * @param userId assignee user id.
     * @param pageRequest pagination info.
     * @return list of alerts.
     */
    @Query(value = QUERY_ALERTS_BY_ASSIGNEE)
    List<AlertStaging> findAlertsByAssignee(@Param("alertTypeCd") String alertTypeCd,
                                            @Param("alertStatusCD") String alertStatusCD,
                                            @Param("userId") String userId, Pageable pageRequest);

    /**
     * Find alert staging by key alert key and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @param alertKey the alert key.
     * @return the alert staging.
     */
    List<AlertStaging> findByAlertTypeCDAndAlertStatusCDAndAlertKeyOrderByAlertKey(String alertTypeCd, String alertStatus,
                                                                                   String alertKey);

    /**
     * Find alert staging by alert type and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @param pageRequest
     * @return list of alert staging.
     */
    @Query(value = "SELECT DISTINCT a.assignedUserID FROM AlertStaging a " +
            " WHERE a.alertTypeCD = :alertTypeCd AND a.alertStatusCD = :alertStatusCD ")
    List<String> findAssigneeByAlertType(@Param("alertTypeCd")String alertTypeCd,
                                         @Param("alertStatusCD") String alertStatus,Pageable pageRequest);
}
