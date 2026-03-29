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
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String userRole = user.getRole();
        // Galti yahan hoti hai: Spring Security ko "ROLE_" prefix chahiye hota hai
        if (userRole != null && !userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole.toUpperCase();
        }

        System.out.println("DEBUG: Logging in user: " + email + " with role: " + userRole);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userRole))
        );
    }
}