package com.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.Category;
import com.entity.User;

@Repository
public class CategoryDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Category> getAllCategory() {
		return (List<Category>) sessionFactory.getCurrentSession().createQuery("from Category").list();
		
	}

}
