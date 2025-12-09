package com.teamteskboard.user.dto.request;

import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
}
