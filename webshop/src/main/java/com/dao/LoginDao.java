package com.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.User;

@Repository
public class LoginDao {
	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	public User loginUser(String userId) {
		return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}

}
