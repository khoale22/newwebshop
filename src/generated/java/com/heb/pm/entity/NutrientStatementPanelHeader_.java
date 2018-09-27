package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientStatementPanelHeader.class)
public abstract class NutrientStatementPanelHeader_ {

	public static volatile SingularAttribute<NutrientStatementPanelHeader, Character> statementMaintenanceSwitch;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, Long> servingsPerContainer;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, String> measureQuantity;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, Long> sourceSystemId;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, String> sourceSystemReferenceId;
	public static volatile ListAttribute<NutrientStatementPanelHeader, NutrientPanelDetail> nutrientPanelDetails;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, Long> upc;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, UnitOfMeasure> nutrientMetricUom;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, String> metricQuantity;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, UnitOfMeasure> nutrientImperialUom;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, LocalDate> effectiveDate;
	public static volatile SingularAttribute<NutrientStatementPanelHeader, Long> nutrientPanelHeaderId;

}

