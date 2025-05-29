package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public class DuplicateException extends BusinessException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
