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
//@RequestMapping(value = "/cart")
public class CartController {
	@Autowired
	ProductService productService;

	@RequestMapping(value = "/cart")
	public String cart() {
		return "cart";
	}

	@RequestMapping(value = "cartadd", method = RequestMethod.GET)
	public String addProductToCart(@RequestParam("command") String command, @RequestParam("productId") String productId,
			 ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		int quantity = (Integer) productService.getQuantityOfProduct(productId);
		System.out.println("quantity is fd :" + quantity);
		System.out.println("quantity is abc:" + quantity);
		if (quantity <= 0) {
			String outOfProduct = "This product is out of quantity ,please buy other";
			session.setAttribute("outOfProduct", outOfProduct);
		} else {
			try {
				Product product = productService.getProductById(productId);
				switch (command) {
				case "plus":
					int quantityNew = quantity - 1;
					productService.updateQuanlityProduct(quantityNew, productId);
					System.out.println("enter into ....");

					if (cart.getCartItems().containsKey(productId)) {

						cart.plusToCart(productId, new Item(product, cart.getCartItems().get(productId).getQuanlity()));

					} else {
						cart.plusToCart(productId, new Item(product, 1));

					}
					break;
				case "search":
				     // chua lam
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "cart";
	}

	
	@RequestMapping(value = "cartremove", method = RequestMethod.GET)
	public String addProductToC(@RequestParam("command") String command,@RequestParam("quantityOfBuy") int quantityOfBuy, @RequestParam("productId") String productId,
			 ModelMap mm, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		int quantity = (Integer) productService.getQuantityOfProduct(productId);
		int quantiyNewOfBuy = quantity + quantityOfBuy;
		productService.updateQuanlityProduct(quantiyNewOfBuy, productId);
		System.out.print("remove ok");
		cart.removeToCart(productId);
		return "cart";
	}
}
