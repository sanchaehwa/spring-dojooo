package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class NotFoundTagException extends BusinessException {
    public NotFoundTagException(ErrorCode errorCode) {
        super(errorCode);
    }
}
