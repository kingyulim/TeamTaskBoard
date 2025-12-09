package com.teamteskboard.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordResponse {
    private final boolean valid;

    public static PasswordResponse from(boolean valid) {
        return new PasswordResponse(valid);
    }
}
