/*
 *  CheckStatusService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.checkstatus;

import com.heb.pm.batchUpload.util.BatchUploadStatus;
import com.heb.pm.batchUpload.util.BatchUploadStatusDetail;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * check status service.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Service
public class CheckStatusService{

	/**
	 * log message
	 */
	private static final Logger logger = LoggerFactory.getLogger(CheckStatusService.class);
	//Batch Upload status
	public static final String RESULT_ACTIVATED = "Activated";
	public static final String RESULT_ACTIVE_FAILED = "Activation Failed";
	public static final String RESULT_BATCH_UPLOAD = "BATCH UPLOAD DATA";
	private static final String SUMMARY_DETAIL="Success: %d of %d Failure: %d of %d";

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;
	@Autowired
	private TransactionTrackingRepositoryWithCount transactionTrackingRepositoryWithCount;
	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;
	@Autowired
	private CandidateWorkRequestRepositoryWithCounts candidateWorkRequestRepositoryWithCounts;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private CandidateStatusRepository candidateStatusRepository;
	
	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	/**
	 * get tracking detail.using pagging to get data.
	 * @param trackingId the tracking id
	 * @return pageable
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatusDetail> getTrackingDetail(long trackingId, Pageable request) {
		List<CandidateWorkRequest> data = candidateWorkRequestRepository
				.findByTransactionTrackingTrackingId(trackingId, request);
		return new PageableResult<>(request.getPageNumber(), convertViewDetail(data));
	}

	/**
	 * get tracking detail.using pagging to get data.
	 *
	 * @param trackingId the tracking id
	 * @return pageable
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatusDetail> getTrackingDetailWithCount(long trackingId, PageRequest pgRequest) {
		Page<CandidateWorkRequest> data = candidateWorkRequestRepositoryWithCounts
				.findByTransactionTrackingTrackingId(trackingId, pgRequest);
		return new PageableResult(pgRequest.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), convertViewDetail(data.getContent()));
	}

	/**
	 * get tracking detail for export csv. get all not pagging
	 * @param trackingId the tracking id
	 * @return List object
	 * @author vn87351
	 */
	public List<BatchUploadStatusDetail> getTrackingDetailAll(long trackingId){
		return convertViewDetail(candidateWorkRequestRepository.findByTransactionTrackingTrackingId(trackingId));
	}

	/**
	 * convert view detail
	 * @param lstRs list work request
	 * @return List object
	 * @author vn87351
	 */
	private List<BatchUploadStatusDetail> convertViewDetail(List<CandidateWorkRequest> lstRs){
		List<BatchUploadStatusDetail> lst = new ArrayList<>();
		List<Long> lstProductIds = new ArrayList<>();
	    lstRs.forEach(obj->{
			BatchUploadStatusDetail batchUploadStatusDetail = convertViewDetail(obj);
			lst.add(batchUploadStatusDetail);
			if(null!=batchUploadStatusDetail.getProductId() && batchUploadStatusDetail.getProductId() > 0){
				lstProductIds.add(batchUploadStatusDetail.getProductId());
			}
		});
		int startIndex;
		int lastIndex;
		int totalRecords = lstProductIds.size();
		List<ProductMaster> lstProductMaster = new ArrayList<>();
		for (int i = 0; i < ((float) totalRecords / 100); i++) {
			startIndex = i * 100;
			lastIndex = (i + 1) * 100;
			if (lastIndex > totalRecords) {
				lastIndex = totalRecords;
			}

			List<Long> subList = lstProductIds.subList(startIndex, lastIndex);
			lstProductMaster.addAll(productInfoRepository.findAll(subList));
		}

		ProductMaster productMaster = null;
		for (BatchUploadStatusDetail batchUploadStatusDetail :lst) {
			productMaster = new ProductMaster();
			productMaster.setProdId(batchUploadStatusDetail.getProductId());
			if(lstProductMaster.contains(productMaster)){
				productMaster = lstProductMaster.get(lstProductMaster.indexOf(productMaster));
				batchUploadStatusDetail.setProductDescription(productMaster.getDescription());
				batchUploadStatusDetail.setPrimaryUpc(productMaster.getProductPrimaryScanCodeId());
				batchUploadStatusDetail.setSize(productMaster.getProductSizeText());
			}
		}
		return lst;
	}

	/**
	 * convert candidate work request to check status detail
	 * @param candidateWorkRequest CandidateWorkRequest
	 * @return BatchUploadStatusDetail
	 * @author vn87351
	 */
	private BatchUploadStatusDetail convertViewDetail(CandidateWorkRequest candidateWorkRequest){
		BatchUploadStatusDetail obj = new BatchUploadStatusDetail();
		obj.setProductId(0L);
		if (candidateWorkRequest.getProductId() == null) {
				if(null!=candidateWorkRequest.getUpc() && candidateWorkRequest.getUpc() > 0){
					SellingUnit sellingUnit = sellingUnitRepository.findOne(candidateWorkRequest.getUpc());
					if (null!=sellingUnit){
						obj.setProductId(sellingUnit.getProdId());
					}
				}

		} else {
			obj.setProductId(candidateWorkRequest.getProductId());
		}
		obj.setUpdateResult(getStatusDescription(candidateWorkRequest.getStatus()));

		CandidateStatus lst=candidateStatusRepository
				.findFirstByKeyWorkRequestIdOrderByKeyLastUpdateDateDesc(candidateWorkRequest.getWorkRequestId());
		if(lst!=null){
			obj.setErrorMessage(StringUtils.trimToEmpty(lst.getCommentText()));
		}else {
			obj.setErrorMessage(BatchUploadStatusDetail.ERROR_MESSAGE_DEFAULT);
		}
		if (candidateWorkRequest.getUpc() == null) {
			obj.setUpc(0L);
		} else {
			obj.setUpc(candidateWorkRequest.getUpc());
		}
		return obj;
	}

	/**
	 * get tracking info by id
	 * @param trackingId the tracking id
	 * @return page check status
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatus> getTrackingById(long trackingId){
		logger.info("service get list tracking by Id");
		TransactionTracker transactionTracking	= transactionTrackingRepository
				.findOne(trackingId);
		transactionTracking.getCandidateWorkRequest().size();
		List<TransactionTracker> tmpLst = new ArrayList<>();
		tmpLst.add(transactionTracking);
		return new PageableResult(0, getListCheckStatus(tmpLst));
	}

	/**
	 * get list tracking info
	 * @return page check status object
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatus> getListTrackingWithCount(PageRequest pageRequest, String trackingId){
		logger.info("service get list tracking");

		Page<TransactionTracker> data = transactionTrackingRepositoryWithCount.getListTracking(
				getListResourceDefault(), getListFileNameDefault(), trackingId, pageRequest);
		return new PageableResult(pageRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				getListCheckStatus(data.getContent()));
	}

	/**
	 * get list tracking info
	 *
	 * @return page check status object
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatus> getListTracking(PageRequest pageRequest, String trackingId) {
		logger.info("service get list tracking");

		List<TransactionTracker> data = transactionTrackingRepository.getListTracking(
				getListResourceDefault(), getListFileNameDefault(), trackingId, pageRequest);
		return new PageableResult(pageRequest.getPageNumber(), getListCheckStatus(data));
	}

	/**
	 * generate list resource default to get tracking info
	 *
	 * @return list resource id
	 * @author vn87351
	 */
	private List<Integer> getListResourceDefault() {
		List<Integer> lstSource = new ArrayList<>();
		lstSource.add(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE);
		lstSource.add(SourceSystem.SOURCE_SYSTEM_GS1);
		lstSource.add(SourceSystem.SOURCE_SYSTEM_BLOSSOM);
		return lstSource;
	}

	/**
	 * generate list file name default to get tracking info
	 *
	 * @return list file name
	 * @author vn87351
	 */
	private List<String> getListFileNameDefault() {
		List<String> lstType = new ArrayList<>();
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_NEW.getName());
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_UPDATE.getName());
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_WRITE.getName());
		lstType.add(TransactionTracker.FileNameCode.NEW_IMAGE.getName());
		lstType.add(TransactionTracker.FileNameCode.TASKS.getName());
		return lstType;
	}
	/**
	 * Get List BatchUploadStatus.
	 * @param lstTrkg
	 *            List<TransactionTracking>
	 * @return List<BatchUploadStatus>
	 * @author vn87351
	 */
	private List<BatchUploadStatus> getListCheckStatus(List<TransactionTracker> lstTrkg) {
		List<BatchUploadStatus> lstStatus = new ArrayList<>();
		BatchUploadStatus batchUploadStatus;
		for (TransactionTracker trxTracking : lstTrkg) {
			batchUploadStatus = new BatchUploadStatus();
			batchUploadStatus.setRequestId(trxTracking.getTrackingId());
			if(trxTracking.getCreateDate()!=null){
				DateTimeFormatter fomat = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm-ss");
				batchUploadStatus.setDateTime(trxTracking.getCreateDate().format(fomat));
			}else{
				batchUploadStatus.setDateTime(BatchUploadStatus.DATE_DEFAULT);
			}

			batchUploadStatus.setUserId(trxTracking.getUserId());
			batchUploadStatus.setUpdateDescription(trxTracking.getFileDes());
			batchUploadStatus.setAttributeSelected(trxTracking.getFileNm());
			if (null != trxTracking.getTrxStatCd() &&
					TransactionTracker.STAT_CODE_COMPLETE.equals(trxTracking.getTrxStatCd().trim())) {
				batchUploadStatus.setStatus(BatchUploadStatus.STATUS_COMPLETED);
			} else {
				batchUploadStatus.setStatus(BatchUploadStatus.STATUS_IN_PROGRESS);
			}
			if (BatchUploadStatus.STATUS_COMPLETED.equalsIgnoreCase(batchUploadStatus.getStatus())) {
				batchUploadStatus.setResult(getSummaryTrackingProcess(trxTracking.getCandidateWorkRequest()));
			} else {
				batchUploadStatus.setResult(TransactionTracker.SUMMARY_UNKNOWN);
			}
			if (null!= trxTracking.getFileNm() && (trxTracking.getFileNm().trim().equalsIgnoreCase(TransactionTracker
					.FileNameCode
					.IMAGE_CONTENT_TYPE
					.getName()) ||
					trxTracking.getFileNm().trim().equalsIgnoreCase(TransactionTracker.FileNameCode.IMAGE_ATTRIBUTES
							.getName()))) {
				batchUploadStatus.setImageUpload(true);
			}
			lstStatus.add(batchUploadStatus);
		}
		return lstStatus;
	}

	/**
	 * get summary info for batch upload request
	 * @param candidateWorkRequest
	 * @return
	 */
	private String getSummaryTrackingProcess(List<CandidateWorkRequest> candidateWorkRequest){
		int success = calcSuccessCount(candidateWorkRequest);
		return String.format(SUMMARY_DETAIL, success, candidateWorkRequest.size(), candidateWorkRequest.size() - success, candidateWorkRequest.size());
	}
	/**
	 * count candidate work request success
	 * @return count success
	 * @author vn87351
	 */
	private Integer calcSuccessCount(List<CandidateWorkRequest> candidateWorkRequest) {
		int totalPass = 0;
		if(CollectionUtils.isNotEmpty(candidateWorkRequest)){
			for (CandidateWorkRequest psWkrq: candidateWorkRequest) {
				if(CandidateWorkRequest.StatusCode.SUCCESS.getName().equals(StringUtils.trimToEmpty(psWkrq.getStatus()))){
					totalPass+=1;
				}
			}
		}
		return totalPass;
	}

	/**
	 * generate status description for candidate work request
	 * @return desc
	 * @author vn87351
	 */
	private String getStatusDescription(String candidateStatusCode){
		String status=RESULT_BATCH_UPLOAD;
		if (CandidateWorkRequest.StatusCode.SUCCESS.getName().equals(candidateStatusCode)) {
			status=RESULT_ACTIVATED;
		} else if (CandidateWorkRequest.StatusCode.FAILURE.getName().equals(candidateStatusCode)) {
			status=RESULT_ACTIVE_FAILED;
		}
		return status;
	}

}
