package org.spring.dojooo.global.exception;

import lombok.Getter;
import org.spring.dojooo.global.ErrorCode;

@Getter
public abstract class InvalidRequestException extends BusinessException {
    private final ErrorCode errorCode;
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

}
