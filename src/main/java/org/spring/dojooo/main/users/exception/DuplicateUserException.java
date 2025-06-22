package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.DuplicateException;

public class DuplicateUserException extends DuplicateException {
    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
