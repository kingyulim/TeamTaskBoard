package com.teamteskboard.domain.comment.dto.response;

import com.teamteskboard.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatedCommentResponse {

    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final UserDto user;
    private final String content;
    private final Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static CreatedCommentResponse from(Comment comment) {
        return new CreatedCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserDto.from(comment.getUser()),
                comment.getContent(),
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()

        );
    }
}

