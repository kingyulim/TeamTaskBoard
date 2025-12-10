package com.teamteskboard.task.dto.response;

import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeDetailResponse {
    private Long id;
    private String username;
    private String name;
    private String email;

    public static AssigneeDetailResponse from(User user) {
        return new AssigneeDetailResponse(
                user.getId(),
                user.getUsername(),
                "test name",// user.getName() // name 컬럼 추가 후 수정 예정
                user.getEmail()
        );
    }
}
