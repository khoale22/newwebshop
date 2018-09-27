/*
 * EcommerceTaskRemoveProductsReader.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.UUID;

/**
 * The reader for the mass update job. It will return a list of products to mass update based on the ecommerce task's
 * or alert referenced transaction id.
 *
 * @author vn40486
 * @since 2.16.0
 */
public class EcommerceTaskRemoveProductsReader implements ItemReader<CandidateWorkRequest>, StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskRemoveProductsReader.class);

	private static final String ERROR_SEARCH_CRITERIA_NOT_AVAILABLE = "No search criteria defined for transaction ID: %d";

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("#{jobParameters['assigneeId']}")
	private String assignee;

	@Value("#{jobParameters['showOnSite']}")
	private String showOnSite;

	@Autowired
	private MassUpdateProductMap massUpdateProductMap;
	@Autowired
	private EcommerceTaskService ecommerceTaskService;

	private int pageSize = 100;
	private int page = 0;
	private ProductSearchCriteria productSearchCriteria;
	private Iterator<CandidateWorkRequest> products;
	/**
	 * Set to TRUE if at least one eligible product (not excluded) found for processing.
	 */
	private boolean isBatchContainsEligibleProducts = false;

	/**
	 * Called by the Spring Batch framework. It will return the next product to add to the mass update
	 *
	 * @return The ID of a product to process.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public CandidateWorkRequest read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		// SetupNextBatch will set the iterator to null once the search service
		// stops returning data.
		if (this.products == null) {
			logger.info("EcommerceTaskRemoveProductsReader with transaction id %s -- Ends", this.transactionId);
			return null;
		}
		// There's may be a list of products to exclude. Loop through the list of products
		// until we find a product ID that's not part of that list.
		while (this.products.hasNext()) {
			CandidateWorkRequest product = this.products.next();
			if (this.productSearchCriteria.getExcludedProducts() == null ||
					!this.productSearchCriteria.getExcludedProducts().contains(product.getProductId())) {
				this.isBatchContainsEligibleProducts = true;
				return product;
			}
		}
		/* At this point, we've read through all the products in the batch, so go get the  next batch. */
		if (!this.isBatchContainsEligibleProducts)  {
			/* If no eligible products are found in the current page(batch), fetch next page(batch) */
			this.page++;
		}
		this.setupNextBatch();
		return this.read();
	}

	/**
	 * Called by the Spring framework when the job starts. This will pull the list of products
	 * from the parameters map so that they can be iterated through.
	 *
	 * @param stepExecution The context the job is executing in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("EcommerceTaskRemoveProductsReader with transaction id %s -- Begins", this.transactionId);
		this.productSearchCriteria = this.massUpdateProductMap.get(this.transactionId);
		if (this.productSearchCriteria == null) {
			throw new IllegalArgumentException(
					String.format(ERROR_SEARCH_CRITERIA_NOT_AVAILABLE, this.transactionId));
		}
		this.productSearchCriteria.setSessionId(UUID.randomUUID().toString());
		this.page = 0;
		this.setupNextBatch();
	}

	/**
	 * Allows for configuring the page size when searching for products. The default is 100.
	 *
	 * @param pageSize The maximum number of records to request from the product search service
	 *                 for each call to the service.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * To be called after a page of data is exhausted. This will load the next batch of products.
	 * It will set the product master list to null once there is no more data available.
	 */
	private void setupNextBatch() {
		PageableResult<CandidateWorkRequest> productData =
				this.ecommerceTaskService.getTaskProducts(this.transactionId, assignee, showOnSite, false, this.page, this.pageSize);
		if (!productData.getData().iterator().hasNext()) {
			this.products = null;
		} else {
			this.products = productData.getData().iterator();
		}
		this.isBatchContainsEligibleProducts = false;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return null
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}