package com.teamteskboard.domain.user.dto.request;

import com.teamteskboard.common.regexp.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.NAME, message = "이름은 한글 또는 영문만 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Pattern(regexp = RegExp.EMAIL, message = "이메일 형식이 아닙니다.(email@email.com)")
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
}
