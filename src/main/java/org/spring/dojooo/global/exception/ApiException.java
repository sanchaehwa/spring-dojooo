package org.spring.dojooo.global.exception;

import org.spring.dojooo.global.ErrorCode;

public class ApiException extends RuntimeException {
  private final ErrorCode errorCode;

  public ApiException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
