package com.cyberedu.service;

import com.cyberedu.user.User;
import com.cyberedu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Service
public class AdminInitializationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeAdminUser() {
        // Check if admin user already exists
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@cyberedu.com");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Default password
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setRoles(Set.of(User.UserRole.ADMIN));
            adminUser.setIsActive(true);
            
            userRepository.save(adminUser);
            System.out.println("Default admin user created: username=admin, password=admin123");
        }
    }
}
