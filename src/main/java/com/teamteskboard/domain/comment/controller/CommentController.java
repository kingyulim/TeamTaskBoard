package com.teamteskboard.domain.comment.controller;

import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.domain.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.domain.comment.dto.response.CreatedCommentResponse;
import com.teamteskboard.domain.comment.dto.response.PageCommentResponse;
import com.teamteskboard.domain.comment.dto.response.UpdateCommentResponse;
import com.teamteskboard.domain.comment.service.CommentService;
import com.teamteskboard.common.config.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;


    /**
     * 댓글 생성 API
     *
     * @param taskId  댓글이 속한 TaskId
     * @param request 생성 댓글 정보를 담은 응답 DTO
     * @return 생성된 댓글 정보를 담은 응답 DTO
     */
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


    /**
     * 댓글 수정 API
     *
     * @param user      인증 사용자 정보
     * @param taskId    댓글이 속한 Tesk Id
     * @param commentId 수정하려는 댓글 Id
     * @param request   수정할 댓글 내용
     * @return 수정된 댓글 정보
     */
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

    /**
     * 댓글 삭제 API
     *
     * @param commentId 삭제하려는 댓글 Id
     * @param user      인증 사용자 정보
     * @return 삭제 결과 응답
     */
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

    /**
     * 댓글 조회 API
     *
     * @param taskId 댓글이 속한 TaskId
     * @param page   페이지 시작 번호
     * @param size   페이지당 댓글 수
     * @param sort   정렬 기준 (newest: 최신순, oldest: 오래된 순)
     * @return 페이징된 댓글 목록
     */
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<Page<PageCommentResponse>>> getCommentPage(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort

    ) {
        //정렬 방법
        Pageable pageable = PageRequest.of(page, size);

        //내림차순/ 오름차순 정렬
        if (sort.equals("newest")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (sort.equals("oldest")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글 목록을 조회했습니다.", commentService.getCommentList(taskId, pageable)));
    }

}




