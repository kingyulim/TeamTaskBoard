package com.teamteskboard.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("비밀번호 매칭 성공 케이스")
    void matches_success() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
    }

    @Test
    @DisplayName("비밀번호 매칭 실패 케이스")
    void matches_fail() {
        // given
        String rawPassword = "testPassword1";
        String encodedPassword = passwordEncoder.encode("rawPassword2");

        // when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertFalse(matches);
    }

}