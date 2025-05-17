package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public class NotFoundException extends BusinessException {
  public NotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
