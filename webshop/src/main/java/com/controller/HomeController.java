package com.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dao.CategoryDao;
import com.entity.User;
import com.model.Cart;
import com.service.CategoryService;
import com.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;

	/*
	 * return to index
	 */
	@RequestMapping(value = "/")
	public String home(ModelMap mm, HttpSession session) {
		mm.addAttribute("listCategory", categoryService.getAllCategory());
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
		}
		if (session.getAttribute("cart") != null) {
			Cart cart = (Cart) session.getAttribute("cart");
			System.out.println(cart.getCartItems());
		}
		int maxResult = 6;
		mm.addAttribute("listProductIndex", productService.GetListProductForIndex(maxResult));
		return "index";
	}
}
