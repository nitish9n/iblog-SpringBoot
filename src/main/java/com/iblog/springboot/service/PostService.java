package com.iblog.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iblog.springboot.entity.Post;
import com.iblog.springboot.repositary.PostRepo;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    // ================= ADD POST =================

    public void addPost(Post post) {

        post.setCreatedAt(LocalDateTime.now());

        // Every new user blog requires admin approval
        post.setStatus("PENDING");

        postRepo.save(post);
    }

    // ================= GET POST BY ID =================

    public Post getPostById(int id) {

        return postRepo.findById(id).orElse(null);
    }

    // ================= GET APPROVED POST BY ID =================

    public Post getApprovedPostById(int id) {

        return postRepo.findByIdAndStatus(id, "APPROVED");
    }

    // ================= GET ALL POSTS =================

    public List<Post> getAllPosts() {

        return postRepo.findAll();
    }

    // ================= GET APPROVED POSTS =================

    public List<Post> getApprovedPosts() {

        return postRepo.findByStatus("APPROVED");
    }

    // ================= SEARCH APPROVED POSTS =================

    public List<Post> searchApprovedPosts(String query) {

        return postRepo.findByStatusAndTitleContainingIgnoreCase(
                "APPROVED", query);
    }

    // ================= GET PENDING POSTS =================

    public List<Post> getPendingPosts() {

        return postRepo.findByStatusOrderByCreatedAtDesc("PENDING");
    }

    // ================= APPROVE POST =================

    public void approvePost(int id) {

        Post post = postRepo.findById(id).orElse(null);

        if (post != null) {
            post.setStatus("APPROVED");
            postRepo.save(post);
        }
    }

    // ================= REJECT POST =================

    public void rejectPost(int id) {

        Post post = postRepo.findById(id).orElse(null);

        if (post != null) {
            post.setStatus("REJECTED");
            postRepo.save(post);
        }
    }

    // ================= UPDATE POST =================

    public void updatePost(Post post) {

        postRepo.save(post);
    }

    // ================= DELETE POST =================

    public void deletePost(int id) {

        postRepo.deleteById(id);
    }
}