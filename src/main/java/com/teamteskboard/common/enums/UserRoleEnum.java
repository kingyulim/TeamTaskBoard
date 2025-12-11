package com.teamteskboard.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    USER("ROLE_NORMAL", "일반 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한")
    ;

    private final String role;        // Spring Security 권한 이름
    private final String description; // 권한 설명
}
