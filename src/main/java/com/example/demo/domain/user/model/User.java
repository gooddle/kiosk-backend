package com.example.demo.domain.user.model;

import com.example.demo.domain.user.common.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name = "role")
    private String role = Role.USER.name();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
