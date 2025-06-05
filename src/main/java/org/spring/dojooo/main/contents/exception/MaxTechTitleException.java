package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class MaxTechTitleException extends BusinessException {
    public MaxTechTitleException(ErrorCode errorCode) {super(errorCode);}
}
