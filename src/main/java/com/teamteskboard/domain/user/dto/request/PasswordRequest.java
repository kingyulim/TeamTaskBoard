package com.teamteskboard.domain.user.dto.request;

import com.teamteskboard.common.regexp.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordRequest {

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.PASSWORD)
    private String password;
}
