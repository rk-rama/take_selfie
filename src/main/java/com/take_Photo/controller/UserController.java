package com.take_Photo.controller;

import com.take_Photo.model.MediaFile;
import com.take_Photo.model.User;
import com.take_Photo.repository.MediaRepository;
import com.take_Photo.service.MediaService;
import com.take_Photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaRepository mediaRepo;

    @Autowired
    private UserRepository userRepo;

    // Helper method to get logged-in user
    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userRepo.findByEmail(email).orElse(null);
    }

    @GetMapping("/my-photos")
    public List<MediaFile> getUserPhotos() {
        User user = getLoggedInUser();
        if (user != null) {
            List<MediaFile> list = mediaRepo.findByUserId(user.getId());
            System.out.println("DEBUG: Found " + list.size() + " photos for " + user.getEmail());
            return list;
        }
        System.out.println("DEBUG: No User found in Security Context!");
        return new ArrayList<>();
    }


    // Photo Delete karne ke liye
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        mediaService.deletePhoto(id);
        return ResponseEntity.ok("Deleted");
    }

    // ADMIN PANEL: Saare users ki photos dekhne ke liye
    @GetMapping("/admin/all-photos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MediaFile> getAllPhotosForAdmin() {
        return mediaRepo.findAll();
    }

    @PostMapping("/upload")
    public String uploadSelfie(@RequestParam("file") MultipartFile file) {
        try {
            User user = getLoggedInUser();
            if (user != null) {
                mediaService.saveUserMedia(file, user);
                return "Success";
            }
            return "Error: Session Expired. Please Login Again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Upload Failed: " + e.getMessage();
        }
    }
}