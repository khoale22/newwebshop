package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransactionTracker.class)
public abstract class TransactionTracker_ {

	public static volatile SingularAttribute<TransactionTracker, Long> icCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, Long> grpCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, String> fileNm;
	public static volatile SingularAttribute<TransactionTracker, Long> trxCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, String> trxStatCd;
	public static volatile SingularAttribute<TransactionTracker, String> source;
	public static volatile SingularAttribute<TransactionTracker, String> userRole;
	public static volatile SingularAttribute<TransactionTracker, String> userId;
	public static volatile ListAttribute<TransactionTracker, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<TransactionTracker, Long> trackingId;
	public static volatile SingularAttribute<TransactionTracker, LocalDateTime> createDate;
	public static volatile SingularAttribute<TransactionTracker, String> fileDes;

}

