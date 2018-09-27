/*
 * NutritionUpdatesTaskService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.db2.AlertStaging;
import com.heb.db2.AlertStagingRepository;
import com.heb.db2.AlertStagingRepositoryWithCount;
import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateService;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.repository.BdmRepository;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.TransactionTrackingRepository;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.IntegerPopulator;
import com.heb.util.list.LongPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service is used to serve nutrition updates related queries.
 *
 * @author vn40486
 * @since 2.11.0
 */
@Service
public class NutritionUpdatesTaskService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionUpdatesTaskService.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Value("${app.sourceSystemId}")
    private int sourceSystemId;

    private static final String USER_ROLE = "USER";
    private static final String TRANSACTION_ID_PARAMETER = "trackingId";

    public static final String TASK_DETAILS_EXPORT_HEADER = "UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Genesis Published Date, Dept, Last Modified, Last Modified By";
    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String TEXT_EXPORT_FORMAT_LAST = "\"%s\"";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";

    public static final int PAGE_SIZE = 100;

    /**
     * The Product info repository without count.
     */
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private TransactionTrackingRepository transactionTrackingRepository;
    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;
    @Autowired
    private MassUpdateProductMap massUpdateProductMap;
    @Autowired
    private MassUpdateService massUpdateService;
    @Autowired
    private AlertStagingRepository alertStagingRepository;
    @Autowired
    private AlertStagingRepositoryWithCount alertStagingRepositoryWithCount;
    @Autowired
    private ProductInfoResolver productInfoResolver;
    @Autowired
    BdmRepository bdmRepository;

    private static final String NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME = "I18X912";
    private static final String JOB_PARAMETER_REJECT_REASON = "rejectReason";
    private static final String JOB_PARAMETER_USER_ID = "userId";

    public static final String MESSAGE_BATCH_SUBMIT_SUCCESS = "SUCCESS";
    public static final String MESSAGE_BATCH_SUBMIT_FAILURE = "FAILURE";
    private static final String ERROR_NUTRITION_UPDATES_FAILURE = "Error with nutrition updates for alert: %s ";
    private static final String ERROR_BATCH_SUBMIT_FAILURE = "Error with nutrition updates mass update: %s ";


    // The default number of products to search for. This number
    // and fewer will have the maximum performance.
    private static final int DEFAULT_PRODUCT_ID_COUNT = 100;

    /**
     * Src system id 26 denoted "Pending Genesis product update approval"
     */
    private static int CANDIDATE_INTENT_NUTRITION_UPDATES = 26;

    /**
     * Src system id 26 denoted "Pending Genesis product update approval"
     */
    private static int CANDIDATE_SRC_NUTRITION_UPDATES = 26;

    /**
     * Used to get consistent size lists to query runners.
     */
    private LongPopulator longPopulator = new LongPopulator();
    private IntegerPopulator integerPopulator = new IntegerPopulator();


    /**
     * Used to get count of all active nutrition updates.
     * @return count.
     */
    Long getActiveNutritionUpdatesCount() {
        return alertStagingRepository.countByAlertTypeCDAndAlertStatusCD(AlertStaging.AlertTypeCD.GENESIS_AP.getName(),
                AlertStaging.AlertStatusCD.ACTIVE.getName());
    }

    /**
     * Fetches nutrition updates based on the filter condition input arguments. Upon fetching the list of alerts, it
     * also pulls related product information .
     * @param includeCounts to fetch pagination or count info; true or false.
     * @param page page number.
     * @param pageSize number of records in a page.
     * @return list of AlertStanding with/without pagination info.
     */
    PageableResult<AlertStaging> getAllActiveNutritionUpdates(boolean includeCounts, int page, int pageSize) {
        PageableResult<AlertStaging> pageableResult = null;
        //1. Get nutrition updates by pagination.
        Pageable pageable = new PageRequest(page, pageSize);
        if(includeCounts) {
            Page<AlertStaging> result = alertStagingRepositoryWithCount.findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
                    AlertStaging.AlertTypeCD.GENESIS_AP.getName(),AlertStaging.AlertStatusCD.ACTIVE.getName(),pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
           List<AlertStaging> result = alertStagingRepository.findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
                    AlertStaging.AlertTypeCD.GENESIS_AP.getName(),AlertStaging.AlertStatusCD.ACTIVE.getName(),pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        if (pageableResult.getData() != null) {
            //2. Collect product ids from the Alerts.
            List<Long> productIds = new ArrayList<>();
            pageableResult.getData().forEach(alertStaging -> productIds.add(Long.valueOf(alertStaging.getAlertKey().trim())));
            //3. Send collected product-ids to master db to fetch their respective product info.
            this.longPopulator.populate(productIds, DEFAULT_PRODUCT_ID_COUNT);
            List<ProductMaster> productMasters = productInfoRepository.findAll(productIds);
            //4. Update Alerts with their respective ProductMaster.
            Map<Long,ProductMaster> productMasterMap = productMasters.stream().collect(
                    Collectors.toMap(productMaster -> productMaster.getProdId(), productMaster -> productMaster));
            pageableResult.getData().forEach(alertStaging -> alertStaging.setProductMaster(
                    productMasterMap.get(Long.valueOf(alertStaging.getAlertKey().trim()))));
            pageableResult.getData().forEach(alertStaging ->  this.productInfoResolver.fetch(alertStaging.getProductMaster()));

        }
        return pageableResult;
    }

    /**
     * Fetches nutrition updates based on the filter condition input arguments. Upon fetching the list of alerts, it
     * also pulls related product information .
     * @param includeCounts to fetch pagination or count info; true or false.
     * @param page page number.
     * @param pageSize number of records in a page.
     * @return list of AlertStanding with/without pagination info.
     */
    PageableResult<AlertStaging> getAllProducts(boolean includeCounts, int page, int pageSize) {
    	PageableResult<AlertStaging> pageableResult = null;
    	//1. Get nutrition updates by pagination.
    	Pageable pageable = new PageRequest(page, pageSize);
    	if(includeCounts) {
    		Page<AlertStaging> result = alertStagingRepositoryWithCount.findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
    				AlertStaging.AlertTypeCD.GENESIS_AP.getName(),AlertStaging.AlertStatusCD.ACTIVE.getName(),pageable);
    		pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
    	} else {
    		List<AlertStaging> result = alertStagingRepository.findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
    				AlertStaging.AlertTypeCD.GENESIS_AP.getName(),AlertStaging.AlertStatusCD.ACTIVE.getName(),pageable);
    		pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
    	}
    	if (pageableResult.getData() != null) {
    		//2. Collect product ids from the Alerts.
    		List<Long> productIds = new ArrayList<>();
    		pageableResult.getData().forEach(alertStaging -> productIds.add(Long.valueOf(alertStaging.getAlertKey().trim())));
    		//3. Send collected product-ids to master db to fetch their respective product info.
    		this.longPopulator.populate(productIds, DEFAULT_PRODUCT_ID_COUNT);
    		List<ProductMaster> productMasters = productInfoRepository.findAll(productIds);
    		//4. Update Alerts with their respective ProductMaster.
    		Map<Long,ProductMaster> productMasterMap = productMasters.stream().collect(
    				Collectors.toMap(productMaster -> productMaster.getProdId(), productMaster -> productMaster));
    		pageableResult.getData().forEach(alertStaging -> alertStaging.setProductMaster(
    				productMasterMap.get(Long.valueOf(alertStaging.getAlertKey().trim()))));
    		pageableResult.getData().forEach(alertStaging ->  {
    			if(alertStaging.getProductMaster() != null){
    				alertStaging.getProductMaster().getProdId();
    			}
    		});
    		
    	}
    	return pageableResult;
    }

    /**
     * Used to reject set of alerts.
     * @param alertIds list of alerts to be rejected.
     * @param rejectReason reject reason.
     * @param userId user ho submitted the reject request.
     */
    public void rejectUpdates(List<Integer> alertIds, String rejectReason, String userId) {
        integerPopulator.populate(alertIds, DEFAULT_PRODUCT_ID_COUNT);
        /* Prepare: Fetch all AlertStaging matching the selected Alert Ids. */
        List<AlertStaging> alertStagings = this.alertStagingRepository.findAll(alertIds);
        /* Prepare: Collect all product ids from the Alerts. */
        List<Long> alertProductIds = alertStagings.stream().map(AlertStaging::getAlertKey).map(String::trim)
                .map(Long::parseLong).collect(Collectors.toList());
        /* Prepare: Fetch all nutrition updates candidates associated to the products.*/
        List<CandidateWorkRequest> candidateWorkRequests = this.candidateWorkRequestRepository
                .findByIntentAndStatusAndSourceSystemAndProductIdIn(
                CANDIDATE_INTENT_NUTRITION_UPDATES, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD,
                CANDIDATE_SRC_NUTRITION_UPDATES, alertProductIds);
        /* Prepare: Set status of all related work request to status = REJECTED and add a status change reason record.*/
        candidateWorkRequests = NutritionUpdatesHelper
                .setCandidateAsRejectedWithComments(candidateWorkRequests, rejectReason, userId);
        /* Save:  this should update PS_WORK_RQST and insert a record into PS_CANDIDATE_STAT. */
        this.candidateWorkRequestRepository.save(candidateWorkRequests);
        /* Save: update ALERT records using webservice. */
        alertStagings.forEach(alertStaging -> {
            try {
                this.applicationAlertStagingServiceClient.updateAlert(alertStaging.getAlertID(),
                        AlertStaging.AlertStatusCD.CLOSE.getName());
            } catch (CheckedSoapException e) {
                logger.error(String.format(ERROR_NUTRITION_UPDATES_FAILURE, e.getMessage()));
            }
        });
    }

    /**
     * Used to Reject all the updates excluding the ones sent in the excludedAlertIds list.
     * @param excludedAlertIds alerts not to be rejected..
     * @param rejectReason reason for reject.
     * @param userId user who submitted the reject request.
     * @return status of batch submit. success / failure.
     */
    public String rejectAllUpdates(List<Long> excludedAlertIds, String rejectReason, String userId) {
        /* Create unique transaction id */
        TransactionTracker transaction = this.getTransaction(userId);
        /* Prepare : set job configuration */
        Job job = this.getMassUpdateJob();
        // The only parameter is the transaction ID.
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(TRANSACTION_ID_PARAMETER, transaction.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_REJECT_REASON, rejectReason);
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();
        productSearchCriteria.setProductIds(excludedAlertIds);
        this.massUpdateProductMap.add(transaction.getTrackingId(), productSearchCriteria);

        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_BATCH_SUBMIT_FAILURE, e.getMessage()));
            return MESSAGE_BATCH_SUBMIT_FAILURE;
        }
        return MESSAGE_BATCH_SUBMIT_SUCCESS;
    }

    /**
     * Returns a reference to the mass update job.
     *
     * @return A reference to the mass update job.
     */
    private Job getMassUpdateJob() {
        try {
            return this.jobLocator.getJob(NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME);
        } catch (NoSuchJobException e) {
            JobNotDefinedException je = new JobNotDefinedException(NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME);
            throw je;
        }
    }

    /**
     * Saves an entry into the tracking table. This will be used to group all of the mass updates together as one unit.
     *
     * @return The object saved to the tracking table.
     */
    @CoreTransactional
    private TransactionTracker getTransaction(String userId) {
        TransactionTracker t = new TransactionTracker();
        t.setUserId(userId);
        t.setCreateDate(LocalDateTime.now());
        t.setSource(Integer.toString(this.sourceSystemId));
        t.setUserRole(USER_ROLE);
        return this.transactionTrackingRepository.save(t);
    }

    /**
     * Returns complete details of nutrition tasks
     * @param outputStream
     */
    public void streamAllTaskDetails(ServletOutputStream outputStream) {
        // Export the header to the file
        try {
            outputStream.println(TASK_DETAILS_EXPORT_HEADER);
        } catch (IOException e) {
            NutritionUpdatesTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }

        // stream 1st page
        PageableResult<AlertStaging> candidateListPage1 = this.getAllActiveNutritionUpdates(true, 0, PAGE_SIZE);
        int numberOfPages = candidateListPage1.getPageCount();

        this.streamTaskDetailsListPage(outputStream, candidateListPage1);

        // stream remaining pages
        if(numberOfPages > 1) {
            for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
                this.streamTaskDetailsListPage(outputStream, this.getAllActiveNutritionUpdates(false, currentPage, PAGE_SIZE));
            }
        }
    }

    /**
     * Stream the task details to a CSV output stream
     * @param outputStream the output stream
     * @param candidateListPage the candidate to output
     */
    private void streamTaskDetailsListPage(ServletOutputStream outputStream, PageableResult<AlertStaging> candidateListPage) {
        StringBuilder csv = new StringBuilder();
        SimpleDateFormat formatter  = new SimpleDateFormat("MM/dd/yyyy");
        try {
            // UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By";
            candidateListPage.getData().forEach(candidate -> {
                if (candidate.getProductMaster() != null && candidate.getProductMaster().getProdItems() != null && candidate.getProductMaster().getProdItems().size() > 0) {
                    candidate.getProductMaster().getProdItems().forEach(item -> {
                        if (item.getItemMaster() != null && item.getItemMaster().getPrimaryUpc() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs().size() > 0) {
                            item.getItemMaster().getPrimaryUpc().getAssociateUpcs().forEach(upc -> {
                                StringBuilder csvLine = new StringBuilder();
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, upc.getUpc())); // upc
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, item.getKey().getItemCode())); // item code
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, item.getKey().getItemType().equals(ItemMasterKey.WAREHOUSE) ? "WHS" : "DSD")); // channel
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getProdId())); // product ID
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getDescription())); // product description
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getProductSizeText())); // size text
                                if (candidate.getProductMaster().getSubCommodity() != null) {
                                    csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getSubCommodity().getDisplayName().replace("\"", "\"\""))); // sub commodity
                                } else {
                                    csvLine.append(String.format(TEXT_EXPORT_FORMAT, "Unknown"));
                                }
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, bdmRepository.findOne(candidate.getProductMaster().getBdmCode()).getDisplayName())); // BDM
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getSubDepartment().getDisplayName().replace("\"", "\"\""))); // sub department
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, formatter.format(candidate.getResponseByDate()))); // genesis published date
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getDepartmentCode() + candidate.getProductMaster().getSubDepartmentCode())); // dept
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getLastUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))); // last modified
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT_LAST, candidate.getProductMaster().getLastUpdateUserId().trim())); // last modified by
                                csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));
                                csv.append(csvLine);
                            });
                        }
                    });
                }
            });


            outputStream.println(csv.toString());

        } catch (IOException e) {
            NutritionUpdatesTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
}