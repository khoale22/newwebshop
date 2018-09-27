package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceAuthorizeRetailRepository;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceAuthorizeRetailRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Holds the business logic for scale maintenance authorization and retail.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceAuthorizeRetailService {

	@Autowired
	private ScaleMaintenanceAuthorizeRetailRepository repository;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailRepositoryWithCount repositoryWithCount;

	public Set<Integer> getAuthorizedStoresByTransactionId(Long transactionId) {
		return this.repository.getUniqueStoresByTransactionIdAndAuthorized(transactionId);
	}

	public List<ScaleMaintenanceAuthorizeRetail> getAuthorizedByTransactionIdAndStore(Long transactionId, Integer store) {
		return this.repository.getUniqueByKeyTransactionIdAndKeyStoreAndAuthorized(transactionId, store, Boolean.TRUE);
	}

	public List<ScaleMaintenanceAuthorizeRetail> findByTransactionIdAndStore(Long transactionId, Integer store) {
		return this.repository.findByKeyTransactionIdAndKeyStore(transactionId, store);
	}

	/**
	 * This method finds all scale maintenance retail information by transaction id and by store.
	 *
	 * @param page The page requested.
	 * @param pageSize The page size requested.
	 * @param includeCount Whether to include count.
	 * @param transactionId Transaction id to search for
	 * @param store Store number to search for.
	 * @return Page of scale maintenance retails.
	 */
	public PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndStore(
			Integer page, Integer pageSize, Boolean includeCount, Long transactionId, Integer store){

		PageRequest request = new PageRequest(page, pageSize);
		return includeCount ?
				this.findAllByTransactionIdAndByStoreWithCount(transactionId, store, request) :
				this.findAllByTransactionIdAndByStoreWithOutCount(transactionId, store, request);
	}

	/**
	 * Finds all scale maintenance retails sorted including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param store Store number for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndByStoreWithCount(
			Long transactionId, Integer store, PageRequest request) {
		Page<ScaleMaintenanceAuthorizeRetail> data = this.repositoryWithCount
				.findByKeyTransactionIdAndKeyStore(transactionId, store, request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

	/**
	 * Finds all scale maintenance retails without including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param store Store number for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndByStoreWithOutCount(
			Long transactionId, Integer store, PageRequest request) {
		List<ScaleMaintenanceAuthorizeRetail> data = this.repository
				.findByKeyTransactionIdAndKeyStore(transactionId, store, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}
}
