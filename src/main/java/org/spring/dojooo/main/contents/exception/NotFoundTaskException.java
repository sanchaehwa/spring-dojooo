package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class NotFoundTaskException extends BusinessException {
    public NotFoundTaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
