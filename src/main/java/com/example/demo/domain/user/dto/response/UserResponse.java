package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.model.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String email,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
