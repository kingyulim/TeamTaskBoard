package com.teamteskboard.domain.search.dto;

import com.teamteskboard.common.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchTaskDto {

    private Long id;
    private String title;
    private String description;
    private String status;

    public static SearchTaskDto from(Task task) {
        return new SearchTaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }
}
