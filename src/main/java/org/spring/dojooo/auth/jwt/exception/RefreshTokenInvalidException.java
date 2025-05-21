package org.spring.dojooo.auth.jwt.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class RefreshTokenInvalidException extends BusinessException {
  public RefreshTokenInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }
}
