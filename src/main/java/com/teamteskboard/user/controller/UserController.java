package com.teamteskboard.user.controller;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.user.dto.request.CreateUserRequest;
import com.teamteskboard.user.dto.request.LoginRequest;
import com.teamteskboard.user.dto.request.PasswordRequest;
import com.teamteskboard.user.dto.response.CreateUserResponse;
import com.teamteskboard.user.dto.response.GetUserResponse;
import com.teamteskboard.user.dto.response.LoginResponse;
import com.teamteskboard.user.dto.response.PasswordResponse;
import com.teamteskboard.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Valid @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    /**
     * 로그인
     * @param request 로그인 요청 DTO (아이디, 비밀번호)
     * @return 로그인 응답 DTO (토큰)
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
           @Valid @RequestBody LoginRequest request
    ) {
        ApiResponse<LoginResponse> result = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 비밀번호 확인
     * @param user 로그인한 사용자 정보
     * @param request 비밀번호 확인 요청 DTO (비밀번호)
     * @return 비밀번호 응답 DTO (일치 여부)
     */
    @PostMapping("/auth/verify-password")
    public ResponseEntity<ApiResponse<PasswordResponse>> verifyPassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PasswordRequest request
    ) {
        ApiResponse<PasswordResponse> result = userService.verifyPassword(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 사용자 정보 조회 요청 검증
     * @param userId 사용자 고유 번호
     * @return ApiResponse<GetUserResponse> 반환
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUser(
        @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    /**
     * 사용자 목록 조회 요청 검증
     * @return List<GetUserResponse> 반환
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<GetUserResponse>>> getUserList() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserList());
    }
}
