package com.teamteskboard.task.controller;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.task.dto.request.CreateTaskRequest;
import com.teamteskboard.task.dto.request.UpdateTaskRequest;
import com.teamteskboard.task.dto.request.UpdateTaskStatusRequest;
import com.teamteskboard.task.dto.response.CreateTaskResponse;
import com.teamteskboard.task.dto.response.GetTaskResponse;
import com.teamteskboard.task.dto.response.UpdateTaskResponse;
import com.teamteskboard.task.enums.TaskStatusEnum;
import com.teamteskboard.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    // 생성
    @PostMapping("/api/tasks")
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request ) {

        CreateTaskResponse response = taskService.saveTask(request);
        ApiResponse<CreateTaskResponse> result =
                ApiResponse.success("작업이 생성되었습니다.", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 단건 조회
    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<GetTaskResponse>> getTask(@PathVariable Long id) {

        GetTaskResponse response = taskService.getTask(id);
        ApiResponse<GetTaskResponse> result =
                ApiResponse.success("작업 조회 성공", response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 전체 조회
    @GetMapping("/api/tasks")
    public ResponseEntity<ApiResponse<Page<GetTaskResponse>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long assigneeId
    ) {

        Page<GetTaskResponse> response =
                taskService.getAllTasks(page, size, status, search, assigneeId);

        ApiResponse<Page<GetTaskResponse>> result =
                ApiResponse.success("작업 목록 조회 성공", response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 수정
    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTask(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskRequest request,
            @PathVariable Long id) {

        UpdateTaskResponse response =
                taskService.updateTask(request, id, user.getId());

        ApiResponse<UpdateTaskResponse> result =
                ApiResponse.success("작업이 수정되었습니다.", response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 상태 수정
    @PatchMapping("/api/tasks/{id}/status")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTaskStatus(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            @PathVariable Long id) {

        UpdateTaskResponse response =
                taskService.updateTaskStatus(request, id, user.getId());

        ApiResponse<UpdateTaskResponse> result =
                ApiResponse.success("작업 상태가 변경되었습니다.", response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 삭제
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id) {

        ApiResponse<Void> result =
                ApiResponse.success("작업이 삭제되었습니다.", null);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
