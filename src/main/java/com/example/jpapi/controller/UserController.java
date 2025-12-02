package com.example.jpapi.controller;

import com.example.jpapi.dto.ApiResponse;
import com.example.jpapi.dto.UserResponse;
import com.example.jpapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User Management (Admin only)")
@SecurityRequirement(name = "session")
public class UserController {
    
    private final UserService userService;
    
    private void checkAdminRole(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin only.");
        }
    }
    
    @GetMapping
    @Operation(summary = "Get All Users", description = "Mendapatkan semua profil user (Admin only)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(HttpSession session) {
        checkAdminRole(session);
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate User", description = "Menonaktifkan user kasir (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(
            @PathVariable Long id,
            HttpSession session) {
        checkAdminRole(session);
        Long adminId = (Long) session.getAttribute("userId");
        userService.deactivateUser(adminId, id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }
}
