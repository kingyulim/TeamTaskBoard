package com.teamteskboard.domain.comment.dto.response;

import com.teamteskboard.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentResponse {

    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final String content;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;



    public static UpdateCommentResponse from(Comment comment) {
        return new UpdateCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
