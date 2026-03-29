package com.take_Photo.controller;

import com.take_Photo.model.MediaFile;
import com.take_Photo.model.User;
import com.take_Photo.repository.MediaRepository;
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
    private MediaRepository mediaRepo;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/user-photos/{userId}")
    public List<MediaFile> getUserPhotos(@PathVariable Long userId) {
        return mediaRepo.findByUserId(userId);
    }
}