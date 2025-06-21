package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;
import org.spring.dojooo.global.exception.DuplicateException;

public class DuplicateTechLogTitleException extends DuplicateException {
    public DuplicateTechLogTitleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
