package org.spring.dojooo.main.contents.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class DuplicateMemoHeader extends BusinessException {
    public DuplicateMemoHeader(ErrorCode errorCode) {
        super(errorCode);
    }
}
