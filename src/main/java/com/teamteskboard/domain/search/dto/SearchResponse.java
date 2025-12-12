package com.teamteskboard.domain.search.dto;

import com.teamteskboard.common.entity.Task;
import com.teamteskboard.common.entity.Team;
import com.teamteskboard.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    private List<SearchTaskDto> tasks;
    private List<SearchTeamDto> teams;
    private List<SearchUserDto> users;

    public static SearchResponse from(List<Task> tasks, List<Team> teams, List<User> users) {
        return new SearchResponse(
                tasks.stream().map(SearchTaskDto::from).toList(),
                teams.stream().map(SearchTeamDto::from).toList(),
                users.stream().map(SearchUserDto::from).toList()
        );
    }
}
