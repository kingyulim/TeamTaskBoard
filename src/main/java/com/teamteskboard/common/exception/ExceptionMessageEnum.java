package com.teamteskboard.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessageEnum {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 ID의 회원을 찾을 수 없습니다."),
    NOT_FOUND_TASK(HttpStatus.NOT_FOUND, "해당 작업을 찾을 수 없습니다."),
    TASK_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 작업 수정 권한이 없습니다."),
    INVALID_TASK_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 값입니다"),
    USER_SAME_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자명입니다."),
    USER_SAME_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST, "회원 정보가 일치하지 않습니다."),
    PATTERN_VALIDATION_FAILED_EXCEPTION(HttpStatus.BAD_REQUEST, "입력 값이 허용된 형식(정규식)에 맞지 않습니다."),
    COMMENT_ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "댓글 수정/삭제 권한이 없습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    TEAM_NAME_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 팀 이름입니다."),
    TEAM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 멤버를 찾을 수 없습니다."),
    TEAM_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 팀에 속한 멤버입니다."),
    TEAM_DELETE_HAS_MEMBERS(HttpStatus.CONFLICT, "팀에 멤버가 존재하여 삭제할 수 없습니다."),
    FORBIDDEN_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    ;

    private final HttpStatus status;
    private final String message;
}
