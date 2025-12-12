package com.teamteskboard.domain.activity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityTypeEnum {

    TASK_CREATED("saveTask", "작업 생성") {
        public String apply(String... str) {
            return String.format("새로운 작업 \"%s\"을 생성했습니다.", str[0]);
        }
    },
    TASK_UPDATED("updateTask", "작업 수정") {
        public String apply(String... str) {
            return String.format("작업 \"%s\" 정보를 수정했습니다.", str[0]);
        }
    },
    TASK_DELETED("deleteTask", "작업 삭제") {
        public String apply(String... str) {
            return String.format("작업 \"%s\"을 삭제했습니다.", str[0]);
        }
    },
    TASK_STATUS_CHANGED("updateTaskStatus", "작업 상태 변경") {
        public String apply(String... str) {
            return String.format("작업 \"%s\"의 상태를 %s으로 변경했습니다.", str[0], str[1]);
        }
    },
    COMMENT_CREATED("save", "댓글 작성") {
        public String apply(String... str) {
            return String.format("작업 \"%s\"에 댓글을 작성했습니다.", str[0]);
        }
    },
    COMMENT_UPDATED("update", "댓글 수정") {
        public String apply(String... str) {
            return String.format("작업 \"%s\"에서 댓글을 수정했습니다.", str[0]);
        }
    },
    COMMENT_DELETED("Delete", "댓글 삭제") {
        public String apply(String... str) {
            return String.format("작업 \"%s\"에서 댓글을 삭제했습니다.", str[0]);
        }
    };

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

    public abstract String apply(String... str);

}
