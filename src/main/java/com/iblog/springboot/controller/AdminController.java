package com.iblog.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iblog.springboot.entity.Admin;
import com.iblog.springboot.entity.Post;
import com.iblog.springboot.service.AdminService;
import com.iblog.springboot.service.PostService;

import jakarta.servlet.http.HttpSession;


@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;


    // ================= ADMIN LOGIN =================

    @GetMapping("/adminLogin")
    public String showAdminLogin() {

        return "iblogadminlogin";
    }


    // ================= PROCESS ADMIN LOGIN =================

    @PostMapping("/processAdminLogin")
    public String processAdminLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Admin admin =
                adminService.loginAdmin(
                        username,
                        password);

        if (admin != null) {

            session.setAttribute(
                    "loggedInAdmin",
                    admin);

            System.out.println(
                    "Admin login successful: "
                    + admin.getUsername());

            return "redirect:/adminDashboard";
        }


        model.addAttribute(
                "error",
                "Invalid admin username or password.");

        return "iblogadminlogin";
    }


    // ================= ADMIN DASHBOARD =================

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(
            HttpSession session,
            Model model) {

        Admin admin =
                (Admin) session.getAttribute(
                        "loggedInAdmin");

        if (admin == null) {

            return "redirect:/adminLogin";
        }


        List<Post> pendingPosts =
                postService.getPendingPosts();

        model.addAttribute(
                "pendingPosts",
                pendingPosts);


        return "iblogadmindashboard";
    }


    // ================= APPROVE POST =================

    @GetMapping("/approvePost/{id}")
    public String approvePost(
            @PathVariable("id") int id,
            HttpSession session) {

        Admin admin =
                (Admin) session.getAttribute(
                        "loggedInAdmin");

        if (admin == null) {

            return "redirect:/adminLogin";
        }


        postService.approvePost(id);


        return "redirect:/adminDashboard";
    }


    // ================= REJECT POST =================

    @GetMapping("/rejectPost/{id}")
    public String rejectPost(
            @PathVariable("id") int id,
            HttpSession session) {

        Admin admin =
                (Admin) session.getAttribute(
                        "loggedInAdmin");

        if (admin == null) {

            return "redirect:/adminLogin";
        }


        postService.rejectPost(id);


        return "redirect:/adminDashboard";
    }


    // ================= ADMIN LOGOUT =================

    @GetMapping("/adminLogout")
    public String adminLogout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
