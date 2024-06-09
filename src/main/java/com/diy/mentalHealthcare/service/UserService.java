package com.diy.mentalHealthcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diy.mentalHealthcare.model.User;
import com.diy.mentalHealthcare.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public User saveUser(User user) {
		return userRepo.save(user);
	}

}
