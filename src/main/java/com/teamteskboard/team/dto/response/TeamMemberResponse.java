package com.teamteskboard.team.dto.response;

import com.teamteskboard.team.entity.UserTeams;
import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberResponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
}
