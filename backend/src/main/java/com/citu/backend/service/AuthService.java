package com.citu.backend.service;

import com.citu.backend.dto.AuthResponse;
import com.citu.backend.dto.LoginRequest;
import com.citu.backend.dto.RegisterRequest;
import com.citu.backend.entity.User;
import com.citu.backend.repository.UserRepository;
import com.citu.backend.security.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setGender(req.getGender());
        userRepository.save(u);
        String token = jwtUtils.generateToken(u.getEmail(), u.getUserId());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req) {
        Optional<User> found = userRepository.findByEmail(req.getEmail());
        if (found.isEmpty()) throw new IllegalArgumentException("Invalid credentials");
        User u = found.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) throw new IllegalArgumentException("Invalid credentials");
        String token = jwtUtils.generateToken(u.getEmail(), u.getUserId());
        return new AuthResponse(token);
    }
}
