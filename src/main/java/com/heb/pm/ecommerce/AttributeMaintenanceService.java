/*
 * AttributeMaintenanceService.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */package com.heb.pm.ecommerce;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Holds business logic related to Attribute Maintenance.
 *
 * @author a786878
 * @since 2.15.0
 */
@Service
public class AttributeMaintenanceService {

    public static final String ATTRIBUTE_ADD = "A";
    public static final String ATTRIBUTE_UPDATE = "U";

    private long PRODUCT_ENTITY_ID=16L;

    @Autowired
    EntityAttributeCodeRepository entityAttributeCodeRepository;

    @Autowired
    EntityAttributeRepository entityAttributeRepository;

    @Autowired
    AttributeRepository attributeRepository;

    @Autowired
    AttributeDomainRepository attributeDomainRepository;

    @Autowired
    private CodeTableManagementServiceClient serviceClient;

    @Autowired
    GenericEntityDescriptionRespository genericEntityDescriptionRespository;

    @Autowired
    GenericEntityRepository genericEntityRepository;

    /**
     * Returns a list of Dynamic Attributes
     * @return list of attributes
     * @param pageable
     */
    public Page<Attribute> getAttributeMaintenanceTable(Pageable pageable, String attributeName){
        Page<Attribute> dynamicAttributes;

        if (attributeName == null || attributeName.length() == 0) {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable);
        } else {
            dynamicAttributes = this.attributeRepository.findDynamicAttributes(pageable, "%" + attributeName + "%");
        }

        dynamicAttributes.forEach(attr -> {
            getEntityDescriptions(attr);

            attr.isSpecification();
            attr.isTag();
        });

        return dynamicAttributes;
    }

    /**
     * Set the entity abbreviation for an attribute
     * @param attr
     */
    private void getEntityDescriptions(Attribute attr) {
        StringBuffer entityDescription = new StringBuffer();
        List<EntityAttribute> entityAttributes = entityAttributeRepository.getEntityAttributesForAttribute(attr.getAttributeId());
        for (EntityAttribute entityAttribute : entityAttributes) {
            GenericEntity entity = genericEntityRepository.findOne(entityAttribute.getKey().getEntityId());
            if (entity == null) {
                continue;
            }
            List<GenericEntityDescription> descriptions = entity.getDescriptions();
            flattenDescriptions(entityDescription, descriptions);
        }

        trimEntityDescription(attr, entityDescription);
    }

    /**
     * Trims the entity description after last append
     * @param attr
     * @param entityDescription
     */
    private void trimEntityDescription(Attribute attr, StringBuffer entityDescription) {
        int idx = entityDescription.lastIndexOf(", ");
        if (idx > 0 && idx == entityDescription.length() - 2) {
            attr.setEntityAbbreviation(entityDescription.substring(0,idx));
        } else {
            attr.setEntityAbbreviation(entityDescription.toString());
        }
    }

    /**
     * Returns a single dynamic attribute
     * @return attribute
     */
    public Attribute getAttributeDetails(Long id){
        Attribute attribute = this.attributeRepository.findOne(id);

        // need to make sure these are loaded from separate table
        attribute.isSpecification();
        attribute.isTag();

        AttributeDomain attributeDomain = this.attributeDomainRepository.findAttributeDomainByAttributeDomainCode(attribute.getAttributeDomainCode());
        attribute.setAttributeDomainDescription(attributeDomain.getAttributeDomainDescription());
        return attribute;
    }

    /**
     * This method will take an attribute id and generate a list of all
     * possible attribute codes tied to that for products
     *
     * @param attributeId the attribute identification number
     * @return list of attribute codes
     */
    public List<EntityAttributeCode> getAttributeValues(Long attributeId){
        List<EntityAttributeCode> entityAttributeCodes = this.entityAttributeCodeRepository.findByKeyAttributeId(attributeId);

        for (EntityAttributeCode entityAttributeCode : entityAttributeCodes) {
            createEntityDescription(entityAttributeCode);
        }

        return entityAttributeCodes;
    }

    /**
     * Create entity description for attribute code
     * @param entityAttributeCode
     */
    private void createEntityDescription(EntityAttributeCode entityAttributeCode) {
        StringBuffer entityDescription = new StringBuffer();

        long entityId = entityAttributeCode.getKey().getEntityId();
        GenericEntity entity = genericEntityRepository.findOne(entityId);
        if (entity == null) {
            return;
        }
        List<GenericEntityDescription> descriptions = entity.getDescriptions();
        flattenDescriptions(entityDescription, descriptions);
        int idx = entityDescription.lastIndexOf(", ");
        if (idx > 0 && idx == entityDescription.length() - 2) {
            entityAttributeCode.setEntityDescription(entityDescription.substring(0,idx));
        } else {
            entityAttributeCode.setEntityDescription(entityDescription.toString());
        }
    }

    /**
     * Combines multiple descriptions into single comma separated description
     * @param entityDescription
     * @param descriptions
     */
    private void flattenDescriptions(StringBuffer entityDescription, List<GenericEntityDescription> descriptions) {
        for (GenericEntityDescription description : descriptions) {
            entityDescription.append(description.getShortDescription());
            entityDescription.append("[");
            entityDescription.append(description.getKey().getHierarchyContext());
            entityDescription.append("]");
            entityDescription.append(", ");
        }
    }

    /**
     * Get list of attribute domains
     * @return attribute domains
     */
    public List<AttributeDomain> getAttributeDomains(){
        List<AttributeDomain> attributeDomains = this.attributeDomainRepository.findAttributeDomains();
        return attributeDomains;
    }

    /**
     * Update a single attribute
     * @param attribute to update
     */
    public void updateAttributeDetails(Attribute attribute) throws CheckedSoapException {
        if (attribute.getOperation().equals(ATTRIBUTE_ADD)) {
            attribute.setAction(Attribute.INSERT);
            attribute.setDynamicAttributeSwitch(true);
            serviceClient.writeAttribute(Arrays.asList(attribute));
        } else if (attribute.getOperation().equals(ATTRIBUTE_UPDATE)) {
            attribute.setAction(Attribute.UPDATE);
            attribute.setDynamicAttributeSwitch(true);

            serviceClient.writeAttribute(Arrays.asList(attribute));
        }

        serviceClient.writeTagAndSpecEcommerUserGroupAttributes(attribute);

        List<EntityAttribute> attrs = entityAttributeRepository.getEntityAttributesForEntityAndAttribute(PRODUCT_ENTITY_ID, attribute.getAttributeId());
        int numEntityAttributes = attrs.size();

        int numEntityAttributeCodes = entityAttributeCodeRepository.findCountForEntityandAttribute(PRODUCT_ENTITY_ID ,attribute.getAttributeId());

        serviceClient.updateAttributeValues(numEntityAttributes, numEntityAttributeCodes, attribute);
    }
}
