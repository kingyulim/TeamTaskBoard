package com.teamteskboard.search.dto;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.team.entity.Team;
import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    private List<TaskSearchDto> tasks;
    private List<TeamSearchDto> teams;
    private List<UserSearchDto> users;

    public static SearchResponse from(List<Task> tasks, List<Team> teams, List<User> users) {
        return new SearchResponse(
                tasks.stream().map(TaskSearchDto::from).toList(),
                teams.stream().map(TeamSearchDto::from).toList(),
                users.stream().map(UserSearchDto::from).toList()
        );
    }
}
