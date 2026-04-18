package com.example.demo.domain.user.service;

import com.example.demo.domain.user.common.Role;
import com.example.demo.domain.user.dto.request.SignInRequest;
import com.example.demo.domain.user.dto.request.SignUpRequest;
import com.example.demo.domain.user.dto.response.SignInResponse;
import com.example.demo.domain.user.dto.response.SignUpResponse;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.infra.security.jwt.JwtPlugin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtPlugin jwtPlugin;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        //중복 가입 확인 (Optional 활용)
        //코틀린에서 let 함수랑 비슷함
        userRepository.findByEmail(request.getEmail())
                .ifPresent(_ -> {
                    throw new IllegalArgumentException("이미 가입된 이메일입니다: " + request.getEmail());
                });
        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER.name())
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(newUser);
        return new SignUpResponse(savedUser.getId(), "회원가입이 완료되었습니다.");
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtPlugin.generateAccessToken(
                user.getId().toString(),
                user.getRole(),
                user.getEmail()
        );
        return SignInResponse.from(user, accessToken);
    }
}
