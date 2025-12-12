package com.teamteskboard.domain.user.dto.response;

import com.teamteskboard.common.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateUserResponse {
    private final Long id;
    private final String name;
    private final String username;
    private final String email;
    private final String role;
    private final LocalDateTime createdAt;

    public CreateUserResponse(Long id, String name, String username, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(
                user.getId(),
                user.getName(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}