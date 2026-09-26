package com.iblog.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iblog.springboot.entity.User;
import com.iblog.springboot.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userservice;


    // ================= REST USER ENDPOINTS =================

    @ResponseBody
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {

        userservice.addUser(user);

        return user;
    }


    @ResponseBody
    @GetMapping("/getUser/{id}")
    public User getUser(
            @PathVariable(name = "id") int id) {

        return userservice.validateUser(id);
    }


    @ResponseBody
    @GetMapping("/getAllUser")
    public List<User> getAllUser() {

        return userservice.getAllUsers();
    }


    // ================= SIGNUP =================

    @GetMapping("/signup")
    public String showSignup(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser");

        // Already logged in
        if (loggedInUser != null) {

            return "redirect:/";
        }

        return "iblogsignup";
    }


    @PostMapping("/processSignup")
    public String processSignup(
            @ModelAttribute("user") User user) {

        userservice.addUser(user);

        System.out.println(
                "New User Registered: " + user);

        return "redirect:/login";
    }


    // ================= LOGIN =================

    @GetMapping("/login")
    public String showLogin(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser");

        // Already logged in
        if (loggedInUser != null) {

            return "redirect:/";
        }

        return "ibloglogin";
    }


    @PostMapping("/processLogin")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        User user =
                userservice.loginUser(
                        username,
                        password);

        if (user != null) {

            // Store logged-in user in session
            session.setAttribute(
                    "loggedInUser",
                    user);

            System.out.println(
                    "Login successful: "
                    + user.getUsername());

            return "redirect:/";
        }


        model.addAttribute(
                "error",
                "Invalid username or password");

        return "ibloglogin";
    }


    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        /*
         * Destroy the complete session.
         *
         * This removes:
         * loggedInUser
         * old session data
         * any other user-specific data
         */
        session.invalidate();

        return "redirect:/";
    }
}
