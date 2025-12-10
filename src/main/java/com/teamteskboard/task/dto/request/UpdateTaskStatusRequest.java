package com.teamteskboard.task.dto.request;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.enums.TaskStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatusEnum status;

}
