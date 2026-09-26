package com.iblog.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iblog.springboot.entity.Admin;
import com.iblog.springboot.repositary.AdminRepo;



@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Transactional(readOnly = true)
    public Admin loginAdmin(String username, String password) {

    	return adminRepo.findByUsernameAndPassword(username, password);    }
}
