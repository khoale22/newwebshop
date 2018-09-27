package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceUpcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds the business logic for scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceUpcService {

	@Autowired
	private ScaleMaintenanceUpcRepository repository;

	public List<ScaleMaintenanceUpc> getByTransactionId(Long transactionId) {
		return this.repository.findByKeyTransactionId(transactionId);
	}
}
