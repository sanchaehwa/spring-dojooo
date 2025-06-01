package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class MaxTagLengthException extends BusinessException {
    public MaxTagLengthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
