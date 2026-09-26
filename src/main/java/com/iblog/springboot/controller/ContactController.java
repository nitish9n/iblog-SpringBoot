package com.iblog.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iblog.springboot.entity.Admin;
import com.iblog.springboot.entity.ContactMessage;
import com.iblog.springboot.entity.User;
import com.iblog.springboot.service.ContactMessageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageService contactMessageService;


    // =========================================================
    // SHOW CONTACT PAGE
    // =========================================================

    @GetMapping("/contact")
    public String showContactPage(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        // If user is logged in, automatically use
        // their details in the contact form
        if (user != null) {

            model.addAttribute(
                    "loggedInUser",
                    user);
        }

        return "iblogcontact";
    }


    // =========================================================
    // PROCESS CONTACT FORM
    // =========================================================

    @PostMapping("/processContact")
    public String processContact(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            HttpSession session) {

        ContactMessage contactMessage =
                new ContactMessage();

        contactMessage.setName(name);
        contactMessage.setEmail(email);
        contactMessage.setSubject(subject);
        contactMessage.setMessage(message);

        // Service will set:
        // createdAt = current time
        // status = PENDING
        contactMessageService.saveMessage(
                contactMessage);

        return "redirect:/contact?success=true";
    }


    // =========================================================
    // USER'S MESSAGES
    // =========================================================

    @GetMapping("/myMessages")
    public String showMyMessages(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        // User must be logged in
        if (user == null) {

            return "redirect:/login";
        }

        List<ContactMessage> messages =
                contactMessageService
                .getMessagesByEmail(
                        user.getEmail());

        model.addAttribute(
                "messages",
                messages);

        return "iblogmymessages";
    }


    // =========================================================
    // ADMIN MESSAGES
    // =========================================================

    @GetMapping("/adminMessages")
    public String showAdminMessages(
            HttpSession session,
            Model model) {

        Admin admin =
                (Admin) session.getAttribute(
                        "loggedInAdmin");

        // Only admin can access messages
        if (admin == null) {

            return "redirect:/adminLogin";
        }

        List<ContactMessage> messages =
                contactMessageService
                .getAllMessages();

        model.addAttribute(
                "messages",
                messages);

        return "iblogadminmessages";
    }


    // =========================================================
    // ADMIN REPLY
    // =========================================================

    @PostMapping("/replyMessage")
    public String replyMessage(
            @RequestParam("id") int id,
            @RequestParam("reply") String reply,
            HttpSession session) {

        Admin admin =
                (Admin) session.getAttribute(
                        "loggedInAdmin");

        // Only admin can reply
        if (admin == null) {

            return "redirect:/adminLogin";
        }

        contactMessageService.replyToMessage(
                id,
                reply);

        return "redirect:/adminMessages";
    }
}