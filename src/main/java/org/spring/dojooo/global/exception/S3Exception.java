package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public class S3Exception extends BusinessException {
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}
