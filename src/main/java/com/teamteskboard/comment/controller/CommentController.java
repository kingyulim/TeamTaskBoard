package com.teamteskboard.comment.controller;

import com.teamteskboard.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.comment.dto.response.*;
import com.teamteskboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     *
     * @param taskId
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/tasks/{taskId}/comments/{userId}")
    public ResponseEntity<ApiResponse<CreatedCommentResponse>> createdComment(
            @PathVariable Long taskId,
            @PathVariable Long userId,// 제거 예정
            @RequestBody CreatedCommentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 작성되었습니다.", commentService.save(taskId, userId, request)));
    }


    /**
     * 댓글 수정 기능 구현
     *
     * @param userId    //
     * @param taskId
     * @param commentId
     * @param request
     * @return
     */
    @PutMapping("/tasks/{taskId}/comments/{commentId}/{userId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> commentUpdate(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 수정됐습니다.", commentService.update(userId, taskId, commentId,request)));

    }

    /**
     * 댓글 삭제 기능
     *
     * @param commentId
     * @param userId
     * @return
     */
    //삭제
    @DeleteMapping("/tasks/{taskId}/comments/{commentId}/{userId}")
    public ResponseEntity <ApiResponse <Void>> commentDelete(
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        commentService.Delete(userId, commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글이 삭제 되었습니다.", null));
    }

    /**
     * 댓글 페이징 조회
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<Page<PageCommentResponse>>> getCommentPage(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
//            @RequestParam(defaultValue = "newest") String sort

    ) {

        Pageable pageable1 = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글 목록을 조회했습니다.", commentService.getCommentPage(taskId, pageable1)));
    }
}








