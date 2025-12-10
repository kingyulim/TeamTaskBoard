package com.teamteskboard.task.dto.request;

import com.teamteskboard.task.enums.TaskPriorityEnum;
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

    @NotBlank
    private String description;

    @NotBlank
    private String status;

    @NotNull
    private TaskPriorityEnum priority;

    @NotNull
    private Long assigneeId;

    @NotNull
    private LocalDateTime dueDate;
}
