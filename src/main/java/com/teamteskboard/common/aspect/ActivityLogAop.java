package com.teamteskboard.common.aspect;

import com.teamteskboard.activity.entity.Activity;
import com.teamteskboard.activity.repository.ActivityRepository;
import com.teamteskboard.comment.dto.response.CreatedCommentResponse;
import com.teamteskboard.comment.dto.response.UpdateCommentResponse;
import com.teamteskboard.comment.entity.Comment;
import com.teamteskboard.comment.repository.CommentRepository;
import com.teamteskboard.common.enums.ActivityTypeEnum;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.task.dto.response.CreateTaskResponse;
import com.teamteskboard.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.repository.TaskRepository;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
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

    @Pointcut("execution(* com.teamteskboard.task.service.TaskService.saveTask(..)) || " +
            "execution(* com.teamteskboard.task.service.TaskService.updateTask(..)) || " +
            "execution(* com.teamteskboard.task.service.TaskService.updateTaskStatus(..))")
    public void taskMethods() {}

    @Pointcut("execution(* com.teamteskboard.task.service.TaskService.deleteTask(..))")
    public void taskDeleteMethods() {}

    @Pointcut("execution(* com.teamteskboard.comment.service.CommentService.save(..)) ||" +
            "execution(* com.teamteskboard.comment.service.CommentService.update(..))")
    public void commentMethods() {}

    @Pointcut("execution(* com.teamteskboard.comment.service.CommentService.Delete(..))")
    public void commentDeleteMethods() {}

    @AfterReturning(pointcut = "taskMethods() || commentMethods()", returning = "result")
    public void AfterSaveAndUpdate(JoinPoint joinPoint, Object result){
        Long taskId = extractTaskId(result); // 작업 Id
        Long userId = extractUserId(result); // 사용자 Id
        String methodName = joinPoint.getSignature().getName(); // 메서드 이름
        save(taskId, userId, methodName);
    }

    @AfterReturning(pointcut = "taskDeleteMethods() || commentDeleteMethods()")
    public void AfterDelete(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Long taskId = null;
        Long userId = null;
        String methodName = joinPoint.getSignature().getName();

        if (methodName.equals("deleteTask")) {
            taskId = (Long) args[0];
            userId = (Long) args[1];
        } else if (methodName.equals("Delete")) {
            userId = (Long) args[0];
            Comment comment = commentRepository.findById((Long) args[1])
                    .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_COMMENT));
            taskId = comment.getTask().getId();
        }
        save(taskId, userId, methodName);
    }

    private void save(Long taskId, Long userId, String methodName) {
        // 작업
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.TASK_NOT_FOUND));
        // 사용자
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NO_USER_ID));
        // 활동 타입
        ActivityTypeEnum type = ActivityTypeEnum.fromMethodName(methodName);
        log.info("활동 타입: {}", type);

        // 활동 로그
        Activity activity = new Activity(
                task,
                user,
                type,
                ""
        );
        activityRepository.save(activity); // 저장!
    }

    private static Long extractTaskId(Object result) {
        if (result instanceof CreateTaskResponse dto) {
            return dto.getId();
        } else if (result instanceof UpdateTaskResponse dto) {
            return dto.getId();
        } else if (result instanceof CreatedCommentResponse dto) {
            return dto.getTaskId();
        } else if (result instanceof UpdateCommentResponse dto) {
            return dto.getTaskId();
        }
        return null;
    }

    private static Long extractUserId(Object result) {
        if (result instanceof CreateTaskResponse dto) {
            return dto.getAssigneeId();
        } else if (result instanceof UpdateTaskResponse dto) {
            return dto.getAssigneeId();
        } else if (result instanceof CreatedCommentResponse dto) {
            return dto.getUserId();
        } else if (result instanceof UpdateCommentResponse dto) {
            return dto.getUserId();
        }
        return null;
    }

}
