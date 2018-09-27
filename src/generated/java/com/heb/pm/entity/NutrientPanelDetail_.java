package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientPanelDetail.class)
public abstract class NutrientPanelDetail_ {

	public static volatile SingularAttribute<NutrientPanelDetail, Double> nutrientDailyValue;
	public static volatile SingularAttribute<NutrientPanelDetail, NLEA16Nutrient> nutrient;
	public static volatile SingularAttribute<NutrientPanelDetail, NutrientStatementPanelHeader> nutrientStatementPanelHeader;
	public static volatile SingularAttribute<NutrientPanelDetail, Double> nutrientQuantity;
	public static volatile SingularAttribute<NutrientPanelDetail, NutrientPanelDetailKey> key;

}

