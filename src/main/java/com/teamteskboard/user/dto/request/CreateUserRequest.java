package com.teamteskboard.user.dto.request;

import com.teamteskboard.common.regexp.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    @NotBlank(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.NAME)
    private String name;

    @NotBlank(message = "회원아이디가 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.USERNAME)
    private String username;

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.EMAIL)
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.PASSWORD)
    private String password;
}
