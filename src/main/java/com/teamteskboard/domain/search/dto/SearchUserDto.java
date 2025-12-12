package com.teamteskboard.domain.search.dto;

import com.teamteskboard.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDto {

    private Long id;
    private String name;
    private String username;

    public static SearchUserDto from(User user) {
        return new SearchUserDto(
                user.getId(),
                user.getName(),
                user.getUserName()
        );
    }
}
