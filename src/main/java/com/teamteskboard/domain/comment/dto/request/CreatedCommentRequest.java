package com.teamteskboard.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CreatedCommentRequest {

    private String content;
    private Long parentId;
}
