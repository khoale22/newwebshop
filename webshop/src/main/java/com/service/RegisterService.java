package com.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.RegisterDao;
import com.entity.Role;
import com.entity.User;

@Service
public class RegisterService {
	
	@Autowired
	RegisterDao registerDao;
	
	@Transactional  
	public User register(String userId) {
		return registerDao.register(userId);
	}
     
	@Transactional
	public Role getRoleInsert(String roleId) {
		return registerDao.getRoleInsert(roleId);
	}
	
}
