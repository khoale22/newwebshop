package com.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dao.BillDetailDao;
import com.entity.Bill;
import com.entity.Billdetail;
import com.entity.User;
import com.model.Cart;
import com.model.Item;
import com.service.BillService;

@Controller
public class CheckOutController {
	@Autowired
	BillService billService;

	@Autowired
	BillDetailDao billDetailDao;

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkOut(HttpSession session, @RequestParam("payment") String payment ) {
		if (session.getAttribute("user") == null) {
           // String checkout = "You must login fisrt before checking out";
           // String checkoutOk ="ok";
           // session.setAttribute("checkout", checkout);
           // session.setAttribute("checkoutOk", checkoutOk);
			System.out.println("user is null");
			return "redirect:/login";
		} else {

			Cart cart = (Cart) session.getAttribute("cart");
			System.out.println("Run into checkout ");
			User user = (User) session.getAttribute("user");

			System.out.println(user.getUserId());
			System.out.println(user.getUserName());
			System.out.println("Run into here2223new22s");

			char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
			StringBuilder sb = new StringBuilder(30);
			Random random = new Random();
			for (int i = 0; i < 30; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			
			String billId = sb.toString();
		    

			try {
				
				Bill bill = new Bill();
				bill.setBillId(billId);

				bill.setPayment(payment);
				bill.setUser(user);
				bill.setDate(new Timestamp(new Date().getTime()));
				bill.setTotal(cart.totalCart());
				billService.insertBill(bill);

				for (Map.Entry<String, Item> list : cart.getCartItems().entrySet()) {
					billDetailDao.insertBill(new Billdetail( bill, list.getValue().getProduct(),
							list.getValue().getProduct().getProductPrice(), list.getValue().getQuanlity()));
				} 

			} catch (Exception e) {
				e.printStackTrace();
			}
			//session.removeAttribute("cart");
		}
		return "redirect:/";
	}

}
