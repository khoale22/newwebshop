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
// ,method = RequestMethod.POST
	@RequestMapping(value="/checkout")
	public String checkOut(HttpSession session ) {
//		if(session.getAttribute("user")==null) {
//			return "redirect:/login";
//		}else {
			
			Cart cart = (Cart) session.getAttribute("cart");
		System.out.println("Run into checkout 432ssnew");
			User user = (User) session.getAttribute("user");
			
			System.out.println(user.getUserId());
			System.out.println(user.getUserName());
		    
			 Bill bill = new Bill();
			    bill.setBillId("fdfsff231324");
			    
			    bill.setPayment("card");
			    bill.setUser(user);
			    bill.setDate(new Timestamp(new Date().getTime())); 
			    bill.setTotal(cart.totalCart());
			    billService.insertBill(bill);	
	      
		
		//User user = new User("user10", "role2", "fdsf", "khoa@fhjska.com", "123");
			//billService.insertBill(new Bill("54dff", user, "cart", new Timestamp(new Date().getTime()), 344)) ;
			//System.out.println("Run into here2223new2"); 
//	billService.insertBill(new Bill("322q","card")); 
//			System.out.println("Run into here");
//			
//			 char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
//			 StringBuilder sb = new StringBuilder(30);
//			 Random random = new Random();
//			 for (int i = 0; i < 30; i++) {
//			     char c = chars[random.nextInt(chars.length)];
//			     sb.append(c);
//			 }
//			 String billId = sb.toString();
//			 String billDetailId =sb.toString(); 
//			
//			try {
//			   // long ID = new Date().getTime();
//			    Bill bill = new Bill();
//			    bill.setBillId(billId);
//			    
//			    bill.setPayment(payment);
//			    bill.setUser(user);
//			    bill.setDate(new Timestamp(new Date().getTime())); 
//			    bill.setTotal(cart.totalCart());
//			    billService.insertBill(bill);		  
//			   
//			    for (Map.Entry<String, Item> list : cart.getCartItems().entrySet()) {
//			    	billDetailDao.insertBill(new Billdetail(billDetailId, bill, list.getValue().getProduct(),
//			    			list.getValue().getProduct().getProductPrice(), list.getValue().getQuanlity()));
//			    } 			
//			    
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
	//	}		
		return null;
	}

}
