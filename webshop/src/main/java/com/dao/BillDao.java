package com.dao;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.Bill;
import com.entity.Category;
@Repository
public class BillDao {

	
	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	public void insertBill(Bill bill) {
		 sessionFactory.getCurrentSession().save(bill);
	}

}



