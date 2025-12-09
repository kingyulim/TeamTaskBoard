package com.teamteskboard.team.dto.response;

import com.teamteskboard.team.entity.UserTeams;
import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberResponse {

    private Long userId;
    private String username;
    private String name;

    // UserTeams 엔티티에서 User 정보를 꺼내 매핑하는 느낌
    public static TeamMemberResponse from(UserTeams userTeams) {
        User user = userTeams.getUser();

        return new TeamMemberResponse(
                user.getId(),
                user.getUsername(),
                user.getName()
        );
    }
}
