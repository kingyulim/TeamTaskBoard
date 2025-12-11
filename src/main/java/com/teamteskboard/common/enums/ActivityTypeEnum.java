package com.teamteskboard.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityTypeEnum {
    TASK_CREATED("saveTask", "작업 생성"),
    TASK_UPDATED("updateTask", "작업 수정"),
    TASK_DELETED("deleteTask", "작업 삭제"),
    TASK_STATUS_CHANGED("updateTaskStatus", "작업 상태 변경"),
    COMMENT_CREATED("", "댓글 작성"),
    COMMENT_UPDATED("", "댓글 수정"),
    COMMENT_DELETED("", "댓글 삭제");

    private final String methodName;
    private final String type;

    public static ActivityTypeEnum fromMethodName(String methodName) {
        for (ActivityTypeEnum typeEnum : values()) {
            if (typeEnum.methodName.equals(methodName)) {
                return typeEnum;
            }
        }
        return null;
    }
}
