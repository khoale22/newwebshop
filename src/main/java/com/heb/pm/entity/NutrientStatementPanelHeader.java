/*
 *
 * NutrientStatementPanelHeader.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a nutrient statement panel header.
 *
 * @author vn70633
 * @since 2.20.0
 */
@Entity
@Table(name="NTRN_PAN_HDR")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class NutrientStatementPanelHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SORT = "sourceSystemReferenceId";
    public static final char ACTIVE_SW = 'N';
    public static final char ACTIVE_SW_Y = 'Y';
    public static final Long SRC_SYTEM_ID_17 = 17L;
    @Id
    @Column(name="ntrn_pan_hdr_id")
    private Long nutrientPanelHeaderId;

    @Column(name="src_sys_ref_id")
    @Type(type="fixedLengthCharPK")
    private String sourceSystemReferenceId ;

    @Column(name = "imprl_srvng_sz_txt")
    private String measureQuantity;

    @Column(name = "met_srvng_sz_txt")
    private String metricQuantity;

    @Column(name = "spc_txt")
    private long servingsPerContainer;

    @Column(name = "eff_dt")
    private LocalDate effectiveDate;

    @Column(name= "actv_sw")
    private char statementMaintenanceSwitch;

    @Column(name="scn_cd_id")
    private Long upc;

    @Column(name="src_system_id")
    private Long sourceSystemId;

    @ManyToOne
    @JoinColumn(name = "imprl_srvng_sz_uom_id", referencedColumnName = "uom_id", insertable = false, updatable = false)
    private UnitOfMeasure nutrientImperialUom;

    @ManyToOne
    @JoinColumn(name = "met_srvng_sz_uom_id", referencedColumnName = "uom_id", insertable = false, updatable = false)
    private UnitOfMeasure nutrientMetricUom;

    @JsonIgnoreProperties("nutrientStatementPanelHeader")
    @OneToMany(mappedBy = "nutrientStatementPanelHeader", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientPanelDetail> nutrientPanelDetails = new ArrayList<>();

    /**
     * Get the nutrient panel header Id.
     *
     * @return the NutrientPanelHeaderId
     */
    public Long getNutrientPanelHeaderId() {
        return nutrientPanelHeaderId;
    }

    /**
     * Gets nutrient statement detail list.
     *
     * @return the nutrient statement detail list
     */
    public List<NutrientPanelDetail> getNutrientPanelDetails() {
        return nutrientPanelDetails;
    }

    /**
     * Sets nutrient statement detail list.
     *
     * @param nutrientPanelDetails the nutrient statement detail list
     */
    public void setNutrientPanelDetails(List<NutrientPanelDetail> nutrientPanelDetails) {
        this.nutrientPanelDetails = nutrientPanelDetails;
    }

    /**
     * Set the nutrient panel header id.
     *
     * @param nutrientPanelHeaderId the nutrient panel header id
     */
    public void setNutrientPanelHeaderId(Long nutrientPanelHeaderId) {
        this.nutrientPanelHeaderId = nutrientPanelHeaderId;
    }

    /**
     * Get the source system reference id.
     *
     * @return the source system reference id
     */
    public String getSourceSystemReferenceId() {
        return sourceSystemReferenceId;
    }

    /**
     * Set the source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id
     */
    public void setSourceSystemReferenceId(String sourceSystemReferenceId) {
        this.sourceSystemReferenceId = sourceSystemReferenceId;
    }

    /**
     * Get the upc.
     *
     * @return the upc
     */
    public Long getUpc() {
        return upc;
    }

    /**
     * Set the upc.
     *
     * @param upc the upc to set
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * Get the sourceSystemId.
     *
     * @return the sourceSystemId
     */
    public Long getSourceSystemId() {
        return sourceSystemId;
    }

    /**
     * Set the sourceSystemId.
     *
     * @param sourceSystemId the sourceSystemId to set
     */
    public void setSourceSystemId(Long sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    /**
     * Gets statement maintanance switch.
     *
     * @return the statement maintanance switch
     */
    public char getStatementMaintenanceSwitch() {
        return statementMaintenanceSwitch;
    }

    /**
     * Sets statement maintanance switch.
     *
     * @param statementMaintenanceSwitch the statement maintanance switch
     */
    public void setStatementMaintenanceSwitch(char statementMaintenanceSwitch) {
        this.statementMaintenanceSwitch = statementMaintenanceSwitch;
    }

    /**
     * Gets nutrient nutrientImperialUom.
     *
     * @return the nutrient imperial uom
     */
    public UnitOfMeasure getNutrientImperialUom() {
        return nutrientImperialUom;
    }

    /**
     * Sets nutrient imperial uom.
     *
     * @param nutrientImperialUom  the nutrient imperial uom
     */
    public void setNutrientImperialUom(UnitOfMeasure nutrientImperialUom) {
        this.nutrientImperialUom = nutrientImperialUom;
    }

    /**
     * Gets nutrient metric uom.
     *
     * @return the nutrient metric uom
     */
    public UnitOfMeasure getNutrientMetricUom() {
        return nutrientMetricUom;
    }

    /**
     * Sets nutrient metric uom.
     *
     * @param nutrientMetricUom  the nutrient imperial uom
     */
    public void setNutrientMetricUom(UnitOfMeasure nutrientMetricUom) {
        this.nutrientMetricUom = nutrientMetricUom;
    }

    /**
     * Gets measure quantity.
     *
     * @return the measure quantity
     */
    public String getMeasureQuantity() {
        return measureQuantity;
    }

    /**
     * Sets measure quantity.
     *
     * @param measureQuantity the measure quantity
     */
    public void setMeasureQuantity(String measureQuantity) {
        this.measureQuantity = measureQuantity;
    }

    /**
     * Gets metric quantity.
     *
     * @return the metric quantity
     */
    public String getMetricQuantity() {
        return metricQuantity;
    }

    /**
     * Sets metric quantity.
     *
     * @param metricQuantity the metric quantity
     */
    public void setMetricQuantity(String metricQuantity) {
        this.metricQuantity = metricQuantity;
    }

    /**
     * Gets servings per container.
     *
     * @return the servings per container
     */
    public long getServingsPerContainer() {
        return servingsPerContainer;
    }

    /**
     * Sets servings per container.
     *
     * @param servingsPerContainer the servings per container
     */
    public void setServingsPerContainer(long servingsPerContainer) {
        this.servingsPerContainer = servingsPerContainer;
    }

    /**
     * Gets effective date.
     *
     * @return the effective date
     */
    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets effective date.
     *
     * @param effectiveDate the effective date
     */
    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Returns a default sort for the table.
     *
     * @return A default sort for the table.
     */
    public static Sort getDefaultSort() {
        return new Sort(new Sort.Order(Sort.Direction.DESC, NutrientStatementPanelHeader.DEFAULT_SORT));
    }

    /**
     * check servingsPerContainer  is varies
     */
    public boolean isServingSizeVariable() {
        return this.servingsPerContainer == NutrientStatementHeader.SERVINGS_PER_CONTAINER_VARY ;
    }

    /**
     * Get metric display name string.
     *
     * @return the string
     */
    public String getMetricDisplayName(){
        return  this.metricQuantity + " " +
                this.nutrientMetricUom.getUomDisplayName().trim();
    }

    /**
     * Get imperial display name string.
     *
     * @return the string
     */
    public String getImperialDisplayName(){
        return  this.measureQuantity + " " +
                this.nutrientImperialUom.getUomDisplayName().trim();
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "NutrientStatementHeader{" +
                " measureQuantity=" + measureQuantity +
                ", metricQuantity=" + metricQuantity +
                ", servingsPerContainer=" + servingsPerContainer +
                ", effectiveDate=" + effectiveDate +
                ", statementMaintenanceSwitch=" + statementMaintenanceSwitch +
                '}';
    }
}
