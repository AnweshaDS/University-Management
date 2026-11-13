package com.example.config;

import com.example.model.AppUser;
import com.example.model.Role;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder,
                                @Value("${app.admin.username}") String username,
                                @Value("${app.admin.password}") String password) {
        return args -> {
            if (!users.existsByUsername(username)) {
                users.save(new AppUser(username, encoder.encode(password), Role.ADMIN));
            }
        };
    }
}
