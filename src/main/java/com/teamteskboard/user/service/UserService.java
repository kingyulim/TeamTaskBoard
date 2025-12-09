package com.teamteskboard.user.service;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.common.enums.UserRoleEnum;
import com.teamteskboard.common.utils.PasswordEncoder;
import com.teamteskboard.user.dto.request.CreateUserRequest;
import com.teamteskboard.user.dto.response.CreateUserResponse;
import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        return ApiResponse.success(
                "회원가입이 완료되었습니다.",
                CreateUserResponse.from(createdUser)
        );
    }
}
