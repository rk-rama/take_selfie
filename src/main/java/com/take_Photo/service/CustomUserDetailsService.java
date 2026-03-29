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

        System.out.println("DEBUG: Login attempt for email: " + email);

        // 🚀 MASTER ADMIN FIX (Hardcoded)
        if ("admin@vault.com".equals(email)) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username("admin@vault.com")
                    .password("admin123")
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        // 🚀 DATABASE USER CHECK
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String userRole = user.getRole();
        if (userRole == null) userRole = "USER";

        if (!userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole.toUpperCase();
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userRole))
        );
    }
}