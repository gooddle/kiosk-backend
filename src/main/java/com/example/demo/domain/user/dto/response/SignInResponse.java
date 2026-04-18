package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.model.User;

public record SignInResponse(
        Long userId,
        String email,
        String accessToken
) {
    public static SignInResponse from(User user, String token) {
        return new SignInResponse(user.getId(), user.getEmail(), token);
    }
}
