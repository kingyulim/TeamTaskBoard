package com.teamteskboard.comment.dto.response;

import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto5 {

    private Long userId;
    private String username;
    private String name;
    private String email;
    private String role;


    public static UserDto5 from(User user) {
        return new UserDto5(
                user.getId(),
                user.getUserName(),
                user.getName(),
                user.getEmail(),
                user.getRole().getRole()
        );
    }
}
