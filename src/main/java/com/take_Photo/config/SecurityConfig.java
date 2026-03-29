package com.take_Photo.config;

import com.take_Photo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/uploads/**").permitAll()
                        .requestMatchers("/admin-dash/**", "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dashboard/**", "/api/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // Ye line add karo
                        .usernameParameter("email")    // HTML ka 'name' yahan match hona chahiye
                        .passwordParameter("password") // HTML ka 'password' name
                        .successHandler((request, response, authentication) -> {
                            var roles = authentication.getAuthorities();
                            for (var role : roles) {
                                if (role.getAuthority().equals("ROLE_ADMIN")) {
                                    response.sendRedirect("/admin-dash");
                                    return;
                                }
                            }
                            response.sendRedirect("/dashboard");
                        })
                        .failureUrl("/login?error=true") // Error aane pe yahan bhejega
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}