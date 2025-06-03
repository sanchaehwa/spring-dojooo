package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class NotFoundTechLogException extends BusinessException {
    public NotFoundTechLogException(ErrorCode errorCode) {
        super(errorCode);
    }
}
