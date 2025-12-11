package com.teamteskboard.search.dto;

import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {

    private Long id;
    private String name;
    private String username;

    public static UserSearchDto from(User user) {
        return new UserSearchDto(
                user.getId(),
                user.getName(),
                user.getUserName()
        );
    }
}
