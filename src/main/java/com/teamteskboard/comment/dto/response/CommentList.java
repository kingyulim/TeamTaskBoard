package com.teamteskboard.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentList {

    //content[] 안에 넣기
    private List<GetCommentResponse> content;
}
