package com.teamteskboard.comment.dto.response;

import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String username;
    private String name;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getName()
        );
    }
}
