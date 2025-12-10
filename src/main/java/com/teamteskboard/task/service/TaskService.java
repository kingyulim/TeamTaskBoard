package com.teamteskboard.task.service;

import com.teamteskboard.common.dto.response.ApiResponse;
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
    public ApiResponse<CreateTaskResponse> saveTask(CreateTaskRequest request) {

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(NO_USER_ID));

        Task task = new Task(request.getTitle(), request.getDescription(), request.getPriority(), assignee, request.getDueDate());

        taskRepository.save(task);

        return ApiResponse.success("작업이 생성되었습니다.", CreateTaskResponse.from(task));
    }


    // 단건 조회
    @Transactional(readOnly = true)
    public ApiResponse<GetTaskResponse> getTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        return ApiResponse.success("작업 조회 성공", GetTaskResponse.fromDetail(task));
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public ApiResponse<Page<GetTaskResponse>> getAllTasks(
            int page,
            int size,
            TaskStatusEnum status,
            String search,
            Long assigneeId
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        Page<Task> tasks = taskRepository.findTasks(status, assigneeId, search,pageable);

        Page<GetTaskResponse> response = tasks.map(GetTaskResponse::from);

        return ApiResponse.success("작업 목록 조회 성공", response);

    }

    // 작업 수정
    @Transactional
    public ApiResponse<UpdateTaskResponse> updateTask(UpdateTaskRequest request, Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        // 작업의 담당자와 로그인한 사용자가 같은지 확인
        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(TASK_ACCESS_DENIED);
        }

        task.update(request);

        Task updatedTask = taskRepository.save(task);

        return ApiResponse.success("작업이 수정되었습니다.", UpdateTaskResponse.from(updatedTask));

    }
    
    // 작업 상태 수정
    @Transactional
    public ApiResponse<UpdateTaskResponse> updateTaskStatus (UpdateTaskStatusRequest request, Long taskId, Long userId) {

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

        return ApiResponse.success("작업 상태가 변경되었습니다.", UpdateTaskResponse.from(updatedTask));

    }

    //삭제(soft delete)
    @Transactional
    public ApiResponse<Void> deleteTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(TASK_ACCESS_DENIED);
        }

        task.softDelete();

        return ApiResponse.success("작업이 삭제되었습니다.", null);
    }


}
