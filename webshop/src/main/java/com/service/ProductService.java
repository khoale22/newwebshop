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
	public List<Product> CreatePagination(int fisrtResult , int maxResult ,String categoryId) {
		return productDao.CreatePagination(fisrtResult, maxResult, categoryId);
	}
	
	public Object getQuantityOfProduct(String productId) {
		return productDao.getQuantityOfProduct(productId); 
	} 
	public Product getProductById(String productId) {
		return productDao.getProductById(productId);
	}
	public void updateQuanlityProduct(int quantity , String productId) {
		productDao.updateQuanlityProduct(quantity, productId);
	}
	public Long countProduct(String categoryId) {
		return productDao.countProduct(categoryId);
	}
	public List<Product> GetListProductForIndex(int maxResult){
		return productDao.GetListProductForIndex(maxResult);
	}

}
