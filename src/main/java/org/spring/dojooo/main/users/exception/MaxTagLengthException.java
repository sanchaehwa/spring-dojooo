package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class MaxTegLengthException extends BusinessException {
    public MaxTegLengthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
