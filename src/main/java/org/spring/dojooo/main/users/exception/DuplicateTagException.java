package org.spring.dojooo.main.users.exception;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.DuplicateException;

public class DuplicateTagException extends DuplicateException {
  public DuplicateTagException(ErrorCode errorCode) {
    super(errorCode);
  }
}
