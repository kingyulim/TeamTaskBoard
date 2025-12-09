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
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.teamteskboard.common.exception.ExceptionMessageEnum.NOT_FOUND_TASK;

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
}
