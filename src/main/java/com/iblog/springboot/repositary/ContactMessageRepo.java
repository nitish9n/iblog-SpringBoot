package com.iblog.springboot.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iblog.springboot.entity.ContactMessage;

public interface ContactMessageRepo extends JpaRepository<ContactMessage, Integer> {

    List<ContactMessage> findByEmail(String email);

    List<ContactMessage> findByStatus(String status);
}