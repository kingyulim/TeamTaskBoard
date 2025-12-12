package com.teamteskboard.domain.task.dto.response;

import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import com.teamteskboard.domain.task.enums.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private TaskPriorityEnum priority;

    private Long assigneeId;
    private Object assignee;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public static GetTaskResponse from (Task task) {
        return new GetTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignee().getId(),
                AssigneeResponse.from(task.getAssignee()),
                task.getCreatedAt(),
                task.getModifiedAt(),
                task.getDueDate()
        );
    }

    public static GetTaskResponse fromDetail (Task task) {
        return new GetTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignee().getId(),
                AssigneeDetailResponse.from(task.getAssignee()),
                task.getCreatedAt(),
                task.getModifiedAt(),
                task.getDueDate()
        );
    }
}
