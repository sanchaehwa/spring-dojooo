package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class DuplicateTechLogTitleException extends BusinessException {
    public DuplicateTechLogTitleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
