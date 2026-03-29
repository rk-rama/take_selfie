package com.take_Photo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "user-dash";
    }

    @GetMapping("/admin-dash")
    public String showAdminDashboard() {
        return "admin-dash";
    }
}