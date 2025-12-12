package com.teamteskboard.domain.search.service;

import com.teamteskboard.domain.search.dto.SearchResponse;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.common.entity.Team;
import com.teamteskboard.domain.team.repository.TeamRepository;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public SearchResponse search(String query) {
        List<Task> tasks = taskRepository.findByKeyword(query);
        List<Team> teams = teamRepository.findByKeyword(query);
        List<User> users = userRepository.findByKeyword(query);

        return SearchResponse.from(tasks, teams, users);
    }
}
