package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ProductDao;
import com.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	ProductDao productDao ;
	public List<Product> pagination(int fisrtResult , int maxResult ,String categoryId) {
		return productDao.pagination(fisrtResult, maxResult, categoryId);
	}
	
	public Object getQuantityOfProduct(String productId) {
		return productDao.getQuantityOfProduct(productId); 
	} 
	public Product getProductById(String productId) {
		return productDao.getProductById(productId);
	}

}
