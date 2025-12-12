package com.teamteskboard.domain.search.dto;

import com.teamteskboard.common.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchTeamDto {

    private Long id;
    private String name;
    private String description;

    public static SearchTeamDto from(Team team) {
        return new SearchTeamDto(
                team.getId(),
                team.getName(),
                team.getDescription()
        );
    }
}
