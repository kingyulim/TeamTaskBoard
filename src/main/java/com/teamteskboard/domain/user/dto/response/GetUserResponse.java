package com.teamteskboard.domain.user.dto.response;

import com.teamteskboard.common.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetUserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetUserResponse(Long id, String username, String email, String name, String role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static GetUserResponse from(User user) {
        return new GetUserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
