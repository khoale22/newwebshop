package com.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.Bill;
import com.entity.Billdetail;

@Repository
public class BillDetailDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	public void insertBill(Billdetail billdetail) {
	  sessionFactory.getCurrentSession().save(billdetail);
	}

}
