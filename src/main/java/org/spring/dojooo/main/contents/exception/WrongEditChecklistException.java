package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class WrongEditChecklistException extends BusinessException {
    public WrongEditChecklistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
