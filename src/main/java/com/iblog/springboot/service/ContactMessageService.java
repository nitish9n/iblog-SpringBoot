package com.iblog.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iblog.springboot.entity.ContactMessage;
import com.iblog.springboot.repositary.ContactMessageRepo;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepo contactMessageRepo;

    // =========================================================
    // SAVE NEW CONTACT MESSAGE
    // =========================================================

    public void saveMessage(ContactMessage contactMessage) {

        contactMessage.setCreatedAt(LocalDateTime.now());

        // New messages are waiting for admin reply
        contactMessage.setStatus("PENDING");

        contactMessageRepo.save(contactMessage);
    }

    // =========================================================
    // GET ALL MESSAGES
    // =========================================================

    public List<ContactMessage> getAllMessages() {

        return contactMessageRepo.findAll();
    }

    // =========================================================
    // GET MESSAGE BY ID
    // =========================================================

    public ContactMessage getMessageById(int id) {

        return contactMessageRepo.findById(id).orElse(null);
    }

    // =========================================================
    // GET MESSAGES BY USER EMAIL
    // =========================================================

    public List<ContactMessage> getMessagesByEmail(String email) {

        return contactMessageRepo.findByEmail(email);
    }

    // =========================================================
    // GET PENDING MESSAGES
    // =========================================================

    public List<ContactMessage> getPendingMessages() {

        return contactMessageRepo.findByStatus("PENDING");
    }

    // =========================================================
    // ADMIN REPLY
    // =========================================================

    public void replyToMessage(int id, String reply) {

        ContactMessage contactMessage =
                contactMessageRepo.findById(id).orElse(null);

        if (contactMessage != null) {

            // Save admin's reply
            contactMessage.setReply(reply);

            // Change status
            contactMessage.setStatus("REPLIED");

            // Record reply time
            contactMessage.setRepliedAt(LocalDateTime.now());

            contactMessageRepo.save(contactMessage);
        }
    }

    // =========================================================
    // UPDATE MESSAGE
    // =========================================================

    public void updateMessage(ContactMessage contactMessage) {

        contactMessageRepo.save(contactMessage);
    }
}