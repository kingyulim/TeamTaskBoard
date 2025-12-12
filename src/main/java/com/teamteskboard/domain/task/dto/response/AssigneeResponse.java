package com.teamteskboard.domain.task.dto.response;

import com.teamteskboard.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AssigneeResponse {
    private Long id;
    private String username;
    private String name;

    public static AssigneeResponse from(User user) {
        return new AssigneeResponse(
                user.getId(),
                user.getUserName(),
                user.getName() // name 컬럼 추가 후 수정 예정
        );
    }
}
