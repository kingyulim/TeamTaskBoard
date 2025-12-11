package com.teamteskboard.comment.controller;

import com.teamteskboard.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.comment.dto.response.*;
import com.teamteskboard.comment.service.CommentService;
import com.teamteskboard.common.config.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;


    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CreatedCommentResponse>> createdComment(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody CreatedCommentRequest request

    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 작성되었습니다.", commentService.save(taskId, user.getId(), request)));
    }


    //댓글 수정
    @PutMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> commentUpdate(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 수정됐습니다.", commentService.update(user.getId(), taskId, commentId, request)));

    }

    //삭제
    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> commentDelete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal SecurityUser user
    ) {
        commentService.Delete(user.getId(), commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 삭제 되었습니다.", null));
    }


    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<Page<PageCommentResponse>>> getCommentPage(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort

    ) {

        Pageable pageable = PageRequest.of(page, size);

        if (sort.equals("newest")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        } else if (sort.equals("oldest")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "modifiedAt"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글 목록을 조회했습니다.", commentService.getCommentPage(taskId, pageable)));
    }
}








