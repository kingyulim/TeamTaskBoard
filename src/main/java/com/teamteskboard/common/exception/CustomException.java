package com.teamteskboard.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionMessageEnum exceptionMessage;

    public CustomException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
        this.exceptionMessage = exceptionMessageEnum;
    }
}
