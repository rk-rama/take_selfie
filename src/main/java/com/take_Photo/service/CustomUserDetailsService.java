package com.take_Photo.service;

import com.take_Photo.model.User;
import com.take_Photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 🚀 STEP 1: FIX MASTER ADMIN CHECK (Ye hamesha kaam karega)
        // Aap yahan apna man-pasand email aur password set kar sakte hain
        if ("admin@vault.com".equals(email)) {
            System.out.println("DEBUG: Master Admin Access Granted for: " + email);
            return org.springframework.security.core.userdetails.User.builder()
                    .username("admin@vault.com")
                    .password("admin123") // Ye aapka FIX Password hai
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        // 🚀 STEP 2: AGAR MASTER ADMIN NAHI HAI, TOH DATABASE MEIN DHOONDO
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String userRole = user.getRole();

        // Role Formatting (Spring Security compatibility ke liye)
        if (userRole != null && !userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole.toUpperCase();
        } else if (userRole == null) {
            userRole = "ROLE_USER"; // Default role agar null ho
        }

        System.out.println("DEBUG: Logging in DB User: " + email + " with role: " + userRole);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userRole))
        );
    }
}