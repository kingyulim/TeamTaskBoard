package com.teamteskboard.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatedTeamRequest {

    @NotBlank(message = "수정할 내용인 팀 이름과 설명을 입력해 주세요.")
    private String name;
    private String description;
}
