package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.IErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

  private static final long serialVersionUID = 3153724492598193066L;
  private final IErrorCode errorCode;
  private final String token;

  public ForbiddenException() {
    super();
    errorCode = null;
    token = null;
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
    token = null;
  }

  public ForbiddenException(String message) {
    super(message);
    errorCode = null;
    token = null;
  }

  public ForbiddenException(Throwable cause) {
    super(cause);
    errorCode = null;
    token = null;
  }

  public ForbiddenException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
    token = null;
  }

  public ForbiddenException(IErrorCode errorCode, String token) {
    super(errorCode.message());
    this.errorCode = errorCode;
    this.token = token;
  }

  public ForbiddenException(IErrorCode errorCode, Throwable cause) {
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
