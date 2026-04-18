package com.example.demo.infra.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record UserPrincipal(
        Long id,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
    // 커스텀 생성자 (ID, 이름, 역할 세트를 받아서 변환)
    public UserPrincipal(Long id, String username, Set<String> roles) {
        this(
                id,
                username,
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet())
        );
    }
}
