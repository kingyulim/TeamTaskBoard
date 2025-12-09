package com.teamteskboard.user.dto.response;

import com.teamteskboard.common.enums.UserRoleEnum;
import com.teamteskboard.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateUserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String role;
    private final LocalDateTime createdAt;

    public CreateUserResponse(Long id, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}