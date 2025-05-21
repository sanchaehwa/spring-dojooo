package org.spring.dojooo.auth.jwt.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class RefreshTokenExpiredException extends BusinessException {
    public RefreshTokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
