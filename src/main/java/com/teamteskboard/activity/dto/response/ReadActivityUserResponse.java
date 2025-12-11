package com.teamteskboard.activity.dto.response;

import com.teamteskboard.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReadActivityUserResponse {
    private final Long id;
    private final String username;
    private final String name;

    public static ReadActivityUserResponse from(User user) {
        return new ReadActivityUserResponse(
                user.getId(),
                user.getUserName(),
                user.getName()
        );
    }
}
