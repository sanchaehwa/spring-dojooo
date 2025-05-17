package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class IllegalArgumentExceptio extends BusinessException {
  public IllegalArgumentExceptio(ErrorCode errorCode) {
    super(errorCode);
  }
}
