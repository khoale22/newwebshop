/*
 *  ApLocation
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the Ap_Location.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "ap_location")
public class ApLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private ApLocationKey key;

	@Column(name = "rmit_co_nm")
	private String remitCoName;

	/**
	 * Returns the ApLocationKey
	 *
	 * @return key
	 **/
	public ApLocationKey getKey() {
		return key;
	}

	/**
	 * Sets the ApLocationKey
	 *
	 * @param key The ApLocationKey
	 **/
	public void setKey(ApLocationKey key) {
		this.key = key;
	}

	/**
	 * Returns the RemitCoName
	 *
	 * @return RemitCoName
	 **/
	public String getRemitCoName() {
		return remitCoName;
	}

	/**
	 * Sets the RemitCoName
	 *
	 * @param remitCoName The RemitCoName
	 **/
	public void setRemitCoName(String remitCoName) {
		this.remitCoName = remitCoName;
	}

	/**
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return String.format(ApLocation.DISPLAY_NAME_FORMAT, this.getRemitCoName().trim(),
				this.getKey().getApVendorNumber());
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ApLocation{" +
				"apLocationKey=" + key +
				", remitCoName='" + remitCoName + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ApLocation that = (ApLocation) o;

		if (key != null ? !key.equals(that.key) : that.key != null)
			return false;
		return remitCoName != null ? remitCoName.equals(that.remitCoName) : that.remitCoName == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (remitCoName != null ? remitCoName.hashCode() : 0);
		return result;
	}
}
