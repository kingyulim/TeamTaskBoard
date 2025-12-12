package com.teamteskboard.domain.user.service;

import com.teamteskboard.common.config.PasswordEncoder;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.dto.request.CreateUserRequest;
import com.teamteskboard.domain.user.dto.response.CreateUserResponse;
import com.teamteskboard.domain.user.enums.UserRoleEnum;
import com.teamteskboard.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원정보 생성 성공")
    void createUser() {
        // given
        User testUser = new User(
                "테스트",
                "test",
                "test@email.com",
                "1234",
                UserRoleEnum.USER
        );

        // when
        User createdUser = userRepository.save(testUser);

        // then
        assertNotNull(createdUser.getId());                 // PK가 생성되었는지
        assertEquals("테스트", createdUser.getName());        // name 검증
        assertEquals("test", createdUser.getUserName());     // username 검증
        assertEquals("test@email.com", createdUser.getEmail()); // email 검증
        assertEquals("1234", createdUser.getPassword());     // password 검증
        assertEquals(UserRoleEnum.USER, createdUser.getRole()); // 권한 검증
    }
}