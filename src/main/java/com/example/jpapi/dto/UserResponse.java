package com.example.jpapi.dto;

import com.example.jpapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String role;
    private Boolean active;
    private LocalDateTime createdAt;
    
    public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getRole().name(),
            user.getActive(),
            user.getCreatedAt()
        );
    }
}
