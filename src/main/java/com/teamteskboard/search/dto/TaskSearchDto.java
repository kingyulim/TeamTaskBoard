package com.teamteskboard.search.dto;

import com.teamteskboard.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchDto {

    private Long id;
    private String title;
    private String description;
    private String status;

    public static TaskSearchDto from(Task task) {
        return new TaskSearchDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }
}
