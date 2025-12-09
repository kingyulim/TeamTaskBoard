package com.teamteskboard.user.service;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.common.enums.UserRoleEnum;
import com.teamteskboard.common.utils.PasswordEncoder;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.common.utils.JwtUtil;
import com.teamteskboard.common.utils.PasswordEncoder;
import com.teamteskboard.user.dto.request.CreateUserRequest;
import com.teamteskboard.user.dto.request.LoginRequest;
import com.teamteskboard.user.dto.request.PasswordRequest;
import com.teamteskboard.user.dto.response.CreateUserResponse;
import com.teamteskboard.user.dto.response.GetUserResponse;
import com.teamteskboard.user.dto.response.LoginResponse;
import com.teamteskboard.user.dto.response.PasswordResponse;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
        User user = new User(
                request.getName(),
                request.getUserName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(UserRoleEnum.USER);

        User createdUser = userRepository.save(user);

        return ApiResponse.success("회원가입이 완료되었습니다.", CreateUserResponse.from(createdUser)
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
        String token = jwtUtil.generateToken(user.getUserName(), user.getRole());

        return ApiResponse.success("로그인 성공", LoginResponse.from(token));
    }

    /**
     * 비밀번호 확인
     * @param username 로그인한 사용자 아이디
     * @param request 비밀번호 확인 요청 DTO (비밀번호)
     * @return 비밀번호 응답 DTO (일치 여부)
     */
    @Transactional(readOnly = true)
    public ApiResponse<PasswordResponse> verifyPassword(String username, PasswordRequest request) {
        // 로그인된 아이디 확인 → 사용자 조회
        User user = userRepository.findByUserName(username)
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
}
