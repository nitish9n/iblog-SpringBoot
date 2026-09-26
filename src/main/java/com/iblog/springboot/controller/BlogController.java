package com.iblog.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.iblog.springboot.entity.Post;
import com.iblog.springboot.entity.User;
import com.iblog.springboot.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BlogController {

    @Autowired
    private PostService postService;


    // ================= ADD BLOG PAGE =================

    @GetMapping("/addBlog")
    public String showAddBlog(
            HttpSession session) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        return "iblogaddblog";
    }


    // ================= PROCESS NEW BLOG =================

    @PostMapping("/processBlog")
    public String processBlog(
            @ModelAttribute Post post,
            @RequestParam(
                    value = "image",
                    required = false)
            MultipartFile image,
            HttpSession session)
            throws IOException {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }


        // Set the logged-in user as author
        post.setAuthor(
                user.getUsername());


        // ================= IMAGE UPLOAD =================

        if (image != null
                && !image.isEmpty()) {

            String originalFileName =
                    image.getOriginalFilename();

            String fileExtension = "";

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                fileExtension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf("."));
            }


            String fileName =
                    System.currentTimeMillis()
                    + fileExtension;


            String uploadDirectory =
                    session.getServletContext()
                           .getRealPath(
                                   "/img/blogs");


            File directory =
                    new File(uploadDirectory);

            if (!directory.exists()) {
                directory.mkdirs();
            }


            File destination =
                    new File(
                            directory,
                            fileName);


            image.transferTo(
                    destination);


            post.setImageName(
                    fileName);
        }


        /*
         * PostService will automatically set:
         *
         * createdAt = current date/time
         * status = PENDING
         *
         * Therefore a newly created blog
         * requires admin approval.
         */

        postService.addPost(post);


        return "redirect:/";
    }


    // ================= ALL PUBLIC BLOGS =================

    @GetMapping("/blogs")
    public String showAllBlogs(
            Model model) {

        /*
         * Only APPROVED blogs are visible
         * to public users.
         */

        List<Post> posts =
                postService.getApprovedPosts();


        model.addAttribute(
                "posts",
                posts);


        return "iblogblogs";
    }


    // ================= VIEW SINGLE BLOG =================

    @GetMapping("/blogpost/{id}")
    public String showBlog(
            @PathVariable("id") int id,
            Model model) {

        /*
         * Public users can open only
         * APPROVED blogs.
         */

        Post post =
                postService.getApprovedPostById(
                        id);


        if (post == null) {
            return "redirect:/";
        }


        model.addAttribute(
                "post",
                post);


        return "iblogblogpost";
    }


    // ================= SEARCH BLOGS =================

    @GetMapping("/search")
    public String search(
            @RequestParam("query") String query,
            Model model) {

        /*
         * Search only APPROVED blogs.
         */

        List<Post> posts =
                postService.searchApprovedPosts(
                        query);


        model.addAttribute(
                "posts",
                posts);

        model.addAttribute(
                "query",
                query);


        return "iblogblogs";
    }


    // ================= UPDATE BLOG PAGE =================

    @GetMapping("/updateBlog/{id}")
    public String showUpdateBlog(
            @PathVariable("id") int id,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");


        if (user == null) {
            return "redirect:/login";
        }


        Post post =
                postService.getPostById(id);


        if (post == null) {
            return "redirect:/";
        }


        /*
         * Only the author of the blog
         * can update it.
         */

        if (!post.getAuthor()
                .equals(user.getUsername())) {

            return "redirect:/";
        }


        model.addAttribute(
                "post",
                post);


        return "iblogupdateblog";
    }


    // ================= PROCESS UPDATE BLOG =================

    @PostMapping("/processUpdateBlog")
    public String processUpdateBlog(
            @ModelAttribute Post updatedPost,
            @RequestParam(
                    value = "image",
                    required = false)
            MultipartFile image,
            HttpSession session)
            throws IOException {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");


        if (user == null) {
            return "redirect:/login";
        }


        Post existingPost =
                postService.getPostById(
                        updatedPost.getId());


        if (existingPost == null) {
            return "redirect:/";
        }


        /*
         * Only the original author
         * can update the blog.
         */

        if (!existingPost.getAuthor()
                .equals(user.getUsername())) {

            return "redirect:/";
        }


        existingPost.setTitle(
                updatedPost.getTitle());

        existingPost.setContent(
                updatedPost.getContent());


        // ================= NEW IMAGE =================

        if (image != null
                && !image.isEmpty()) {

            String originalFileName =
                    image.getOriginalFilename();

            String fileExtension = "";

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                fileExtension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf("."));
            }


            String fileName =
                    System.currentTimeMillis()
                    + fileExtension;


            String uploadDirectory =
                    session.getServletContext()
                           .getRealPath(
                                   "/img/blogs");


            File directory =
                    new File(uploadDirectory);


            if (!directory.exists()) {
                directory.mkdirs();
            }


            File destination =
                    new File(
                            directory,
                            fileName);


            image.transferTo(
                    destination);


            existingPost.setImageName(
                    fileName);
        }


        /*
         * Important:
         *
         * If an APPROVED blog is edited,
         * it should require admin approval again.
         */

        existingPost.setStatus(
                "PENDING");


        postService.updatePost(
                existingPost);


        return "redirect:/";
    }


    // ================= DELETE BLOG =================

    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(
            @PathVariable("id") int id,
            HttpSession session) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");


        if (user == null) {
            return "redirect:/login";
        }


        Post post =
                postService.getPostById(id);


        if (post == null) {
            return "redirect:/";
        }


        /*
         * Only the author can delete
         * their own blog.
         */

        if (!post.getAuthor()
                .equals(user.getUsername())) {

            return "redirect:/";
        }


        postService.deletePost(id);


        return "redirect:/";
    }
}