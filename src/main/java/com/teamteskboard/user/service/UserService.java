package com.teamteskboard.user.service;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.common.enums.UserRoleEnum;
import com.teamteskboard.common.regexp.RegExp;
import com.teamteskboard.common.utils.PasswordEncoder;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.common.utils.JwtUtil;
import com.teamteskboard.user.dto.request.CreateUserRequest;
import com.teamteskboard.user.dto.request.LoginRequest;
import com.teamteskboard.user.dto.request.PasswordRequest;
import com.teamteskboard.user.dto.request.UpdateUserRequest;
import com.teamteskboard.user.dto.response.*;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 비지니스 로직 처리
     * @param request 회원가입 입력값 파라미터
     * @return ApiResponse<CreateUserResponse> json 반환
     */
    @Transactional
    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest request) {
        // userName 중복 체크
        if (userRepository.existsByUserName(request.getUserName())) {
            return ApiResponse.error("이미 존재하는 사용자 명입니다.");
        }

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("이미 사용 중인 이메일입니다.");
        }

        // 이메일 형식 체크
        if (!request.getEmail().matches(RegExp.EMAIL)) {
            return ApiResponse.error("올바른 이메일 형식이 아닙니다.");
        }

        // User 생성
        User user = new User(
                request.getName(),
                request.getUserName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(UserRoleEnum.USER);

        // 저장
        User createdUser = userRepository.save(user);

        return ApiResponse.success(
                "회원가입이 완료되었습니다.",
                CreateUserResponse.from(createdUser)
        );
    }

    /**
     * 로그인
     * @param request 로그인 요청 DTO (아이디, 비밀번호)
     * @return 로그인 응답 DTO (토큰)
     */
    @Transactional(readOnly = true)
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        // 아이디 확인 → 사용자 조회
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS));

        // 삭제된 계정인지, 비밀번호가 일치하는지 확인
        if (user.getIsDeleted() || !passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS);

        // 토큰 생성
        String token = jwtUtil.generateToken(user.getId(), user.getUserName(), user.getRole());

        return ApiResponse.success("로그인 성공", LoginResponse.from(token));
    }

    /**
     * 비밀번호 확인
     * @param id 로그인한 사용자 아이디
     * @param request 비밀번호 확인 요청 DTO (비밀번호)
     * @return 비밀번호 응답 DTO (일치 여부)
     */
    @Transactional(readOnly = true)
    public ApiResponse<PasswordResponse> verifyPassword(Long id, PasswordRequest request) {
        // 로그인된 아이디 확인 → 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS));

        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        return ApiResponse.success("비밀번호가 확인되었습니다.", PasswordResponse.from(valid));
    }

    /**
     * 사용자 정보 조회
     * @param userId 회원 고유 번호
     * @return ApiResponse<GetUserResponse> json 반환
     */
    @Transactional(readOnly = true)
    public ApiResponse<GetUserResponse> getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ApiResponse.error("사용자를 찾을 수 없습니다.");
        }

        return ApiResponse.success("사용자 정보 조회 성공", GetUserResponse.from(user));
    }

    /**
     * 사용자 목록 조회
     * @return List<GetUserResponse> 반환
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<GetUserResponse>> getUserList() {
        List<User> user = userRepository.findAll();

        List<GetUserResponse> getUserResponseList = user
                .stream()
                .map(u -> GetUserResponse.from(u))
                .toList();

        return ApiResponse.success("사용자 목록 조회 성공", getUserResponseList);
    }

    /**
     * 회원정보 수정 비지니스 로직 처리
     * @param userId 회원 고유 번호
     * @param request 수정할 데이터 파라미터
     * @return UpdateUserResponse json 반환
     */
    @Transactional
    public ApiResponse<UpdateUserResponse> updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이메일 중복 체크 (자기 자신 제외)
        if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            return ApiResponse.error("이미 사용 중인 이메일입니다.");
        }

        // 이름 수정
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        // 이메일 수정
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // 비밀번호 수정
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return ApiResponse.success("사용자 정보가 수정되었습니다.", UpdateUserResponse.from(user));
    }

    /**
     * 회원탈퇴 비지니스 로직 처리
     * @param userId 회원 고유 번호
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }
}