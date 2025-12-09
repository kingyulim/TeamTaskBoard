package com.teamteskboard.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final String token;

    public static LoginResponse from(String token) {
        return new LoginResponse(token);
    }
}
