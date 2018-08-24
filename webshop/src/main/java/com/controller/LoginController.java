package com.controller;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.User;
import com.service.LoginService;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "authenticate", method = RequestMethod.POST)
	public String loginForm(@RequestParam("userId") String userId, @RequestParam("pass") String pass, HttpSession session ,ModelMap m2) {
		User user = loginService.loginUser(userId, pass);
		if (user == null) {
			String error = "Your pass or userid are not valid ";
			session.setAttribute("error",  error);
			return "redirect:/login";	
		}else {
			if (user.getRole().getRoleName().equals("user")) {
				System.out.println(user.getRole().getRoleName());				
				session.setAttribute("userId", userId);
				session.setAttribute("user", user);
				return "redirect:/";

			}
			else if(user.getRole().getRoleName().equals("admin")) {		
				System.out.println(user.getRole().getRoleName());
				session.setAttribute("userId", userId);
				session.setAttribute("user", user);
				return null;
			}
		}
      return "error";
	}
}
