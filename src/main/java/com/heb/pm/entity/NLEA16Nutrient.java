/*
 *
 * NLEA16Nutrient.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a NLEANutrient.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Entity
@Table(name = "ntrnt")
public class NLEA16Nutrient implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NTRNT_ID")
	private Long nutrientId;

	@Column(name = "NTRNT_DES")
	private String nutrientDescription;

	@Column(name = "NTRNT_DSPLY_SEQ_NBR")
	private Integer nutrientDisplaySequenceNumber;

	@Column(name = "RCMD_DALY_VAL_QTY")
	private Double recommendedDailyAmount;

	/**
	 * Get the nutrientId.
	 *
	 * @return the nutrientId.
	 */
	public Long getNutrientId() {
		return nutrientId;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param nutrientId the nutrientId to set.
	 */
	public void setNutrientId(Long nutrientId) {
		this.nutrientId=nutrientId;
	}

	/**
	 * Get the nutrientDescription.
	 *
	 * @return the nutrientDescription.
	 */
	public String getNutrientDescription() {
		return nutrientDescription;
	}

	/**
	 * Set the nutrientDescription.
	 *
	 * @param nutrientDescription the nutrientDescription to set.
	 */
	public void setNutrientDescription(String nutrientDescription) {
		this.nutrientDescription=nutrientDescription;
	}

	/**
	 * Get the nutrientDisplaySequenceNumber.
	 *
	 * @return the nutrientDisplaySequenceNumber.
	 */
	public Integer getNutrientDisplaySequenceNumber() {
		return nutrientDisplaySequenceNumber;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param nutrientDisplaySequenceNumber the nutrientDisplaySequenceNumber to set.
	 */
	public void setNutrientDisplaySequenceNumber(Integer nutrientDisplaySequenceNumber) {
		this.nutrientDisplaySequenceNumber=nutrientDisplaySequenceNumber;
	}

	/**
	 * Get the recommendedDailyAmount.
	 *
	 * @return the recommendedDailyAmount.
	 */
	public Double getRecommendedDailyAmount() {
		return recommendedDailyAmount;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param recommendedDailyAmount the recommendedDailyAmount to set.
	 */
	public void setRecommendedDailyAmount(Double recommendedDailyAmount) {
		this.recommendedDailyAmount=recommendedDailyAmount;
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
		if (!(o instanceof NLEA16Nutrient)) return false;

		NLEA16Nutrient that=(NLEA16Nutrient) o;

		return getNutrientId().equals(that.getNutrientId());
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return getNutrientId().hashCode();
	}

	@Override
	public String toString() {
		return "NLEANutrient{" +
				"nutrientId=" + nutrientId +
				", nutrientDescription='" + nutrientDescription + '\'' +
				", nutrientDisplaySequenceNumber=" + nutrientDisplaySequenceNumber +
				", recommendedDailyAmount=" + recommendedDailyAmount +
				'}';
	}
}
