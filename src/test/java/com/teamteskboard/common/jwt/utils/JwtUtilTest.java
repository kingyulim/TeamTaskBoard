package com.teamteskboard.common.jwt.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.teamteskboard.domain.user.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // Reflection을 이용해 @Value 주입 대체
        ReflectionTestUtils.setField(jwtUtil, "secretKeyString", SECRET_KEY);

        // @PostConstruct 수동 호출
        jwtUtil.init();
    }

    @Test
    @DisplayName("JWT 토큰 생성 시 userId, username, role 정보가 정상적으로 포함된다")
    void generateToken_success() {
        // Given
        Long userId = 1L;
        String username = "min";
        UserRoleEnum role = UserRoleEnum.ADMIN;

        // When
        String token = jwtUtil.generateToken(userId, username, role);

        // Then
        JwtParser parser = (JwtParser) ReflectionTestUtils.getField(jwtUtil, "parser");

        assert parser != null;
        Claims claims = parser.parseSignedClaims(token).getPayload();

        assertThat(claims.get("userId", Long.class)).isEqualTo(userId);
        assertThat(claims.get("username", String.class)).isEqualTo(username);
        assertThat(claims.get("auth", String.class)).isEqualTo(role.name());
    }

    @Test
    @DisplayName("유효한 토큰이면 true 반환")
    void validateToken_success() {
        // Given
        Long userId = 1L;
        String username = "min";
        UserRoleEnum role = UserRoleEnum.ADMIN;
        String token = jwtUtil.generateToken(userId, username, role);

        // When
        boolean valid = jwtUtil.validateToken(token);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("유효한 토큰이 아니면 false 반환")
    void validateToken_fail() {
        // Given
        String token = "thisiswrongtoken";

        // When
        boolean valid = jwtUtil.validateToken(token);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    @DisplayName("토큰에서 userId, username, role을 정상적으로 복호화한다")
    void extract_success() {
        // Given
        Long userId = 1L;
        String username = "min";
        UserRoleEnum role = UserRoleEnum.ADMIN;
        String token = jwtUtil.generateToken(userId, username, role);

        // When
        Long extractUserId = jwtUtil.extractUserId(token);
        String extractUsername = jwtUtil.extractUsername(token);
        String extractRole = jwtUtil.extractRole(token);

        // Then
        assertThat(extractUserId).isEqualTo(userId);
        assertThat(extractUsername).isEqualTo(username);
        assertThat(extractRole).isEqualTo(role.name());
    }
}