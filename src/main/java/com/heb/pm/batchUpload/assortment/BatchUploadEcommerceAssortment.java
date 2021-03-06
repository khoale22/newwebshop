/*
 *  ExcelUploadEcommerceAssortment
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.assortment;

import java.util.ArrayList;
import java.util.List;

/**
 * This is BatchUploadEcommerceAssortment class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class BatchUploadEcommerceAssortment {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 3884054421364570290L;

    public static final String SALES_CHANEL_CODE_DEFAULT_BATCH_UPLOAD="01";
    private List<String> errorMessages = new ArrayList<String>();
    private String productId;
    private String fulfillmentProgram;
    private String effectiveStartDate;
    private String effectiveEndDate;
    private String minCustomerOrderQuantity;
    private String minOrderQuantityForDiscount;
    private String minOrderQuantityDiscountType;
    private String minOrderQuantityDiscountValue;
    private String statusCode;
    // set for Candidate Stat
    private String commentText ;
    private String salesChanelCode;

    /**
     * @return the productId
     */
    public String getProductId() {
	return this.productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId) {
	this.productId = productId;
    }

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
	return this.errorMessages;
    }

    /**
     * @param errorMessages
     *            the errorMessages to set
     */
    public void setErrorMessages(List<String> errorMessages) {
	this.errorMessages = errorMessages;
    }

    /**
     * @return the fulfillmentProgram
     */
    public String getFulfillmentProgram() {
	return this.fulfillmentProgram;
    }

    /**
     * @param fulfillmentProgram
     *            the fulfillmentProgram to set
     */
    public void setFulfillmentProgram(String fulfillmentProgram) {
	this.fulfillmentProgram = fulfillmentProgram;
    }

    /**
     * @return the effectiveStartDate
     */
    public String getEffectiveStartDate() {
	return this.effectiveStartDate;
    }

    /**
     * @param effectiveStartDate
     *            the effectiveStartDate to set
     */
    public void setEffectiveStartDate(String effectiveStartDate) {
	this.effectiveStartDate = effectiveStartDate;
    }

    /**
     * @return the effectiveEndDate
     */
    public String getEffectiveEndDate() {
	return this.effectiveEndDate;
    }

    /**
     * @param effectiveEndDate
     *            the effectiveEndDate to set
     */
    public void setEffectiveEndDate(String effectiveEndDate) {
	this.effectiveEndDate = effectiveEndDate;
    }

    /**
     * @return the minCustomerOrderQuantity
     */
    public String getMinCustomerOrderQuantity() {
	return this.minCustomerOrderQuantity;
    }

    /**
     * @param minCustomerOrderQuantity
     *            the minCustomerOrderQuantity to set
     */
    public void setMinCustomerOrderQuantity(String minCustomerOrderQuantity) {
	this.minCustomerOrderQuantity = minCustomerOrderQuantity;
    }

    /**
     * @return the minOrderQuantityForDiscount
     */
    public String getMinOrderQuantityForDiscount() {
	return this.minOrderQuantityForDiscount;
    }

    /**
     * @param minOrderQuantityForDiscount
     *            the minOrderQuantityForDiscount to set
     */
    public void setMinOrderQuantityForDiscount(String minOrderQuantityForDiscount) {
	this.minOrderQuantityForDiscount = minOrderQuantityForDiscount;
    }

    /**
     * @return the minOrderQuantityDiscountValue
     */
    public String getMinOrderQuantityDiscountValue() {
	return this.minOrderQuantityDiscountValue;
    }

    /**
     * @param minOrderQuantityDiscountValue
     *            the minOrderQuantityDiscountValue to set
     */
    public void setMinOrderQuantityDiscountValue(String minOrderQuantityDiscountValue) {
	this.minOrderQuantityDiscountValue = minOrderQuantityDiscountValue;
    }

    /**
     * @return the minOrderQuantityDiscountType
     */
    public String getMinOrderQuantityDiscountType() {
	return this.minOrderQuantityDiscountType;
    }

    /**
     * @param minOrderQuantityDiscountType
     *            the minOrderQuantityDiscountType to set
     */
    public void setMinOrderQuantityDiscountType(String minOrderQuantityDiscountType) {
	this.minOrderQuantityDiscountType = minOrderQuantityDiscountType;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
	return this.statusCode;
    }

    /**
     * @param statusCode
     *            the statusCode to set
     */
    public void setStatusCode(String statusCode) {
	this.statusCode = statusCode;
    }

    /**
     * @return the commentText
     */
    public String getCommentText() {
	return this.commentText;
    }

    /**
     * @param commentText
     *            the commentText to set
     */
    public void setCommentText(String commentText) {
	this.commentText = commentText;
    }
    /**
     * @return the salesChanelCode
     */
    public String getSalesChanelCode() {
        return this.salesChanelCode;
    }
    /**
     * @param salesChanelCode
     *            the salesChanelCode to set
     */
    public void setSalesChanelCode(String salesChanelCode) {
        this.salesChanelCode = salesChanelCode;
    }
}
