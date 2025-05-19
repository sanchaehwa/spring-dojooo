package org.spring.dojooo.auth.jwt.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
  public InvalidTokenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
