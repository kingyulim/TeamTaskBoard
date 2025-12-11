package com.teamteskboard.comment.service;

import com.teamteskboard.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.comment.dto.response.*;
import com.teamteskboard.comment.entity.Comment;
import com.teamteskboard.comment.repository.CommentRepository;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.task.entity.Task;

import com.teamteskboard.task.repository.TaskRepository;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.teamteskboard.common.exception.ExceptionMessageEnum.*;

@RequiredArgsConstructor
@Service


public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 생성
    @Transactional
    public CreatedCommentResponse save(Long taskId, Long userId, CreatedCommentRequest request) {


        //태스크 아이디 조회
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TASK));

        //유저 아이디 조회
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        //부모 아이디 조회 및 여부
        Long parentId = request.getParentId();

        //parentId가 존재할 경우
        if (parentId != null) {
            Comment parent = commentRepository.findByIdAndIsDeletedFalse(parentId)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));

            //parentId의 TaskId가 다를 경우 존재하지 않을 경우
            if (!parent.getTask().getId().equals(taskId)) {
                throw new CustomException(COMMENT_ACCESS_DENIED_EXCEPTION);
            }
        }
        Comment comment = new Comment(user, task, request.getContent(), request.getParentId());
        Comment save = commentRepository.save(comment);

        return  CreatedCommentResponse.from(save);

    }


    //수정
    @Transactional
    public UpdateCommentResponse update(Long userId, Long taskId, Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findByIdAndTaskIdAndIsDeletedFalse(commentId, taskId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));

        //댓글 사용자가 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(COMMENT_ACCESS_DENIED_EXCEPTION);
        }

        //수정 메소드 불러오기
        comment.commentUpdate(request.getContent());

        commentRepository.save(comment);
        return UpdateCommentResponse.from(comment);
    }


    //삭제
    @Transactional
    public void Delete(Long userId, Long commentId){
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT)
                );

        //삭제 권한 - 유저 아이디가 다른 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(COMMENT_ACCESS_DENIED_EXCEPTION);
        }
        //엔티티에서 기능 사용
        comment.getIsDelete();
    }

    //페이징 조회
    @Transactional(readOnly = true)
    public Page<PageCommentResponse> getCommentPage(Long taskId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByTaskIdAndIsDeletedFalse(taskId, pageable);
        return commentPage.map(PageCommentResponse::from);
    }
}
