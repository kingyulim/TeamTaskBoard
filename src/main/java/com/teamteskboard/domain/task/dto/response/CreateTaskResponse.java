package com.teamteskboard.domain.task.dto.response;

import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateTaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskPriorityEnum priority;
    private Long assigneeId;
    private AssigneeResponse assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public static CreateTaskResponse from(Task task) {
        return new CreateTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getAssignee().getId(),
                AssigneeResponse.from(task.getAssignee()),
                task.getCreatedAt(),
                task.getModifiedAt(),
                task.getDueDate()
        );

    }

}
