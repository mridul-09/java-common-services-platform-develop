package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.IErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenRevokedException extends RuntimeException {

  private static final long serialVersionUID = 3153724492598193066L;

  private final IErrorCode errorCode;

  public TokenRevokedException() {
    super();
    errorCode = null;
  }

  public TokenRevokedException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
  }

  public TokenRevokedException(String message) {
    super(message);
    errorCode = null;
  }

  public TokenRevokedException(Throwable cause) {
    super(cause);
    errorCode = null;
  }

  public TokenRevokedException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public TokenRevokedException(IErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
  }

  public IErrorCode getErrorCode() {
    return errorCode;
  }
}
