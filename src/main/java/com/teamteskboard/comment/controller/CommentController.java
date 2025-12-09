package com.teamteskboard.comment.controller;

import com.teamteskboard.comment.dto.ApiResponse;
import com.teamteskboard.comment.dto.CreatedCommentRequest;
import com.teamteskboard.comment.dto.CreatedCommentResponse;
import com.teamteskboard.comment.dto.GetCommentResponseDto;
import com.teamteskboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments/{userId}")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CreatedCommentResponse> createdComment(
            @PathVariable Long taskId,
            @PathVariable Long userId,//
            @RequestBody CreatedCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.save(taskId, userId, request));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetCommentResponseDto>>> foundAll() {
        List<GetCommentResponseDto> responses = commentService.getAll();

        //timestamp 선언
        LocalDateTime timestamp = LocalDateTime.now();

        ApiResponse apiResponse = new ApiResponse<>(true, "댓글 목록을 조회했었습니다.", responses, timestamp);
        ResponseEntity<ApiResponse<List<GetCommentResponseDto>>> responseDto = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        return responseDto;
    }
}






