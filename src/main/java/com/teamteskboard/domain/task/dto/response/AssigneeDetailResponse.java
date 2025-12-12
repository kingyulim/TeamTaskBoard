package com.teamteskboard.domain.task.dto.response;

import com.teamteskboard.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AssigneeDetailResponse {
    private Long id;
    private String username;
    private String name;
    private String email;

    public static AssigneeDetailResponse from(User user) {
        return new AssigneeDetailResponse(
                user.getId(),
                user.getUserName(),
                user.getName(),
                user.getEmail()
        );
    }
}
