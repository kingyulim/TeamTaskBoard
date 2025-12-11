package com.teamteskboard.task.dto.request;

import com.teamteskboard.task.enums.TaskPriorityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    private TaskPriorityEnum priority;

    @NotNull
    private Long assigneeId;

    private LocalDateTime dueDate;

}
