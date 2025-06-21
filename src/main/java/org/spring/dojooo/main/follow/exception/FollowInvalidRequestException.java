package org.spring.dojooo.main.follow.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;
import org.spring.dojooo.global.exception.InvalidRequestException;

public class FollowInvalidRequestException extends InvalidRequestException {
    public FollowInvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
