package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.LoginDao;
import com.entity.Role;
import com.entity.User;

@Service
public class LoginService {
	@Autowired
	LoginDao loginDao;


	public User loginUser(String userId, String pass) {

		User user = loginDao.loginUser(userId);
		if (user != null) {
			Role role = user.getRole();
			if (role != null) {
				System.out.println("Role of this user :" + role.getRoleName());
			}
			if (user.getUserPass().equals(pass)) {
				return user;
			}
		}
		return null;
	}
     
}
