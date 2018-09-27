/*
 * DB2EntityManager.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.db2;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Since we have multiple entity managers, this annotation can be added to auto wired EntityManager properties
 * when the main entity manager is needed.
 *
 * @author vn40486
 * @since 2.17.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("db2Emf")
public @interface DB2EntityManager {
}
