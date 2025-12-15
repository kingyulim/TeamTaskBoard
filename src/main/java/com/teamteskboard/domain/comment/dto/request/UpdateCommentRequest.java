package com.teamteskboard.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
}
