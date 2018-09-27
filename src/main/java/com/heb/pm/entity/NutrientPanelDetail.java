/*
 *
 * NutrientPanelDetail.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents NLEA Nutrient statement detail.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Entity
@Table(name = "ntrn_pan_dtl")
public class NutrientPanelDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NutrientPanelDetailKey key;

	@Column(name = "NTRNT_QTY")
	private Double nutrientQuantity;

	@Column(name = "DALY_VAL_PCT")
	private Double nutrientDailyValue;

	@ManyToOne
	@JoinColumn(name = "NTRNT_ID", referencedColumnName = "NTRNT_ID", insertable = false, updatable = false, nullable = false)
	private NLEA16Nutrient nutrient;

	@JsonIgnoreProperties("nutrientStatementDetailList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NTRN_PAN_HDR_ID", referencedColumnName = "NTRN_PAN_HDR_ID", insertable = false, updatable = false, nullable = false)
	private NutrientStatementPanelHeader nutrientStatementPanelHeader;

	/**
	 * Get the key.
	 *
	 * @return the key.
	 */
	public NutrientPanelDetailKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(NutrientPanelDetailKey key) {
		this.key=key;
	}

	/**
	 * Get the nutrientQuantity.
	 *
	 * @return the nutrientQuantity
	 */
	public Double getNutrientQuantity() {
		return nutrientQuantity;
	}

	/**
	 * Set the nutrientQuantity.
	 *
	 * @param nutrientQuantity the nutrientQuantity to set
	 */
	public void setNutrientQuantity(Double nutrientQuantity) {
		this.nutrientQuantity=nutrientQuantity;
	}

	/**
	 * Get the nutrientDailyValue.
	 *
	 * @return the nutrientDailyValue.
	 */
	public Double getNutrientDailyValue() {
		return nutrientDailyValue;
	}

	/**
	 * Set the nutrientDailyValue.
	 *
	 * @param nutrientDailyValue the nutrientDailyValue to set
	 */
	public void setNutrientDailyValue(Double nutrientDailyValue) {
		this.nutrientDailyValue=nutrientDailyValue;
	}

	/**
	 * Get the nutrient.
	 *
	 * @return the nutrient.
	 */
	public NLEA16Nutrient getNutrient() {
		return nutrient;
	}

	/**
	 * Set the nutrient.
	 *
	 * @param nutrient the nutrient to set
	 */
	public void setNutrient(NLEA16Nutrient nutrient) {
		this.nutrient=nutrient;
	}

	/**
	 * Get the nutrientStatementPanelHeader.
	 *
	 * @return the nutrientStatementPanelHeader.
	 */
	public NutrientStatementPanelHeader getNutrientStatementPanelHeader() {
		return nutrientStatementPanelHeader;
	}

	/**
	 * Set the nutrientStatementPanelHeader.
	 *
	 * @param nutrientStatementPanelHeader the nutrientStatementPanelHeader to set
	 */
	public void setNutrientStatementPanelHeader(NutrientStatementPanelHeader nutrientStatementPanelHeader) {
		this.nutrientStatementPanelHeader=nutrientStatementPanelHeader;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NutrientPanelDetail)) return false;

		NutrientPanelDetail that=(NutrientPanelDetail) o;

		return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return getKey() != null ? getKey().hashCode() : 0;
	}

	@Override
	public String toString() {
		return "NutrientPanelDetail{" +
				"key=" + key +
				", nutrientQuantity=" + nutrientQuantity +
				", nutrientDailyValue=" + nutrientDailyValue +
				'}';
	}
}

