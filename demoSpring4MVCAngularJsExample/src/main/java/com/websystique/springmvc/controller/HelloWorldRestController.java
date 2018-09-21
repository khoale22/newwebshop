package com.websystique.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.service.UserService;

@RestController
public class HelloWorldRestController {

	@Autowired
	UserService userService;
	  
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
	
	/*
	/// CACH 2 : Khong Can dung @ResponseBody vi da co @RestController
   // @ResponseBody
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public List<User> listAllUsers() {
        List<User> users = userService.findAllUsers();
      
       return users; 
    } */

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public  ResponseEntity<User> updateUSer(@PathVariable("id") long id ,@RequestBody User user){

        User currentUser = userService.findById(id);

        if (currentUser==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        currentUser.setUsername(user.getUsername());
        currentUser.setAddress(user.getAddress());
        currentUser.setEmail(user.getEmail());

        userService.updateUser(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }



    @RequestMapping(value = "/user/", method = RequestMethod.POST)
     public  ResponseEntity<User> createUSer(@RequestBody User user){

            userService.saveUser(user);
        return new ResponseEntity<User>( HttpStatus.NO_CONTENT);
    }

}