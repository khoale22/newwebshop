package com.heb.pm.repository;

import com.heb.pm.entity.TransactionTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface TransactionTrackingRepositoryWithCount extends JpaRepository<TransactionTracker, Long>,TransactionTrackingRepositoryCommon {
	/**
	 * find the summary batch upload status with count
	 * @param lstSource the resource id
	 * @param lstFileName file name
	 * @param pageable page transaction tracking
	 * @return Page
	 * @author vn87351
	 */
	@Query(FIND_TRACKING)
	Page<TransactionTracker> getListTracking(@Param("listSource") List<Integer> lstSource,
											 @Param("listFileName") List<String> lstFileName,
											 @Param("requestId") String requestId,
											 Pageable pageable);

}
