package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Product;
import com.service.ProductService;

@Controller
public class ProductDetailController {

	@Autowired
	ProductService productService;

	/*
	 * function return to product Detail from specific product
	 */
//	@RequestMapping(value="/productDetail" , method=RequestMethod.GET)
	public String productDetail(@RequestParam("productId") String productId, ModelMap mm) {
		Product product = productService.getProductById(productId);
		mm.addAttribute("product", product);
		return "productDetail";
	}

}
