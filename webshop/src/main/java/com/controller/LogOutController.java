package com.controller;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogOutController {
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session) {

		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}

		if (session.getAttribute("cart") != null) {
			session.removeAttribute("cart");
		}

		return "redirect:/";
	}

}
