package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Product;
import com.model.Cart;
import com.model.Item;
import com.service.ProductService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {
	@Autowired
	ProductService productService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addProductToCart(@RequestParam("command") String command, @RequestParam("productId") String productId,
			ModelMap mm, HttpSession session) {

		Cart cart = (Cart) session.getAttribute("cart");
		int quantity = (Integer) productService.getQuantityOfProduct(productId);

		System.out.println("quantity is :" + quantity);
		if (quantity <= 0) {
			String outOfProduct = "This product is out of quantity ,please buy other";
			session.setAttribute("outOfProduct", outOfProduct);
			
		} else {


			try {

				Product product = productService.getProductById(productId);
				switch (command) {
				case "plus":

					if (cart.getCartItems().containsKey(productId)) {

						cart.plusToCart(productId, new Item(product, cart.getCartItems().get(productId).getQuanlity()));

					} else {
						cart.plusToCart(productId, new Item(product, 1));

					}
					break;
				case "remove":
					cart.removeToCart(productId);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "cart";
	}

}
