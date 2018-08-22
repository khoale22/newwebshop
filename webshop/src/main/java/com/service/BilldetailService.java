package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.BillDao;
import com.dao.BillDetailDao;
import com.entity.Billdetail;

@Service 
public class BilldetailService {
	
	@Autowired
	BillDetailDao billDetailDao;
	
	public void insertBill(Billdetail billdetail) {
		 billDetailDao.insertBill(billdetail);
	}

}
