package com.teamteskboard.common.aspect;

import com.teamteskboard.activity.entity.Activity;
import com.teamteskboard.activity.repository.ActivityRepository;
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

    @Pointcut("execution(* com.teamteskboard.task.service.TaskService.saveTask(..)) || " +
            "execution(* com.teamteskboard.task.service.TaskService.updateTask(..)) || " +
            "execution(* com.teamteskboard.task.service.TaskService.updateTaskStatus(..))")
    public void taskServiceMethods() {}

    @Pointcut("execution(* com.teamteskboard.task.service.TaskService.deleteTask(..))")
    public void deleteTaskMethods() {}

    @Pointcut("execution(* com.teamteskboard.comment.service..*(..))")
    public void commentServiceMethods() {}

    @AfterReturning(pointcut = "taskServiceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result){
        Long taskId = extractTaskId(result); // 작업 Id
        Long userId = extractUserId(result); // 사용자 Id
        String methodName = joinPoint.getSignature().getName(); // 메서드 이름
        save(taskId, userId, methodName);
    }

    @AfterReturning(pointcut = "deleteTaskMethods()")
    public void logAfter(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Long taskId = (Long) args[0];
        Long userId = (Long) args[1];
        String methodName = joinPoint.getSignature().getName();
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
        }
        if (result instanceof UpdateTaskResponse dto) {
            return dto.getId();
        }
        return null;
    }

    private static Long extractUserId(Object result) {
        if (result instanceof CreateTaskResponse dto) {
            return dto.getAssigneeId();
        }
        if (result instanceof UpdateTaskResponse dto) {
            return dto.getAssigneeId();
        }
        return null;
    }

}
