package com.take_Photo.controller;

import com.take_Photo.model.MediaFile;
import com.take_Photo.model.User;
import com.take_Photo.repository.MediaRepository; // Import zaroor karein
import com.take_Photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MediaRepository mediaRepo; // <-- Is repo ko Autowire karein

    // 1. Purani API (Users ki list ke liye) - Waisi hi rahegi
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // 2. NAYI API (Specific user ki photos ke liye) <-- YE ADD KAREIN
    @GetMapping("/user-photos/{userId}")
    public List<MediaFile> getUserPhotos(@PathVariable Long userId) {
        System.out.println("ADMIN DEBUG: Fetching photos for User ID: " + userId);
        return mediaRepo.findByUserId(userId);
        // Ensure karein ki MediaRepository mein findByUserId method bana ho
    }
}