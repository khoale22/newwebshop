/*
 *  CandidateComments
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the candidate comments. Comments related to work requests in CandidateWorkRequest can be found here.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Entity
@Table(name = "PS_CANDIDATE_CMTS")
public class CandidateComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateCommentsKey key;

    @Column(name = "CMTS_TS")
    private LocalDateTime time;

    @Column(name = "CMTS_USR_ID")
    private String userId;

    @Column(name = "VNDR_VISIBLTY_SW")
    private Boolean vendorVisibility;

    @Column(name = "CMTS_TXT", nullable = false, length = 255)
    private String comments;

    public CandidateCommentsKey getKey() {
        return key;
    }

    public void setKey(CandidateCommentsKey key) {
        this.key = key;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getVendorVisibility() {
        return vendorVisibility;
    }

    public void setVendorVisibility(Boolean vendorVisibility) {
        this.vendorVisibility = vendorVisibility;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateComments that = (CandidateComments) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
