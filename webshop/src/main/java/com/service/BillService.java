package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.BillDao;
import com.entity.Bill;

@Service
public class BillService {
	
	@Autowired
	BillDao billDao;
	
	public Bill insertBill(Bill bill) { 
		return billDao.insertBill(bill);
	}
	

}
