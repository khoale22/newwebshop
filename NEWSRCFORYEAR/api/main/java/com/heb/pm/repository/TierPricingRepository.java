package com.heb.pm.repository;

import com.heb.pm.entity.TierPricing;
import com.heb.pm.entity.TierPricingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface attribute repository.
 * @author  vn70633
 * @since 2.19.0
 */
public interface TierPricingRepository extends JpaRepository<TierPricing, TierPricingKey> {

    /**
     * Find tier pricing by product id.
     *
     * @param prodId the product id
     * @return the list
     */
    List<TierPricing> findByKeyProdId(Long prodId);
}
