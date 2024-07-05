package com.example.pract.service;

import com.example.pract.controller.MainController;
import com.example.pract.entity.User;
import com.example.pract.enums.Role;
import com.example.pract.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String firstName, String lastName) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User updateUser(Long userId, String username, String password, String firstName, String lastName) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(username);
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setPassword(passwordEncoder.encode(password));

        return userRepository.save(existingUser);
    }
}
