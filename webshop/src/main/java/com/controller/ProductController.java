package com.controller;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.Cart;
import com.service.ProductService;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
	@Autowired
	ProductService productService;

	/*
	 * function return to product
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String pagation(@RequestParam("page") int page, @RequestParam("categoryId") String categoryId, ModelMap mm,
			HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		Float countProduct = productService.countProduct(categoryId).floatValue();
		countProduct = countProduct / 10;
		session.setAttribute("countProduct", countProduct);
		System.out.println("count product new :" + countProduct);
		int maxResult = 10;
		if (page == 1) {

		} else {
			page = page - 1;
			page = page * maxResult + 1;
		}

		mm.addAttribute("categoryId", categoryId);
		mm.addAttribute("page", page);
		mm.addAttribute("listProduct", productService.CreatePagination(page, maxResult, categoryId));

		return "productnew";

	}

}
