package com.teamteskboard.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatedTeamRequest {

    @NotNull(message = "팀 이름은 필수입니다.")
    private String name;
    private String description;
}
