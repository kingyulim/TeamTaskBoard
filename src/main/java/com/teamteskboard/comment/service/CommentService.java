package com.teamteskboard.comment.service;

import com.teamteskboard.comment.dto.*;
import com.teamteskboard.comment.entity.Comment;
import com.teamteskboard.comment.repository.CommentRepository;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.entity.repository.TaskRepository;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.teamteskboard.common.exception.ExceptionMessageEnum.*;

@RequiredArgsConstructor
@Service
@Transactional

public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 생성
    public CreatedCommentResponse save(Long taskId, Long userId, CreatedCommentRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TASK));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole());
        Comment comment = new Comment(user, task, request.getContent(), request.getParentId());
        Comment save = commentRepository.save(comment);

        return new CreatedCommentResponse(
                save.getCommentId(),
                save.getTask().getId(),
                save.getUser().getId(),
                userDto,
                save.getContent(),
                save.getCreatedAt(),
                save.getModifiedAt()
        );
    }

    @Transactional(readOnly = false)
    public List<GetCommentResponseDto> getAll() {

        //비활성된 데이토 조회 X 할 예정 ->findAllByIsDeleted(IsDelete.N);
        List<Comment> comments = commentRepository.findAll();

        //반환
        List<GetCommentResponseDto> dtos = new ArrayList<>();

        for (Comment comment : comments) {
            User user = comment.getUser();
            UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole());

            GetCommentResponseDto getCommentResponseDto = new GetCommentResponseDto(
                    comment.getCommentId(),
                    comment.getContent(),
                    comment.getTask().getId(),
                    comment.getUser().getId(),
                    userDto
            );
            dtos.add(getCommentResponseDto);
        }
        return dtos;
    }

    //수정
    public UpdateCommentResponse update(Long userId, Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));

        //댓글 사용자가 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(COMMENT_ACCESS_DENIED_EXCEPTION);
        }

        //수정 메소드 불러오기
        comment.commentUpdate(request.getContent());

        return new UpdateCommentResponse(
                comment.getCommentId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
