package com.teamteskboard.task.service;

import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.task.dto.request.CreateTaskRequest;
import com.teamteskboard.task.dto.request.UpdateTaskRequest;
import com.teamteskboard.task.dto.request.UpdateTaskStatusRequest;
import com.teamteskboard.task.dto.response.CreateTaskResponse;
import com.teamteskboard.task.dto.response.GetTaskResponse;
import com.teamteskboard.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.enums.TaskStatusEnum;
import com.teamteskboard.task.repository.TaskRepository;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.teamteskboard.common.exception.ExceptionMessageEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 태스크 생성
    @Transactional
    public CreateTaskResponse saveTask(CreateTaskRequest request) {

        User assignee = userRepository.findByIdAndIsDeletedFalse(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(NO_USER_ID));

        Task task = new Task(request.getTitle(), request.getDescription(), request.getPriority(), assignee, request.getDueDate());

        taskRepository.save(task);

        return CreateTaskResponse.from(task);
    }


    // 단건 조회
    @Transactional(readOnly = true)
    public GetTaskResponse getTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        return GetTaskResponse.from(task);
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public Page<GetTaskResponse> getAllTasks(
            int page,
            int size,
            TaskStatusEnum status,
            String search,
            Long assigneeId
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        Page<Task> tasks = taskRepository.findTasks(status, assigneeId, search,pageable);

        Page<GetTaskResponse> response = tasks.map(GetTaskResponse::from);

        return response;

    }

    // 작업 수정
    @Transactional
    public UpdateTaskResponse updateTask(UpdateTaskRequest request, Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        // 작업의 담당자와 로그인한 사용자가 같은지 확인
        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(TASK_ACCESS_DENIED);
        }

        User updatedAssignee = userRepository.findByIdAndIsDeletedFalse(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(NO_USER_ID));

        task.update(request, updatedAssignee);

        Task updatedTask = taskRepository.saveAndFlush(task);

        return UpdateTaskResponse.from(updatedTask);

    }
    
    // 작업 상태 수정
    @Transactional
    public UpdateTaskResponse updateTaskStatus (UpdateTaskStatusRequest request, Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(TASK_ACCESS_DENIED);
        }

        TaskStatusEnum status;
        try{
            status = TaskStatusEnum.valueOf(request.getStatus());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new CustomException(INVALID_TASK_STATUS);
        }

        task.updateStatus(status);

        Task updatedTask = taskRepository.save(task);

        return UpdateTaskResponse.from(updatedTask);
    }

    //삭제(soft delete)
    @Transactional
    public void deleteTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(TASK_ACCESS_DENIED);
        }

        task.softDelete();
    }


}
