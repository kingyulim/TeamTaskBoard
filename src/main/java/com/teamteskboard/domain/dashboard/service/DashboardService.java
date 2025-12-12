package com.teamteskboard.domain.dashboard.service;

import com.teamteskboard.domain.activity.repository.ActivityRepository;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import com.teamteskboard.domain.dashboard.dto.GetDashboardStatsResponse;
import com.teamteskboard.domain.dashboard.dto.GetMyDashboardResponse;
import com.teamteskboard.domain.dashboard.dto.GetWeeklyDashboardResponse;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    /**
     * @param userId 로그인 id
     * @return DashboardStats응답 DTO
     */
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
        int teamProgress = (int)(((totalTasks == 0) ? 0 : (completedTasks*100.0 /totalTasks)) + 0.5);
        // 내 진행률(완료율)
        int completionRate = (int)(((myTotalTasks == 0) ? 0 : (myCompletedTasks*100.0 /myTotalTasks)) + 0.5);

        return GetDashboardStatsResponse.from(
                totalTasks,
                completedTasks,
                inProgressTasks,
                todoTasks,
                overdueTasks,
                teamProgress,
                completionRate
        );
    }

    /**
     * @param userId 로그인 id
     * @return My Dashboard 응답 DTO
     */
    @Transactional(readOnly = true)
    public GetMyDashboardResponse getMyDashboard(Long userId) {

        // 오늘 마감인 작업
        List<Task> todayTasks = taskRepository.findTodayTasks(userId);
        // 다가오는 작업
        List<Task> upcomingTasks = taskRepository.findUpcomingTasks(userId);
        // 지연된 일
        List<Task> overdueTasks = taskRepository.findOverdueTasks(userId);

        return GetMyDashboardResponse.from(
                todayTasks,
                upcomingTasks,
                overdueTasks
        );
    }

    /**
     * @return WeeklyDashboard 응답 DTO
     */
    @Transactional(readOnly = true)
    public List<GetWeeklyDashboardResponse> getWeeklyDashboard() {

        LocalDate now = LocalDate.now();

        List<GetWeeklyDashboardResponse> result = new ArrayList<>();

        for(int i = 6; i>=0; i--) {
            LocalDate date = now.minusDays(i);

            LocalDateTime start = date.atStartOfDay();  // 해당 날짜의 00:00:00부터
            LocalDateTime end = date.atStartOfDay().plusDays(1); // 그 다음날 00:00:00

            int tasks = activityRepository.countCreatedTasksByDate(ActivityTypeEnum.TASK_CREATED, start, end);
            int completed = activityRepository.countCompletedTasksByDate(ActivityTypeEnum.TASK_STATUS_CHANGED, start, end, "상태를 DONE으로 변경"); //description : 작업 상태를 DONE으로 변경했습니다.

            String dayName = convertDay(date.getDayOfWeek());

            result.add(GetWeeklyDashboardResponse.from(dayName, tasks, completed, date.toString()));

        }

        return result;

    }

    private String convertDay(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }
}
