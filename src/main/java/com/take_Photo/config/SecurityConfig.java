package com.take_Photo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

    // --- YE SECTION FIX ADMIN SET KAREGA ---
    @Bean
    public UserDetailsService userDetailsService() {
        // Yahan apna fix Email aur Password daal do
        UserDetails admin = User.withUsername("admin@vault.com")
                .password("admin123")
                .roles("ADMIN") // Spring automatic isse ROLE_ADMIN bana dega
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}