package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.BusinessException;

public class DuplicateTagException extends BusinessException {
  public DuplicateTagException(ErrorCode errorCode) {
    super(errorCode);
  }
}
