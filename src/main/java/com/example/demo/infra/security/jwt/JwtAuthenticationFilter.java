package com.example.demo.infra.security.jwt;

import com.example.demo.infra.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Optional;
import java.util.Set;
import static java.util.Arrays.stream;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtPlugin jwtPlugin;

    public JwtAuthenticationFilter(JwtPlugin jwtPlugin) {
        this.jwtPlugin = jwtPlugin;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String jwt = getJwtFromCookie(request);

        if (jwt != null) {
            Optional<Jws<Claims>> jwtClaims = jwtPlugin.validateToken(jwt);
            if (jwtClaims.isPresent()) {
                Jws<Claims> result = jwtClaims.get();
                Long userId = Long.parseLong(result.getBody().getSubject());
                String role = result.getBody().get("role", String.class);
                String username = result.getBody().get("username", String.class);

                UserPrincipal principal = new UserPrincipal(
                        userId,
                        username,
                        Set.of(role)
                );

                JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                        principal,
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName())) // 쿠키 이름이 accessToken인 것 찾기
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
