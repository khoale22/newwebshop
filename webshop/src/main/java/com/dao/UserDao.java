package com.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.User;

@Repository
public class UserDao {
	@Autowired
	SessionFactory sessionFactory;
	
	
	
	@Transactional
	public void insertUser(User user) {
		sessionFactory.getCurrentSession().save(user);	
	}

}
