package com.teamteskboard.domain.task.service;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.task.dto.request.CreateTaskRequest;
import com.teamteskboard.domain.task.dto.request.UpdateTaskRequest;
import com.teamteskboard.domain.task.dto.response.AssigneeResponse;
import com.teamteskboard.domain.task.dto.response.CreateTaskResponse;
import com.teamteskboard.domain.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.domain.user.enums.UserRoleEnum;
import com.teamteskboard.domain.user.repository.UserRepository;
import org.assertj.core.api.CollectionAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User("name", "username", "asdf@as.df", "asdf1234", UserRoleEnum.USER);
        ReflectionTestUtils.setField(testUser, "id", 1L);

        testTask = new Task("제목", "설명", TaskPriorityEnum.HIGH, testUser, LocalDateTime.parse("2024-01-31T00:00:00"));
        ReflectionTestUtils.setField(testTask, "id", 1L);
    }

    @Test
    @DisplayName("task 생성 성공")
    void task_save_success() {
        //given
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        LocalDateTime dueDate = LocalDateTime.parse("2024-01-31T00:00:00");

        CreateTaskRequest request = new CreateTaskRequest("제목", "설명", TaskPriorityEnum.HIGH, 1L, dueDate);

        // when
        CreateTaskResponse response = taskService.saveTask(request);

        // then
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getDescription()).isEqualTo("설명");
        assertThat(response.getDueDate()).isEqualTo(LocalDateTime.parse("2024-01-31T00:00:00"));
        assertEquals(1L, response.getAssigneeId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("task 수정 성공")
    void task_update_success() {
        //given
        SecurityUser user = new SecurityUser(1L, "username", "asdf1234", Collections.emptyList());
        User testUser2 = new User("name2", "username2", "asdf2@as.df", "asdf1234", UserRoleEnum.USER);
        ReflectionTestUtils.setField(testUser2, "id", 2L);

        when(taskRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testTask));
        when(userRepository.findByIdAndIsDeletedFalse(2L)).thenReturn(Optional.of(testUser2));
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(testTask);

        LocalDateTime updateDueDate = LocalDateTime.parse("2025-01-31T00:00:00");

        UpdateTaskRequest request = new UpdateTaskRequest("수정된 제목", "수정된 설명", TaskPriorityEnum.LOW, testUser2.getId(), updateDueDate);

        //when
        UpdateTaskResponse response = taskService.updateTask(request, 1L, user.getId());

        //then
        assertThat(response.getTitle()).isEqualTo("수정된 제목");
        assertThat(response.getAssignee().getId()).isEqualTo(2L);
        verify(taskRepository, times(1)).saveAndFlush(any(Task.class));
    }
}