package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class NotUserEqualsCurrentUserException extends BusinessException {
    public NotUserEqualsCurrentUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
