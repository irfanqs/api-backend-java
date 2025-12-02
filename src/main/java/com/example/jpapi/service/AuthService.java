package com.example.jpapi.service;

import com.example.jpapi.dto.*;
import com.example.jpapi.model.User;
import com.example.jpapi.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        return UserResponse.fromUser(savedUser);
    }
    
    public UserResponse login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!user.getActive()) {
            throw new RuntimeException("User is deactivated");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // Store user in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole().name());
        
        return UserResponse.fromUser(user);
    }
    
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
