package com.diy.mentalHealthcare.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.diy.mentalHealthcare.UserAlreadyExistException;
import com.diy.mentalHealthcare.model.User;
import com.diy.mentalHealthcare.service.UserService;

@RestController
public class AuthenticationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;



	@SuppressWarnings("rawtypes")
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
		System.out.println("Inside register" + user);
		Optional<User> existingUser = userService.getUserByEmail(user.getEmail());
		System.out.println("User to be registerd - from db - " + existingUser);
		if (existingUser.isPresent()) {
			System.out.println("User already exist");
//			throw new UserAlreadyExistException("User already exist. Please login!");
			return new ResponseEntity("User already exist. Please login!", HttpStatus.BAD_REQUEST);
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userService.saveUser(user);

		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}

//	@PostMapping("/login")
//	public String login(@RequestBody User user) {
//		System.out.println("Inside login " + user);
//		User existingUser = userService.getUserByEmail(user.getEmail());
//		System.out.println("Inside login - user from db " + existingUser);
//		if (null != existingUser) {
//			return "Successfully Authenticated";
//		} else
//			return null;
//
//	}
	
//	@GetMapping("/users")
//	public List<User> getUsers() {
//		return userService.getAllUsers();
//	}

}
