/*
 * AlertUserResponseKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.db2;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the composite key of the Alert User Response.
 *
 * @author vn40486
 * @since 2.16.0
 */
@Embeddable
public class AlertUserResponseKey implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "alrt_id")
    private Integer alertID;

    @Column(name = "usr_id")
    private String userId;

    public AlertUserResponseKey() {}

    public AlertUserResponseKey(Integer alertID, String userId) {
        this.alertID = alertID;
        this.userId = userId;
    }

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertUserResponseKey that = (AlertUserResponseKey) o;

        if (!alertID.equals(that.alertID)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = alertID.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertUserResponseKey{" +
                "alertID=" + alertID +
                ", userId='" + userId + '\'' +
                '}';
    }
}
