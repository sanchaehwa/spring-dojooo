package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public abstract class DuplicateException extends BusinessException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
