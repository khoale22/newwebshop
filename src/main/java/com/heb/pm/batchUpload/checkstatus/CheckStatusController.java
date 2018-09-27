/*
 *  CheckStatusController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.checkstatus;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.util.BatchUploadStatus;
import com.heb.pm.batchUpload.util.BatchUploadStatusDetail;
import com.heb.pm.entity.TransactionTracker;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * check status file. will check status for tracking and the detail for tracking id
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CheckStatusController.BATCH_UPLOAD)
@AuthorizedResource(ResourceConstants.CHECK_STATUS)
public class CheckStatusController {
	//URL
	public static final String BATCH_UPLOAD = "/batchUpload";
	private static final String GET_lIST_TRACKING_URL = "/getListTracking";
	private static final String GET_TRACKING_DETAIL_URL = "/getTrackingDetail";
	private static final String GET_TRACKING_BY_ID_URL = "/getTrackingById";
	private static final String EXPORT_TO_CSV_URL = "/exportToCsv";
	//message
	private static final String FIND_TRACKING =
			"User %s from IP %s requested to get list tracking with pagging info (page: %s , page size: %s)";
	private static final String FIND_TRACKING_DETAIL =
			"User %s from IP %s requested to get tracking detail #%s with pagging info (page: %s , page size: %s)";
	private static final String FIND_TRACKING_BY_ID =
			"User %s from IP %s requested to get tracking with tracking id : #%s";
	private static final String EXPORT_TRACKING_DETAIL =
			"User %s from IP %s requested to export csv tracking detail info: #%s";

	private static final String GENERATE_TRACKING_DETAIL_REPORT= "generate tracking detail report error : %s";
	// Defaults related to paging.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	@Autowired
	private CheckStatusService checkStatusService;
	private static final Logger logger = LoggerFactory.getLogger(CheckStatusController.class);

	@Autowired
	private UserInfo userInfo;

	/**
	 * get list tracking and fetch status for each tracking.
	 * @param page the page number
	 * @param pageSize the size of page
	 * @param includeCount  request with count or not.
	 * @param request  The HTTP request that initiated this call.
	 * @return the Pageable Result list tracking info
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_lIST_TRACKING_URL)
	public PageableResult<BatchUploadStatus> getListTracking(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "requestId", required = false, defaultValue = "") String requestId,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCount,
			HttpServletRequest request) {

		logger.info(String.format(CheckStatusController.FIND_TRACKING, this.userInfo.getUserId(),
				request.getRemoteAddr(), page, pageSize));
		int p = page == null ? CheckStatusController.DEFAULT_PAGE : page;
		int s = pageSize == null ? CheckStatusController.DEFAULT_PAGE_SIZE : pageSize;
		return includeCount ? this.checkStatusService.getListTrackingWithCount(
				new PageRequest(p, s, TransactionTracker.getDefaultSort()), requestId) :
				this.checkStatusService.getListTracking(new PageRequest(p, s, TransactionTracker.getDefaultSort()), requestId);
	}
	/**
	 * get tracking detail. the candidate work request associate to tracking.
	 * will fetch some value from product master for product info.
	 * @param page the page number
	 * @param pageSize the size of page
	 * @param trackingId the tracking id to get detail
	 * @param includeCount  request with count or not.
	 * @param request  The HTTP request that initiated this call.
	 * @return the Pageable Result tracking detail
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TRACKING_DETAIL_URL)
	public PageableResult<BatchUploadStatusDetail> getTrackingDetail(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "trackingId", required = false) Long trackingId,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCount,
			HttpServletRequest request) {

		logger.info(String.format(CheckStatusController.FIND_TRACKING_DETAIL, this.userInfo.getUserId(),
				request.getRemoteAddr(), trackingId, page, pageSize));
		int p = page == null ? CheckStatusController.DEFAULT_PAGE : page;
		int s = pageSize == null ? CheckStatusController.DEFAULT_PAGE_SIZE : pageSize;
		return includeCount ? this.checkStatusService
				.getTrackingDetailWithCount(trackingId, new PageRequest(p, s)) :
				this.checkStatusService.getTrackingDetail(trackingId, new PageRequest(p, s));
	}

	/**
	 * Calls to get tracking info by id.
	 * @param trackingId the tracking id
	 * @param request The HTTP request that initiated this call.
	 * @return tracking info
	 * @author vn87351
	 *
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TRACKING_BY_ID_URL)
	public PageableResult<BatchUploadStatus> getTrackingInfoById(@RequestParam(value = "trackingId", required = true)
																		 Long trackingId, HttpServletRequest request) {

		logger.info(String.format(CheckStatusController.EXPORT_TRACKING_DETAIL, this.userInfo.getUserId(),
				request.getRemoteAddr(), trackingId));
		return this.checkStatusService.getTrackingById(trackingId);
	}

	/**
	 * Calls excel export for tracking detail.
	 *
	 * @param trackingId the tracking id to get detail
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 * @author vn87351
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = EXPORT_TO_CSV_URL, headers = "Accept=text/csv")
	public void generateTrackingDetailReport(@RequestParam(value = "trackingId", required = true) Long trackingId,
								 HttpServletRequest request,
								 @RequestParam(value = "downloadId", required = false) String downloadId,
								 HttpServletResponse response) {

		logger.info(String.format(CheckStatusController.FIND_TRACKING_BY_ID, this.userInfo.getUserId(),
				request.getRemoteAddr(), trackingId));
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			response.getOutputStream().println(CheckStatusReportGenerator.CSV_HEADING);
			response.getOutputStream().print(CheckStatusReportGenerator.createCsv(this.checkStatusService
					.getTrackingDetailAll(trackingId)));
		} catch (IOException e) {
			CheckStatusController.logger.error(String.format(GENERATE_TRACKING_DETAIL_REPORT,e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
}
