package com.teamteskboard.domain.comment.service;

import com.teamteskboard.domain.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.domain.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.domain.comment.dto.response.CreatedCommentResponse;
import com.teamteskboard.domain.comment.dto.response.PageCommentResponse;
import com.teamteskboard.domain.comment.dto.response.UpdateCommentResponse;
import com.teamteskboard.common.entity.Comment;
import com.teamteskboard.domain.comment.repository.CommentRepository;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 생성 기능
     *
     * @param taskId  댓글이 속한 TaskId
     * @param userId
     * @param request 생성 댓글 정보를 담은 응답 DTO
     * @return 생성된 댓글 정보를 담은 응답 DTO
     */
    @Transactional
    public CreatedCommentResponse save(Long taskId, Long userId, CreatedCommentRequest request) {


        //태스크 아이디 조회
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));

        //유저 아이디 조회
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        //부모 아이디 조회 및 여부
        Long parentId = request.getParentId();

        //parentId가  (존재할 경우 대댓글)
        if (parentId != null) {
            Comment parent = commentRepository.findByIdAndIsDeletedFalse(parentId)
                    .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_COMMENT));

            //parentId의 TaskId가 다를 경우 존재하지 않을 경우
            if (!parent.getTask().getId().equals(taskId)) {
                throw new CustomException(ExceptionMessageEnum.COMMENT_ACCESS_DENIED_EXCEPTION);
            }
        }
        Comment comment = new Comment(user, task, request.getContent(), request.getParentId());
        Comment save = commentRepository.save(comment);

        return CreatedCommentResponse.from(save);
    }

    /**
     * 댓글 수정 기능
     *
     * @param userId    수정 요청하는 사용자 ID
     * @param taskId    댓글이 속한 테스크 ID
     * @param commentId 수정하려는 댓글 ID
     * @param request   수정할 댓글 내용이 담긴 응답 DTO
     * @return 수정된 댓글 정보를 담은 응답 DTO
     */
    @Transactional
    public UpdateCommentResponse update(Long userId, Long taskId, Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findByIdAndTaskIdAndIsDeletedFalse(commentId, taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_COMMENT));

        //댓글 사용자가 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionMessageEnum.COMMENT_ACCESS_DENIED_EXCEPTION);
        }

        //수정 메소드 불러오기
        comment.commentUpdate(request.getContent());
        commentRepository.save(comment);
        return UpdateCommentResponse.from(comment);
    }

    /**
     * 댓글 삭제 기능
     *
     * @param userId    삭제 요청한 사용자 ID
     * @param commentId 살제할 댓글 ID
     */
    @Transactional
    public void Delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_COMMENT)
                );

        //삭제 권한 - 유저 아이디가 다른 경우
        if (!comment.getUserId().equals(userId)) {
            throw new CustomException(ExceptionMessageEnum.COMMENT_ACCESS_DENIED_EXCEPTION);
        }
        //soft delete
        comment.getIsDelete();
    }

    /**
     * 댓글 조회, 페이징
     *
     * @param taskId   댓글이 속한 TaskId
     * @param pageable 페이지 정렬 정보
     * @return 페이징된 댓글 목록
     */
    //페이징 조회
    @Transactional(readOnly = true)
    //페이징 정보 전달
    public Page<PageCommentResponse> getCommentList(Long taskId, Pageable pageable) {

        //대댓글 제외하고 찾기 (해당 Task의 댓글, 부모댓글, 삭제 안된 것)
        Page<Comment> commentPage = commentRepository.findAllByTaskIdAndParentIdIsNullAndIsDeletedFalse(taskId, pageable);

        //대댓글 찾기
        List<Comment> commentList = commentRepository.findByTaskIdAndParentIdIsNotNullAndIsDeletedFalseOrderByCreatedAtAsc(taskId);

        //현 페이지에 포함된 부모 댓글 반복
        List<PageCommentResponse> result = new ArrayList<>();
        for (Comment commentParent : commentPage.getContent()) {

            //부모 댓글을 결과에 저장
            result.add(PageCommentResponse.from(commentParent));

            //대댓글 목록 검사 -> 일치 시 result 저장
            for (Comment child : commentList) {
                if (child.getParentId().equals(commentParent.getId())) {
                    result.add(PageCommentResponse.from(child));
                }
            }
        }
        //부모 댓글 기준으로 페이징된 결과 반환
        return new PageImpl<>(result, pageable, commentPage.getTotalElements());
    }

}