package com.teamteskboard.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CreatedCommentRequest {

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
    private Long parentId;
}
