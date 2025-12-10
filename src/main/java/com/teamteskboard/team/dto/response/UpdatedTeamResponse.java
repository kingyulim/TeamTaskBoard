package com.teamteskboard.team.dto.response;

import com.teamteskboard.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdatedTeamResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<TeamMemberResponse> members;

    public static UpdatedTeamResponse from(Team team, List<TeamMemberResponse> members) {
        return new UpdatedTeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                members
        );
    }
}
