/*
 * EcommerceTaskPredicateBuilder.java
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to build predicates for fetch products under an ecommerce task based on basic conditions(like
 * tracking id, product status, sales channel) and filter conditions (like show on site yes/no).
 *
 * @author vn40486
 * @since 2.17.0
 */
@Service
public class EcommerceTaskPredicateBuilder {

    private static final String LIKE = "%";
    private static final String SHOW_ON_SITE_YES = "Y";


    /**
     * Used to build predicates(WHERE clause) for fetching products under the task. Makes a list of predicates based on the
     * filter condition input. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root  The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param trackingId tracking of the alert/task.
     * @param assignee alert assigned to user.
     * @param showOnSite show on site Y/N.
     * @return
     */
    protected Predicate buildPredicate(Root<CandidateWorkRequest> root, CriteriaQuery<CandidateWorkRequest> queryBuilder,
                                       CriteriaBuilder criteriaBuilder, Long trackingId, String assignee, String showOnSite) {
        Specification<CandidateWorkRequest> spec = buildSpecification(trackingId, assignee, showOnSite);
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searchign product updates.
     * @param trackingId tracking of the alert/task.
     * @param assignedUserID alert assigned to user.
     * @param showOnSite show on site Y/N.
     * @return
     */
    private Specification<CandidateWorkRequest> buildSpecification(Long trackingId, String assignedUserID, String showOnSite) {
        return new Specification<CandidateWorkRequest>() {
            @Override
            public Predicate toPredicate(Root<CandidateWorkRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get(CandidateWorkRequest_.trackingId), trackingId));
                predicates.add(cb.equal(root.get(CandidateWorkRequest_.status), CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD));
                if(assignedUserID != null && !assignedUserID.trim().isEmpty()) {
                    predicates.add(cb.equal(cb.upper(root.get(CandidateWorkRequest_.lastUpdateUserId)), assignedUserID.toUpperCase()));
                }
                if(StringUtils.isNotBlank(showOnSite)) {
                    Subquery<ProductOnline> showOnSiteSubquery = buildShowOnSitePredicate(showOnSite, root, query, cb);
                    if(showOnSite.equalsIgnoreCase(SHOW_ON_SITE_YES)) {
                        predicates.add(cb.exists(showOnSiteSubquery));
                    } else {
                        predicates.add(cb.not(cb.exists(showOnSiteSubquery)));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    /**
     * Builds predicate to fetch alert referenced product with matching show on site. show on site status is decided by
     * a set of parameters like record exist with valid effective and expiration dates.
     * @param showOnSite show on site Y/N
     * @param root The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb criteria builder, used to construct the various parts of the SQL statement.
     * @return filter by show on site predicate.
     */
    private Subquery<ProductOnline> buildShowOnSitePredicate(String showOnSite, Root<CandidateWorkRequest> root,
                                                                CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ProductOnline> productOnlineExist = queryBuilder.subquery(ProductOnline.class);
        Root<ProductOnline> poRoot = productOnlineExist.from(ProductOnline.class);
        productOnlineExist.select(poRoot.get("key").get("productId"));

        Predicate[] predicates = new Predicate[5];
        predicates[0] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.productId),
                root.get(CandidateWorkRequest_.productId));
        predicates[1] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.saleChannelCode),
                SalesChannel.SALES_CHANNEL_HEB_COM);
        predicates[2] = cb.equal(poRoot.get(ProductOnline_.showOnSite), true);
        predicates[3] = cb.lessThanOrEqualTo(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.effectiveDate),LocalDate.now());
        predicates[4] = cb.greaterThanOrEqualTo(poRoot.get(ProductOnline_.expirationDate),LocalDate.now());
        productOnlineExist.where(cb.and(predicates));

        return productOnlineExist;
    }
}
