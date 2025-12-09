package com.teamteskboard.user.controller;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.user.dto.request.CreateUserRequest;
import com.teamteskboard.user.dto.response.CreateUserResponse;
import com.teamteskboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 요청 검증
     * @param request 입력값 파라미터
     * @return ApiResponse<CreateUserResponse> json 반환
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<CreateUserResponse>> createUser(
            @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }
}
