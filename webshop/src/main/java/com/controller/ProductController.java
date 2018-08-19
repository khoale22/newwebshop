package com.controller;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.service.ProductService;

@Controller 
@RequestMapping(value ="/product")
public class ProductController {
	@Autowired
	ProductService productService;

	@RequestMapping(method =RequestMethod.GET)
	public String pagation(@RequestParam("page") int page , @RequestParam("categoryId") String categoryId , ModelMap mm) {
		if(page ==1) {
		  	
		}else {
			page = page -1 ;
			page = page*3+1;
		}	
		
		mm.addAttribute("categoryId" ,categoryId);
		mm.addAttribute("page" ,page);
		mm.addAttribute("listProduct" ,productService.pagination(page, 3, categoryId));
		
		return "product";
		
	}
	
}
