package com.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dao.RegisterDao;
import com.entity.Role;
import com.entity.User;
import com.service.LoginService;
import com.service.ProductService;
import com.service.UserService;

@Controller
public class RegisterController {

	@Autowired
	LoginService loginService;

	@Autowired
	RegisterDao registerService;

	@Autowired
	UserService userService;

	/*
	 * function return to register
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	/*
	 * function add user from register
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(@RequestParam("user_id") String user_id, @RequestParam("user_name") String user_name,
			@RequestParam("user_email") String user_email, @RequestParam("user_pass") String user_pass,
			HttpSession session) {
		User user = registerService.register(user_id);
		if (user == null) {
			String registerOk = "Successful";
			session.setAttribute("registerOk", registerOk);
			String roleId = "role2";
			Role role = registerService.getRoleInsert(roleId);
			User userAdd = new User(user_id, role, user_name, user_email, user_pass);
			userService.insertUser(userAdd);
			return "redirect:/register";
		} else {
			String registerFail = "fail";
			session.setAttribute("registerFail", registerFail);
			return "redirect:/register";
		}
	}
}
