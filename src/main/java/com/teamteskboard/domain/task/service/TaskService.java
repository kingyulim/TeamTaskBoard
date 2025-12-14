package com.teamteskboard.domain.task.service;

import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.domain.task.dto.request.*;
import com.teamteskboard.domain.task.dto.response.CreateTaskResponse;
import com.teamteskboard.domain.task.dto.response.GetTaskResponse;
import com.teamteskboard.domain.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.domain.task.enums.TaskStatusEnum;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 태스크 생성
     * @param request 작업 생성 요청 DTO
     * @return 생성 응답 DTO
     */
    @Transactional
    public CreateTaskResponse saveTask(CreateTaskRequest request) {
        User assignee = userRepository.findByIdAndIsDeletedFalse(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 마감일이 null이면 7일 후로 설정
        LocalDateTime dueDate = request.getDueDate();

        if(dueDate == null) {
            dueDate = LocalDateTime.now().plusDays(7);
        }

        Task task = new Task(request.getTitle(), request.getDescription(), request.getPriority(), assignee, dueDate);

        taskRepository.save(task);

        return CreateTaskResponse.from(task);
    }


    /**
     * 단건 조회
     * @param taskId 작업 id
     * @return 작업 조회 응답
     */
    @Transactional(readOnly = true)
    public GetTaskResponse getTask(Long taskId) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));

        return GetTaskResponse.fromDetail(task);
    }

    /**
     * 전체 조회
     * @param status 작업의 상태로 검색 가능
     * @param search 검색어로 검색 가능
     * @param assigneeId 작업자id 검색
     * @param pageable 페이지, 사이즈로 검색 가능
     * @return Page<GetTaskResponse>
     */
    @Transactional(readOnly = true)
    public Page<GetTaskResponse> getAllTasks(
            TaskStatusEnum status,
            String search,
            Long assigneeId,
            Pageable pageable
    ) {
        Page<Task> tasks = taskRepository.findTasks(status, assigneeId, search, pageable);

        return tasks.map(GetTaskResponse::from);
    }


    /**
     * 작업 수정
     * @param request 수정 요청 DTO
     * @param taskId 수정할 작업 id
     * @param userId 로그인한 아이디
     * @return 수정 응답 DTO
     */
    @Transactional
    public UpdateTaskResponse updateTask(UpdateTaskRequest request, Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));

        // 작업의 담당자와 로그인한 사용자가 같은지 확인
        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(ExceptionMessageEnum.TASK_ACCESS_DENIED);
        }

        User updatedAssignee = userRepository.findByIdAndIsDeletedFalse(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        task.update(request, updatedAssignee);

        Task updatedTask = taskRepository.saveAndFlush(task);

        return UpdateTaskResponse.from(updatedTask);

    }

    /**
     * 작업 상태 수정
     * @param request 상태 수정 DTO
     * @param taskId 수정할 작업 id
     * @param userId 로그인 id
     * @return 수정 응답 DTO
     */
    @Transactional
    public UpdateTaskResponse updateTaskStatus (UpdateTaskStatusRequest request, Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));

        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(ExceptionMessageEnum.TASK_ACCESS_DENIED);
        }

        TaskStatusEnum status;
        try{
            status = TaskStatusEnum.valueOf(request.getStatus());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new CustomException(ExceptionMessageEnum.INVALID_TASK_STATUS);
        }

        task.updateStatus(status);

        Task updatedTask = taskRepository.save(task);

        return UpdateTaskResponse.from(updatedTask);
    }

    /**
     * 삭제(soft delete)
     * @param taskId 삭제할 작업 id
     * @param userId 로그인 id
     */
    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_TASK));

        if(!userId.equals(task.getAssignee().getId())) {
            throw new CustomException(ExceptionMessageEnum.TASK_ACCESS_DENIED);
        }

        task.softDelete();
    }
}
