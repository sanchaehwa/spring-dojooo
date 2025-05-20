package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class ModificationTimeExceededException extends BusinessException {
    public ModificationTimeExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
