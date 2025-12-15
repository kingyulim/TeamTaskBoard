package com.teamteskboard.domain.dashboard.service;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.activity.repository.ActivityRepository;
import com.teamteskboard.domain.dashboard.dto.GetDashboardStatsResponse;
import com.teamteskboard.domain.dashboard.dto.GetMyDashboardResponse;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.domain.user.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("name", "username", "asdf@as.df", "asdf1234", UserRoleEnum.USER);
        ReflectionTestUtils.setField(testUser, "id", 1L);
    }

    @Test
    @DisplayName("내 작업 요약 불러오기 성공")
    void myDashboard_success(){
        Long userId = 1L;
        LocalDateTime today = LocalDateTime.now();

        Task todayTask = new Task("오늘 할 일", "설명", TaskPriorityEnum.HIGH, testUser, today);
        Task upcomingTask = new Task("다가오는 일", "설명", TaskPriorityEnum.MEDIUM, testUser, today.plusDays(1));
        Task overdueTask = new Task("지연된 일", "설명", TaskPriorityEnum.MEDIUM, testUser, today.minusDays(1));

        List<Task> todayTasks = List.of(todayTask);
        List<Task> upcomingTasks = List.of(upcomingTask);
        List<Task> overdueTasks = List.of(overdueTask);

        when(taskRepository.findTodayTasks(userId)).thenReturn(todayTasks);
        when(taskRepository.findUpcomingTasks(userId)).thenReturn(upcomingTasks);
        when(taskRepository.findOverdueTasks(userId)).thenReturn(overdueTasks);

        //when
        GetMyDashboardResponse response = dashboardService.getMyDashboard(userId);

        //then
        assertThat(response.getTodayTasks().get(0).getTitle()).isEqualTo("오늘 할 일");
        assertThat(response.getUpcomingTasks().get(0).getTitle()).isEqualTo("다가오는 일");
        assertThat(response.getOverdueTasks().get(0).getTitle()).isEqualTo("지연된 일");

        verify(taskRepository).findTodayTasks(userId);
        verify(taskRepository).findUpcomingTasks(userId);
        verify(taskRepository).findOverdueTasks(userId);
    }

    @Test
    @DisplayName("대시보드 통계 불러오기 성공")
    void DashboardStats_success(){
        Long userId = 1L;

        when(taskRepository.countAll()).thenReturn(8);
        when(taskRepository.countCompleted()).thenReturn(3);
        when(taskRepository.countInProgress()).thenReturn(2);
        when(taskRepository.countTodo()).thenReturn(2);
        when(taskRepository.countOverdue()).thenReturn(1);

        when(taskRepository.countAllByUser(userId)).thenReturn(5);
        when(taskRepository.countCompletedByUser(userId)).thenReturn(3);

        //when
        GetDashboardStatsResponse response = dashboardService.getDashboardStats(userId);

        assertThat(response.getTeamProgress()).isEqualTo(38);
        assertThat(response.getCompletionRate()).isEqualTo(60);
    }
}