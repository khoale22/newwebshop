package com.dao;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.User;
import com.entity.Role;
@Repository
public class RegisterDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Transactional 
	public User register(String userId) {
		return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}
      
	@Transactional
	public Role getRoleInsert(String roleId) {
		return  (Role) sessionFactory.getCurrentSession().get(Role.class, roleId); 
	}

}
