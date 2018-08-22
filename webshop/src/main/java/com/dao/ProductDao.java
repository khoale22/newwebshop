package com.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.entity.Product;

@Repository
public class ProductDao {
	
	@Autowired
	SessionFactory sessionFactory;	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Product> pagination(int fisrtResult , int maxResult ,String categoryId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Product where category.categoryId=:categoryId");
		query.setFirstResult(fisrtResult-1);
		query.setMaxResults(maxResult);
		query.setParameter("categoryId", categoryId);
		return query.list();		
	}
	
	@Transactional
	public Object getQuantityOfProduct(String productId) {
		
		Query query = sessionFactory.getCurrentSession().createQuery("select quantity from Product where productId=:productId");
		query.setParameter("productId", productId);
		return query.uniqueResult();
						
	}
	
	@Transactional
	
	public Product getProductById(String productId) {
	     return (Product) sessionFactory.getCurrentSession().get(Product.class, productId);
		
	}
	
	@Transactional
	public void updateQuanlityProduct(int quantity , String productId) {
		Query query = sessionFactory.getCurrentSession().createQuery("update Product set quantity=:quantity where productId=:productId");
		query.setParameter("productId", productId);
		query.setParameter("quantity", quantity);
		query.executeUpdate();
		
	}
	

}
