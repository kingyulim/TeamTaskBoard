package com.teamteskboard.search.service;

import com.teamteskboard.search.dto.SearchResponse;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.repository.TaskRepository;
import com.teamteskboard.team.entity.Team;
import com.teamteskboard.team.repository.TeamRepository;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
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
