package com.iblog.springboot.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iblog.springboot.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findByUsernameAndPassword(String username, String password);
}
