package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
