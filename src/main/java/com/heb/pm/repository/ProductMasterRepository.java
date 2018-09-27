package com.heb.pm.repository;

import com.heb.pm.entity.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for the ProductMaster entity.
 *
 * @author d116773
 * @since 2.13.0
 */
public interface ProductMasterRepository  extends JpaRepository<ProductMaster, Long>{
}
