package com.teamteskboard.comment.dto.request;

import lombok.Getter;

@Getter
public class CreatedCommentRequest {
    private String content;
    private Long parentId;
}
