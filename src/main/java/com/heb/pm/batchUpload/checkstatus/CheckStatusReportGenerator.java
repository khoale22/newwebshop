/*
 *  CheckStatusReportGenerator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.checkstatus;

import com.heb.pm.batchUpload.util.BatchUploadStatusDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * generate to csv on check status page
 *
 * @author vn87351
 * @since 2.12.0
 */
public class CheckStatusReportGenerator {
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
	public static final String CSV_HEADING = "Product ID, Product Description, Size,Primary UPC,Result,Error Message";
	/**
	 * Creates a CSV string from a list of object detail tracking.
	 *
	 * @param lst a list of tracking detail.
	 * @return a CSV string with tracking detail information.
	 */
	public static String createCsv(List<BatchUploadStatusDetail> lst){
		StringBuilder csv = new StringBuilder();
		for(BatchUploadStatusDetail batchUploadStatusDetail : lst){
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getProductId()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getProductDescription()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getSize()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getPrimaryUpc()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getUpdateResult()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, batchUploadStatusDetail.getErrorMessage()));
			csv.append(NEWLINE_TEXT_EXPORT_FORMAT);
		}
		return csv.toString();
	}
}
