package com.example.jpapi.controller;

import com.example.jpapi.dto.*;
import com.example.jpapi.service.AuthService;
import com.example.jpapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints untuk autentikasi (Login, Register, Logout)")
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login untuk Kasir dan Admin")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        UserResponse user = authService.login(request, session);
        return ResponseEntity.ok(ApiResponse.success("Login successful", user));
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register user baru (Kasir atau Admin)")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", user));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout user yang sedang login")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
    
    @GetMapping("/profile")
    @Operation(summary = "Get Profile", description = "Mendapatkan profile user yang sedang login")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
        UserResponse user = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
