package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.request.SignInRequest;
import com.example.demo.domain.user.dto.request.SignUpRequest;
import com.example.demo.domain.user.dto.response.SignInResponse;
import com.example.demo.domain.user.dto.response.SignUpResponse;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.infra.cors.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CookieUtils cookieUtils;

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody SignInRequest request,
            HttpServletResponse response
    ) {
        SignInResponse signInResponse = userService.signIn(request);
        cookieUtils.addCookie(response, "accessToken", signInResponse.accessToken(), 3600);
        return ResponseEntity.ok(signInResponse);
    }
}
