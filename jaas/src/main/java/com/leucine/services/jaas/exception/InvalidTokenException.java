package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.IErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException {

  private static final long serialVersionUID = 3153724492598193066L;
  private final IErrorCode errorCode;
  private final String token;

  public InvalidTokenException() {
    super();
    errorCode = null;
    token = null;
  }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
    token = null;
  }

  public InvalidTokenException(String message) {
    super(message);
    errorCode = null;
    token = null;
  }

  public InvalidTokenException(Throwable cause) {
    super(cause);
    errorCode = null;
    token = null;
  }

  public InvalidTokenException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
    token = null;
  }

  public InvalidTokenException(IErrorCode errorCode, String token) {
    super(errorCode.message());
    this.errorCode = errorCode;
    this.token = token;
  }

  public InvalidTokenException(IErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
    token = null;
  }

  public IErrorCode getErrorCode() {
    return errorCode;
  }

  public String getToken() {
    return token;
  }
}
