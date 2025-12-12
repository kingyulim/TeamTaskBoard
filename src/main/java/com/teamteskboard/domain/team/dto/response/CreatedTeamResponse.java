package com.teamteskboard.domain.team.dto.response;

import com.teamteskboard.common.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreatedTeamResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<TeamMemberResponse> members;

    public static CreatedTeamResponse from(Team team, List<TeamMemberResponse> members) {
        return new CreatedTeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                members
        );
    }
}
