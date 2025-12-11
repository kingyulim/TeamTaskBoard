package com.teamteskboard.user.dto.request;

import com.teamteskboard.common.regexp.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    @NotBlank(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.NAME, message = "이름은 한글 또는 영문만 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "회원아이디가 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.USERNAME, message = "아이디는 영문 소문자로 시작해야 하며, 영문 소문자와 숫자만 입력할 수 있습니다")
    private String username;

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.EMAIL, message = "이메일 형식이 아닙니다.(email@email.com)")
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.PASSWORD, message = "비밀번호는 영문, 숫자, 그리고 ! @ # $ % 문자만 사용할 수 있습니다.")
    private String password;
}
