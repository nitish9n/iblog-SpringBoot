package com.iblog.springboot.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iblog.springboot.entity.Post;

public interface PostRepo extends JpaRepository<Post, Integer>{

	 // Approved posts
    List<Post> findByStatus(String status);

    // Approved post by ID
    Post findByIdAndStatus(int id, String status);

    // Pending posts
    List<Post> findByStatusOrderByCreatedAtDesc(String status);

    // Search approved posts
    List<Post> findByStatusAndTitleContainingIgnoreCase(
            String status, String query);

	
}
