package com.teamteskboard.domain.comment.dto.response;

import com.teamteskboard.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PageCommentResponse {

    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private UserDto user;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PageCommentResponse from(Comment comment) {
        return new PageCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserDto.from(comment.getUser()),
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
