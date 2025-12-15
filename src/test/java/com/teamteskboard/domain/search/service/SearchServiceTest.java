package com.teamteskboard.domain.search.service;

import com.teamteskboard.domain.search.dto.SearchResponse;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.domain.team.repository.TeamRepository;
import com.teamteskboard.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void searchKeyword() {
        when(taskRepository.findByKeyword(any()))
                .thenReturn(List.of());

        when(teamRepository.findByKeyword(any()))
                .thenReturn(List.of());

        when(userRepository.findByKeyword(any()))
                .thenReturn(List.of());

        SearchResponse response = searchService.search("테스트");

        assertNotNull(response);
    }

    @Test
    void noKeyword() {
        // given
        String query = "없는검색어";
        when(taskRepository.findByKeyword(query)).thenReturn(List.of());
        when(teamRepository.findByKeyword(query)).thenReturn(List.of());
        when(userRepository.findByKeyword(query)).thenReturn(List.of());
        //when
        SearchResponse response = searchService.search(query);
        // then
        assertNotNull(response);
        assertTrue(response.getTasks().isEmpty());
        assertTrue(response.getTeams().isEmpty());
        assertTrue(response.getUsers().isEmpty());
    }
}