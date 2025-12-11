package com.teamteskboard.dashboard.service;

import com.teamteskboard.dashboard.dto.response.GetDashboardStatsResponse;
import com.teamteskboard.dashboard.dto.response.GetMyDashboardResponse;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.repository.TaskRepository;
import com.teamteskboard.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public GetDashboardStatsResponse getDashboardStats(Long userId) {

        int totalTasks = taskRepository.countAll();
        int completedTasks = taskRepository.countCompleted();
        int inProgressTasks = taskRepository.countInProgress();
        int todoTasks = taskRepository.countTodo();
        int overdueTasks = taskRepository.countOverdue();

        // 내 작업 개수
        int myTotalTasks = taskRepository.countAllByUser(userId);
        // 내 완료된 작업 개수
        int myCompletedTasks = taskRepository.countCompletedByUser(userId);

        //팀 진행률(완료율)
        double teamProgress = (totalTasks == 0) ? 0 : (completedTasks*100.0 /totalTasks);
        // 내 진행률(완료율)
        double completionRate = (myTotalTasks == 0) ? 0 : (myCompletedTasks*100.0 /myTotalTasks);

        return new GetDashboardStatsResponse(
                totalTasks,
                completedTasks,
                inProgressTasks,
                todoTasks,
                overdueTasks,
                teamProgress,
                completionRate
        );
    }

    @Transactional(readOnly = true)
    public GetMyDashboardResponse getMyDashboard(Long userId) {

        // 오늘 마감인 작업
        List<Task> todayTasks = taskRepository.findTodayTasks(userId);
        // 다가오는 작업
        List<Task> upcomingTasks = taskRepository.findUpcomingTasks(userId);
        // 지연된 일
        List<Task> overdueTasks = taskRepository.findOverdueTasks(userId);

        return new GetMyDashboardResponse(
                todayTasks.stream().map(GetMyDashboardResponse.TaskSummary::from).toList(),
                upcomingTasks.stream().map(GetMyDashboardResponse.TaskSummary::from).toList(),
                overdueTasks.stream().map(GetMyDashboardResponse.TaskSummary::from).toList()
        );
    }

}
