package com.teamteskboard.search.dto;

import com.teamteskboard.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamSearchDto {

    private Long id;
    private String name;
    private String description;

    public static TeamSearchDto from(Team team) {
        return new TeamSearchDto(
                team.getId(),
                team.getName(),
                team.getDescription()
        );
    }
}
