package com.teamteskboard.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreatedTeamMemberRequest {

    @NotBlank(message = "추가할 멤버 ID를 입력해 주세요.")
    private Long userId;
}
