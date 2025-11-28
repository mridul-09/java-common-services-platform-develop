package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.IErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3153724492598193066L;

  private final IErrorCode errorCode;

  public ResourceNotFoundException() {
    super();
    errorCode = null;
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
  }

  public ResourceNotFoundException(String message) {
    super(message);
    errorCode = null;
  }

  public ResourceNotFoundException(Throwable cause) {
    super(cause);
    errorCode = null;
  }

  public ResourceNotFoundException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public ResourceNotFoundException(IErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
  }

  public IErrorCode getErrorCode() {
    return errorCode;
  }
}
