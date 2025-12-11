package com.teamteskboard.comment.dto;

import lombok.Getter;

@Getter
public class GetCommentResponseDto{

    //댓글 조회 -> 대댓글 아님!
    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final UserDto user;

    public GetCommentResponseDto(Long id,String content, Long taskId, Long userId, UserDto user ) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.user = user;
    }
}
