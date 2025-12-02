package com.example.jpapi.service;

import com.example.jpapi.dto.UserResponse;
import com.example.jpapi.model.User;
import com.example.jpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserResponse::fromUser)
            .collect(Collectors.toList());
    }
    
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.fromUser(user);
    }
    
    @Transactional
    public void deactivateUser(Long adminId, Long userId) {
        // Cek apakah user yang akan di-deactivate ada
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Cek apakah admin mencoba deactivate diri sendiri
        if (adminId.equals(userId)) {
            throw new RuntimeException("You cannot deactivate yourself");
        }
        
        // Cek apakah user yang akan di-deactivate adalah ADMIN
        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("You cannot deactivate another admin");
        }
        
        // Hanya KASIR yang bisa di-deactivate
        user.setActive(false);
        userRepository.save(user);
    }
}
