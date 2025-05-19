package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
