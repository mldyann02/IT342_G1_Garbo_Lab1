package com.citu.backend.controller;

import com.citu.backend.entity.User;
import com.citu.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        Optional<User> u = userRepository.findByEmail(email);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
