package org.spring.dojooo.auth.jwt.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class RefreshTokenNullException extends BusinessException {
    public RefreshTokenNullException(ErrorCode errorCode) {
        super(errorCode);
    }
}
