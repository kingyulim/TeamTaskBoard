package com.teamteskboard.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessageEnum {
    NO_USER_ID(HttpStatus.NOT_FOUND, "해당 ID의 회원을 찾을 수 없습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 작업을 찾을 수 없습니다."),
    TASK_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "해당 작업 수정 권한이 없습니다."),
    INVALID_TASK_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 값입니다"),
    NULL_TITLE_ASSIGNEE(HttpStatus.BAD_REQUEST, "제목과 작성자는 필수입니다."),

    USER_SAME_ACOUNT(HttpStatus.BAD_REQUEST, "중복되는 데이터가 존재합니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

    //댓글
    NOT_FOUND_TASK(HttpStatus.NOT_FOUND, "해당 작업물을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),


    NO_MEMBER_ID(HttpStatus.NOT_FOUND, "해당 ID의 회원을 찾을 수 없습니다."),
    NO_MEMBER_INFO(HttpStatus.NOT_FOUND, "해당 회원 정보를 찾을 수 없습니다."),
    LOGIN_CHECK(HttpStatus.UNAUTHORIZED, "로그인이 되어있습니다."),
    SESSION_CHECK(HttpStatus.NOT_FOUND, "세션이 존재하지 않습니다."),
    TOKEN_CHECK(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    IS_DELETION_USER(HttpStatus.UNAUTHORIZED, "탈퇴한 회원입니다."),
    INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST, "회원 정보가 일치하지 않습니다."),
    NO_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 되지 않았습니다."),
    BOARD_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 게시물은 존재하지 않습니다."),
    POST_ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "접근 불가능한 게시물입니다."),
    PATTERN_VALIDATION_FAILED_EXCEPTION(HttpStatus.BAD_REQUEST, "입력 값이 허용된 형식(정규식)에 맞지 않습니다."),
    DUPLICATE_DATA_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 데이터 입니다"),
    FAILED_DELETE_FILE(HttpStatus.BAD_REQUEST, "파일 삭제에 실패하였습니다."),
    NOT_FOUND_THIS_FILE(HttpStatus.BAD_REQUEST, "잘못된 파일 요청입니다"),
    SELF_LIKE_EXCEPTION(HttpStatus.UNAUTHORIZED, "본인이 작성한 게시물과 댓글에 좋아요를 남길 수 없습니다."),
    SELF_FOLLOW_EXCEPTION(HttpStatus.UNAUTHORIZED, "스스로에게 팔로우, 언팔로우 할 수 없습니다."),
    ALREADY_FRIEND_EXCEPTION(HttpStatus.UNAUTHORIZED, "해당 유저는 이미 친구입니다."),
    NOT_FRIEND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저는 친구가 아닙니다."),
    COMMENT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    COMMENT_ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "댓글 수정/삭제 권한이 없습니다."),
    NULL_POINT_EXCEPTION(HttpStatus.BAD_REQUEST, "입력란이 비어있습니다."),
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
