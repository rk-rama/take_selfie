package com.take_Photo.controller;

import com.take_Photo.model.User;
import com.take_Photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword) {

        if(!password.equals(confirmPassword)) {
            return "redirect:/register?error=mismatch";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Abhi NoOp use ho raha hai toh plain text chalega
        newUser.setRole("ROLE_USER"); // Default role

        userRepo.save(newUser);
        return "redirect:/login?success=registered";
    }
}