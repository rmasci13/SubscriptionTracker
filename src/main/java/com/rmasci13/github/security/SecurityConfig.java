package com.rmasci13.github.security;

import com.rmasci13.github.user.User;
import com.rmasci13.github.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // Exclude API endpoints from CSRF protection
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CommandLineRunner setupDefaultUser(UserRepository userRepository) {
        return args -> {
            String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
            String encodedPassword = passwordEncoder.encode(generatedPassword);

            System.out.println("=================================================");
            System.out.println("Generated user credentials for development:");
            System.out.println("Username: admin");
            System.out.println("Password: " + generatedPassword);
            System.out.println("=================================================");

            // Create and save the user if it doesn't exist
            if (!userRepository.findByUsername("admin").isPresent()) {
                User defaultUser = new User();
                defaultUser.setUsername("admin");
                defaultUser.setPassword(encodedPassword);
                // Set appropriate roles based on your application
                defaultUser.setRoles("ADMIN"); // Adjust according to your User entity structure
                userRepository.save(defaultUser);
            }
        };
    }
}
