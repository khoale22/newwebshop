package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dao.CategoryDao;
import com.service.CategoryService;

@Controller
public class HomeController {
	
	@Autowired
	CategoryService categoryService;

	@RequestMapping(value = "/")
	public String home(ModelMap mm ) {
		mm.addAttribute("listCategory" , categoryService.getAllCategory());
		return "index";
	}
}
