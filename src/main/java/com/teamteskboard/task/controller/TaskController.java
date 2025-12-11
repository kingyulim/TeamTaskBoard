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

    // 생성
    @PostMapping("/api/tasks")
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request ) {

        CreateTaskResponse response = taskService.saveTask(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("작업이 생성되었습니다.", response));
    }

    // 단건 조회
    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<GetTaskResponse>> getTask(@PathVariable Long id) {

        GetTaskResponse response = taskService.getTask(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("작업 조회 성공", response));
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        Page<GetTaskResponse> response =
                taskService.getAllTasks(status, search, assigneeId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("작업 목록 조회 성공", response));
    }


    // 수정
    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTask(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskRequest request,
            @PathVariable Long id) {

        UpdateTaskResponse response =
                taskService.updateTask(request, id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("작업이 수정되었습니다.", response));
    }

    // 상태 수정
    @PatchMapping("/api/tasks/{id}/status")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTaskStatus(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            @PathVariable Long id) {

        UpdateTaskResponse response =
                taskService.updateTaskStatus(request, id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("작업 상태가 변경되었습니다.", response));
    }

    // 삭제
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id) {

        taskService.deleteTask(id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("작업이 삭제되었습니다.", null));
    }
}
