package com.diy.mentalHealthcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.diy.mentalHealthcare.model.User;
import com.diy.mentalHealthcare.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/user/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
		Optional<User> userByEmail = userService.getUserByEmail(email);
		if(userByEmail.isPresent()) {
			return new ResponseEntity<User>(userByEmail.get(), HttpStatus.OK);
		}
		return new ResponseEntity("No user exist with given email Id", HttpStatus.BAD_REQUEST);
	}
}
