package org.spring.dojooo.main.follow.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.DuplicateException;

public class DuplicateFollowException extends DuplicateException {
    public DuplicateFollowException(ErrorCode errorCode) {
        super(errorCode);
    }
}
