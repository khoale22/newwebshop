/*
 * NutritionUpdatesMassUpdateReader
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.db2.AlertStaging;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.util.jpa.PageableResult;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NutritionUpdatesMassUpdateReader is invoked when a batch request(mass update) is submitted for the
 * nutrition updates(rejects). It reads the nutrition updates realted alerts from database in batch mode and feeds to
 * the batch processor.
 *
 * @author vn40486
 * @since 2.13.0
 */
public class NutritionUpdatesMassUpdateReader implements ItemReader<AlertStaging>, StepExecutionListener {

    private static final int BATCH_SIZE = 100;
    private int page = 0;
    private int totalPages = 0;
    private Iterator<AlertStaging> alertStagings;

    /**
     * Job tracking id.
     */
    @Value("#{jobParameters['trackingId']}")
    private Long trackingId;

    @Autowired
    private MassUpdateProductMap massUpdateProductMap;

    @Autowired
    private NutritionUpdatesTaskService nutritionUpdatesTaskService;
    @Override
    public AlertStaging read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        return getNext();
    }

    /**
     * Initialize the state of the listener with the {@link StepExecution} from
     * the current scope.
     *
     * @param stepExecution
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        //Find total number of records to processed.
        Long totalNoOfRecords = this.nutritionUpdatesTaskService.getActiveNutritionUpdatesCount();
        calculatePagination(totalNoOfRecords);
    }

    /**
     * Give a listener a chance to modify the exit status from a step. The value
     * returned will be combined with the normal exit status using
     * {@link ExitStatus#and(ExitStatus)}.
     * <p>
     * Called after execution of step's processing logic (both successful or
     * failed). Throwing exception in this method has no effect, it will only be
     * logged.
     *
     * @param stepExecution
     * @return an {@link ExitStatus} to combine with the normal value. Return
     * null to leave the old value unchanged.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    /**
     * Used to perform pagination claculation for the batch processing.
     * @param totalNoOfRecords total number of records when batch was submitted.
     */
    private void calculatePagination(Long totalNoOfRecords) {
        if (totalNoOfRecords > 0) {
            this.totalPages = (int) Math.ceil((float)totalNoOfRecords/this.BATCH_SIZE);
        }
    }

    /**
     * Fetches an AlertStaging from the batch to be processed.
     * @return an AlertStaging records.
     */
    private AlertStaging getNext() {
        if(this.alertStagings != null && this.alertStagings.hasNext()) {
            return this.alertStagings.next();
        } else {
            this.alertStagings = fetchNextSet();
            return this.alertStagings.hasNext() ? this.alertStagings.next() : null;
        }
    }

    /**
     * Fetches next of batch to be executed.
     * @return a set of AlertStaging records.
     */
    private Iterator<AlertStaging> fetchNextSet() {
        Iterable<AlertStaging> resultList = new ArrayList<>();
        if (this.page < this.totalPages ) {
            PageableResult<AlertStaging> result = this.nutritionUpdatesTaskService.
                    getAllActiveNutritionUpdates(false, this.page++, this.BATCH_SIZE);
            if (result != null & result.getData() != null) {
                resultList = result.getData();
                applyExclusions(resultList);
                //If applyExclusions has removed all the items in the list, fetch next of result. Otherwise getNext()
                // will return NULL which will force batch to abruptly conclude.
                if(!result.getData().iterator().hasNext()) { fetchNextSet();}
            }
        }
        return resultList.iterator();
    }

    /**
     * Removes the alerts excluded by user during mass update (reject all).
     * @param resultList list of alerts.
     */
    private void applyExclusions(Iterable<AlertStaging> resultList) {
        List<Long> productIds = massUpdateProductMap.get(this.trackingId).getProductIds();
        if (productIds != null && !productIds.isEmpty()) {
            Iterator<AlertStaging> alertStagingIterator = resultList.iterator();
            while(alertStagingIterator.hasNext()) {
                if (productIds.contains(alertStagingIterator.next().getAlertID().longValue())) {
                    alertStagingIterator.remove();
                }
            }
        }
    }
}