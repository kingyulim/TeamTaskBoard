package com.teamteskboard.domain.task.controller;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.task.dto.request.*;
import com.teamteskboard.domain.task.dto.response.CreateTaskResponse;
import com.teamteskboard.domain.task.dto.response.GetTaskResponse;
import com.teamteskboard.domain.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.domain.task.enums.TaskStatusEnum;
import com.teamteskboard.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    /**
     *  생성
     * @param request 생성 요청 DTO
     * @return 생성 응답 DTO
     */
    @PostMapping("/api/tasks")
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        CreateTaskResponse response = taskService.saveTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("작업이 생성되었습니다.", response));
    }

    /**
     *  단건 조회
     * @param id 조회할 task id
     * @return 조회 응답 DTO
     */
    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<GetTaskResponse>> getTask(@PathVariable Long id) {
        GetTaskResponse response = taskService.getTask(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("작업 조회 성공", response));
    }

    /**
     *  전체 조회
     * @param page 페이지
     * @param size 사이즈
     * @param status 작업 상태
     * @param search 검색어
     * @param assigneeId 담당자
     * @return 작업 응답 DTO
     */
    @GetMapping("/api/tasks")
    public ResponseEntity<ApiResponse<Page<GetTaskResponse>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long assigneeId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        Page<GetTaskResponse> response = taskService.getAllTasks(status, search, assigneeId, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("작업 목록 조회 성공", response));
    }


    /**
     *  수정
     * @param user 로그인 사용자
     * @param request 수정 요청 DTO
     * @param id 수정할 task id
     * @return 수정 응답 DTO
     */
    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTask(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskRequest request,
            @PathVariable Long id
    ) {
        UpdateTaskResponse response = taskService.updateTask(request, id, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("작업이 수정되었습니다.", response));
    }

    /**
     *  상태 수정
     * @param user 로그인 사용자
     * @param request 상태 수정 요청 DTO
     * @param id 수정할 task id
     * @return 수정 응답 DTO
     */
    @PatchMapping("/api/tasks/{id}/status")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTaskStatus(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            @PathVariable Long id
    ) {

        UpdateTaskResponse response = taskService.updateTaskStatus(request, id, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("작업 상태가 변경되었습니다.", response));
    }

    /**
     * 작업 삭제
     * @param user 로그인 사용자
     * @param id 삭제할 task id
     * @return 삭제 완료 응답
     */
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id
    ) {

        taskService.deleteTask(id, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("작업이 삭제되었습니다.", null));
    }
}
