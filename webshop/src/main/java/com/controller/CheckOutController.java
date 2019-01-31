package com.controller;

import com.dao.BillDetailDao;
import com.entity.Bill;
import com.entity.Billdetail;
import com.entity.User;
import com.model.Cart;
import com.model.Item;
import com.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
public class CheckOutController {
	@Autowired
	BillService billService;

	@Autowired
	BillDetailDao billDetailDao;

	/*
	 * function check out from cart
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkOut(HttpSession session, @RequestParam("payment") String payment,
			@RequestParam("address_payment") String address_payment, @RequestParam("phone") int phone) {
		Cart cart = (Cart) session.getAttribute("cart");
		System.out.println("Run into checkout ");
		User user = (User) session.getAttribute("user");
		System.out.println(user.getUserId());
		System.out.println(user.getUserName());
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
			bill.setPhone(phone);
			bill.setAddress_payment(address_payment);
			// bill.setState("not yet");
			billService.insertBill(bill);
			for (Map.Entry<String, Item> list : cart.getCartItems().entrySet()) {
				billDetailDao.insertBill(new Billdetail(bill, list.getValue().getProduct(),
						list.getValue().getProduct().getProductPrice(), list.getValue().getQuanlity()));
			}
			session.removeAttribute("cart");
			String checkOutOk = "successful";
			session.setAttribute("checkOutOk", checkOutOk);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/contact";
	}

}
