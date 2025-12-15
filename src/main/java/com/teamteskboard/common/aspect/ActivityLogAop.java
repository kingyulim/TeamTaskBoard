package com.teamteskboard.common.aspect;

import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.domain.activity.repository.ActivityRepository;
import com.teamteskboard.domain.comment.dto.response.CreatedCommentResponse;
import com.teamteskboard.domain.comment.dto.response.UpdateCommentResponse;
import com.teamteskboard.common.entity.Comment;
import com.teamteskboard.domain.comment.repository.CommentRepository;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.domain.task.dto.response.CreateTaskResponse;
import com.teamteskboard.domain.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityLogAop {

    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Pointcut("execution(* com.teamteskboard.domain.task.service.TaskService.saveTask(..)) ||" +
            "execution(* com.teamteskboard.domain.task.service.TaskService.updateTask(..)) ||" +
            "execution(* com.teamteskboard.domain.comment.service.CommentService.save(..)) ||" +
            "execution(* com.teamteskboard.domain.comment.service.CommentService.update(..)) ||" +
            "execution(* com.teamteskboard.domain.task.service.TaskService.updateTaskStatus(..))")
    public void SaveAndUpdateMethods() {}

    @Pointcut("execution(* com.teamteskboard.domain.task.service.TaskService.deleteTask(..))")
    public void DeleteTaskMethods() {}

    @Pointcut("execution(* com.teamteskboard.domain.comment.service.CommentService.Delete(..))")
    public void DeleteCommentMethods() {}

    @AfterReturning(pointcut = "SaveAndUpdateMethods()", returning = "result")
    public void AfterSaveAndUpdate(JoinPoint joinPoint, Object result){
        Long taskId = extractTaskId(result); // 작업 Id
        Long userId = extractUserId(result); // 사용자 Id
        String methodName = joinPoint.getSignature().getName(); // 메서드 이름

        save(taskId, userId, methodName);
    }

    @AfterReturning(pointcut = "DeleteTaskMethods()")
    public void AfterDeleteTask(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Long taskId = (Long) args[0];
        Long userId = (Long) args[1];
        String methodName = joinPoint.getSignature().getName();

        save(taskId, userId, methodName);
    }

    @AfterReturning(pointcut = "DeleteCommentMethods()")
    public void AfterDeleteComment(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Comment comment = commentRepository.findById((Long) args[1])
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_COMMENT));
        Long taskId = comment.getTask().getId();
        Long userId = (Long) args[0];
        String methodName = joinPoint.getSignature().getName();

        save(taskId, userId, methodName);
    }

    private void save(Long taskId, Long userId, String methodName) {
        // 작업
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));
        // 사용자
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 활동 타입
        ActivityTypeEnum type = ActivityTypeEnum.fromMethodName(methodName);

        // 변경 내용
        String description = type != null ? type.apply(task.getTitle(), task.getStatus().name()) : "";

        // 활동 로그
        log.info("[{}] {}", type, description);
        Activity activity = new Activity(task, user, type, description);
        activityRepository.save(activity); // 저장!
    }

    private Long extractTaskId(Object result) {
        if (result instanceof CreateTaskResponse dto) {
            return dto.getId();
        } else if (result instanceof UpdateTaskResponse dto) {
            return dto.getId();
        } else if (result instanceof CreatedCommentResponse dto) {
            return dto.getTaskId();
        } else if (result instanceof UpdateCommentResponse dto) {
            return dto.getTaskId();
        } else {
            throw new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK);
        }
    }

    private Long extractUserId(Object result) {
        if (result instanceof CreateTaskResponse dto) {
            return dto.getAssigneeId();
        } else if (result instanceof UpdateTaskResponse dto) {
            return dto.getAssigneeId();
        } else if (result instanceof CreatedCommentResponse dto) {
            return dto.getUserId();
        } else if (result instanceof UpdateCommentResponse dto) {
            return dto.getUserId();
        } else {
            throw new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK);
        }
    }

}
