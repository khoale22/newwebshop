/*
 * NutritionUpdatesController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.db2.AlertStaging;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * NutritionUpdatesController is used to handle requests associates to nutrition updates (alerts).
 *
 * @author vn40486
 * @since 2.11.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutritionUpdatesTaskController.NUTRITION_UPDATES_URL)
@AuthorizedResource(ResourceConstants.TASK_NUTRITION_UPDATES)
public class NutritionUpdatesTaskController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionUpdatesTaskController.class);

    protected  static final String NUTRITION_UPDATES_URL = "/task/nutritionUpdates";

    /*Message Constants*/
    private static final String REJECT_SUCCESS_MESSAGE_KEY ="NutritionUpdates.reject.success";
    private static final String REJECT_ALL_SUCCESS_MESSAGE_KEY ="NutritionUpdates.reject.all.success";
    private static final String REJECT_ALL_FAILURE_MESSAGE_KEY ="NutritionUpdates.reject.all.failure";

    @Autowired
    private NutritionUpdatesTaskService nutritionUpdatesTaskService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserInfo userInfo;

    private static final String LOG_REQUEST_COUNT_REQUEST =
            "User %s from IP %s has submitted request to get count of all open nutrition updates.";
    private static final String LOG_REQUEST_GET_UPDATES =
            "User %s from IP %s has submitted request to get nutrition update alerts by: {includeCounts:%B, page:%d, " +
                    "pageSize:%d} ";
    private static final String LOG_REQUEST_GET_ALL_PRODUCTS =
    		"User %s from IP %s has submitted request to get all products of nutrition update task by: {includeCounts:%B, page:%d, " +
    				"pageSize:%d} ";
    private static final String REJECT_ALL_UPDATES_LOG_MESSAGE =
            "User %s from IP %s has submitted Reject All Nutrition Updates with following exclusions: [%s]";
    private static final String REJECT_UPDATES_LOG_MESSAGE =
            "User %s from IP %s has submitted Reject Nutrition Updates for Alerts: [%s]";
    private static final String LOG_REQUEST_EXPORT_CSV =
            "User %s from IP %s has submitted request to export as csv all tasks";

    private static final String GENERATE_TASK_DETAIL_REPORT = "generate task detail report error : %s";

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/nutritionUpdatesCount")
    public ModifiedEntity<Long> getActiveNutritionUpdatesCount(HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_COUNT_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<Long>(this.nutritionUpdatesTaskService.getActiveNutritionUpdatesCount(), null);
        return modifiedEntity;
    }

    /**
     * Returns nutrition updates alerts filtered by page size.
     * @param includeCounts include pagination counts info.
     * @param page page number.
     * @param pageSize page size.
     * @return list of alert staging results.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/getAllActiveNutritionUpdates")
    PageableResult<AlertStaging> getAllActiveNutritionUpdates(
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_UPDATES, this.userInfo.getUserId(), request.getRemoteAddr(), includeCounts, page,
                pageSize);
        return nutritionUpdatesTaskService.getAllActiveNutritionUpdates(includeCounts, page, pageSize);
    }

    /**
     * Returns nutrition updates alerts filtered by page size.
     * @param includeCounts include pagination counts info.
     * @param page page number.
     * @param pageSize page size.
     * @return list of alert staging results.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/getAllProducts")
    PageableResult<AlertStaging> getAllProducts(
    		@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "pageSize", required = false) Integer pageSize,
    		HttpServletRequest request) {
    	this.logRequest(LOG_REQUEST_GET_ALL_PRODUCTS, this.userInfo.getUserId(), request.getRemoteAddr(), includeCounts, page,
    			pageSize);
    	return nutritionUpdatesTaskService.getAllProducts(includeCounts, page, pageSize);
    }

    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "/rejectUpdates")
    public ModifiedEntity<String> rejectUpdates(
                @RequestParam(value = "alertIds", required = true) List<Integer> alertIds,
            @RequestParam(value = "rejectReason", required = true) String rejectReason,
            HttpServletRequest request) {
        this.logRequest(REJECT_UPDATES_LOG_MESSAGE, request.getRemoteAddr(), this.userInfo.getUserId(), alertIds);
        /**
         * Spring is normally converting single number request parameter to Long. Hence the following typecast.
         */
        if (alertIds != null && alertIds.size() == 1) {
            alertIds.set(0, NumberUtils.toInt(String.valueOf(alertIds.get(0))));
        }
        nutritionUpdatesTaskService.rejectUpdates(alertIds, rejectReason, this.userInfo.getUserId());
        String updateMessage = this.messageSource.getMessage(
                REJECT_SUCCESS_MESSAGE_KEY,
                null,
                REJECT_SUCCESS_MESSAGE_KEY, request.getLocale());
        return new ModifiedEntity<>(null, updateMessage);
    }

    /**
     * Rejects all nutrition updates excluding the one in the excludedAlertIds list.
     * @param excludedAlertIds alerts not to be rejected.
     * @param rejectReason reason for reject.
     * @param request http request.
     * @return reject batch submit status.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "/rejectAllUpdates")
    public ModifiedEntity<String> rejectAllUpdates(
            @RequestParam(value = "excludedAlertIds", required = false) List<Long> excludedAlertIds,
            @RequestParam(value = "rejectReason", required = true) String rejectReason, HttpServletRequest request) {
        this.logRequest(REJECT_ALL_UPDATES_LOG_MESSAGE, request.getRemoteAddr(), this.userInfo.getUserId(), excludedAlertIds);
        String status = nutritionUpdatesTaskService.rejectAllUpdates(excludedAlertIds, rejectReason, this.userInfo.getUserId());
        //compose reply message.
        String replyMessageKey = status.equals(NutritionUpdatesTaskService.MESSAGE_BATCH_SUBMIT_SUCCESS) ?
                REJECT_ALL_SUCCESS_MESSAGE_KEY :  REJECT_ALL_FAILURE_MESSAGE_KEY;
        String updateMessage = this.messageSource.getMessage(replyMessageKey,null,replyMessageKey, request.getLocale());
        return new ModifiedEntity<>(null, updateMessage);
    }

    /**
     * Used to log the incoming requests.
     * @param logMessage message template to be logged.
     * @param arguments messsage arguments containing request specifics.
     */
    private void logRequest(String logMessage, Object... arguments) {
        NutritionUpdatesTaskController.logger.info(String.format(logMessage, arguments));
    }

    /**
     * Used to get a list of all product update tasks for alert type and assignee.
     * @param request
     * @return list of all tasks.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/exportNutritionUpdatesToCsv")
    public void exportNutritionUpdatesToCsv(
            @RequestParam(value = "downloadId", required = false) String downloadId,
            HttpServletRequest request,
            HttpServletResponse response) {
        this.logRequest(LOG_REQUEST_EXPORT_CSV, this.userInfo.getUserId(), request.getRemoteAddr(), downloadId);

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try {
            this.nutritionUpdatesTaskService.streamAllTaskDetails(response.getOutputStream());
        } catch (IOException e) {
            NutritionUpdatesTaskController.logger.error(String.format(GENERATE_TASK_DETAIL_REPORT,e.getMessage()));
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
}
