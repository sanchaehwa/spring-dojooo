package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class WrongUserEditException extends BusinessException {
    public WrongUserEditException(ErrorCode errorCode) {
        super(errorCode);
    }
}
