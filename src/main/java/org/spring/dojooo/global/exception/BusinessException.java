package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
  public ErrorCode getErrorCode() {return errorCode;}
}
