package com.iblog.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iblog.springboot.entity.User;
import com.iblog.springboot.repositary.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void addUser(User user) {

    	userRepo.save(user);

        System.out.println("User registered successfully: " + user);
    }

   
    public User validateUser(int id) {

        return userRepo.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {

        return userRepo.findAll();
    }
    public User loginUser(String username, String password) {

        return userRepo.findByUsernameAndPassword(username, password);
    }
}