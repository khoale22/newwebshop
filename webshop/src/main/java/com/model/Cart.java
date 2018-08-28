package com.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

	private HashMap<String, Item> cartItems;

	public Cart() {

		cartItems = new HashMap<String, Item>();
	}

	public Cart(HashMap<String, Item> cartItems) {
		this.cartItems = cartItems;
	}

	public HashMap<String, Item> getCartItems() {
		return cartItems;
	}

	public void setCartItems(HashMap<String, Item> cartItems) {
		this.cartItems = cartItems;
	}

	public void plusToCart(String key, Item item) {

		boolean check = cartItems.containsKey(key);
		if (check) {

			int quanlity_old = item.getQuanlity();
			int quanlity = quanlity_old + 1;
			item.setQuanlity(quanlity);
			cartItems.put(key, item);
		} else {
			cartItems.put(key, item);
		}
	}

	public double totalCart() {
		double count = 0;
		for (Map.Entry<String, Item> list : cartItems.entrySet()) {
			count += list.getValue().getProduct().getProductPrice() * list.getValue().getQuanlity();

		}
		return count;
	}

	public void removeToCart(String key) {
		boolean check = cartItems.containsKey(key);
		if (check) {
			cartItems.remove(key);
		}
	}
	public int countEntryOfCart() {
		return cartItems.size();
		
	}
	
}
