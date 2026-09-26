package com.iblog.springboot.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iblog.springboot.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {

    Admin findByUsernameAndPassword(String username, String password);

}
