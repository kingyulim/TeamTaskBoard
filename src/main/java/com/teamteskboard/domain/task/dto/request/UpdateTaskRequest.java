package com.teamteskboard.domain.task.dto.request;

import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;

    private String description;

    private TaskPriorityEnum priority;

    @NotNull(message = "담당자는 필수값입니다.")
    private Long assigneeId;

    private LocalDateTime dueDate;
}
