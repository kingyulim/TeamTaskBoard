package com.teamteskboard.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teamteskboard.common.entity.Comment;
import com.teamteskboard.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class GetCommentResponse {


    //댓글 조회 -> 대댓글 아님!
    private final Long id;

    private final String content;
    private final Long taskId;
    private final Long userId;
    private final UserDto5 user;

    // parentId가 null이면 화면에 안나옴
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;


    public static GetCommentResponse from(Comment comment) {
        User user = comment.getUser();
        UserDto5 userDto5 = new UserDto5(
                user.getId(),
                user.getUserName(),
                user.getName(),
                user.getEmail(),
                user.getRole().getRole());

        return new GetCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                userDto5,
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
