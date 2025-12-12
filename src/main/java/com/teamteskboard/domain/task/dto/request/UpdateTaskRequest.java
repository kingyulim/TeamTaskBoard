package com.teamteskboard.domain.task.dto.request;

import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    private TaskPriorityEnum priority;

    @NotNull
    private Long assigneeId;

    private LocalDateTime dueDate;
}
