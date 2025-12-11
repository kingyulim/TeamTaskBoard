package com.teamteskboard.dashboard.dto.response;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.enums.TaskPriorityEnum;
import com.teamteskboard.task.enums.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyDashboardResponse {

    private final List<TaskSummary> todayTasks;
    private final List<TaskSummary> upcommingTasks;
    private final List<TaskSummary> overdueTasks;

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {
        private final Long id;
        private final String title;
        private final TaskStatusEnum status;
        private final TaskPriorityEnum priority;
        private final LocalDateTime dueDate;

        public static TaskSummary from (Task task) {
            return new TaskSummary(
                    task.getId(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getPriority(),
                    task.getDueDate()
            );
        }

    }

    // 정적메서드 추가
    public static GetMyDashboardResponse from(
            List<Task> today,
            List<Task> upcoming,
            List<Task> overdue
    ) {
        return new GetMyDashboardResponse(
                today.stream().map(TaskSummary::from).toList(),
                upcoming.stream().map(TaskSummary::from).toList(),
                overdue.stream().map(TaskSummary::from).toList()
        );
    }
}
