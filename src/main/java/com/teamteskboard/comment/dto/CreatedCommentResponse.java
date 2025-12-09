package com.teamteskboard.comment.dto;

import com.teamteskboard.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class CreatedCommentResponse {


    // @추가 하기
    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final UserDto user;
    private final String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    //생성자
    public CreatedCommentResponse(Long id,
                                  Long taskId,
                                  Long userId,
                                  UserDto user,
                                  String content,
                                  LocalDateTime createdAt,
                                  LocalDateTime modifiedAt) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
