package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class MaxTagRegisterException extends BusinessException {
    public MaxTagRegisterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
