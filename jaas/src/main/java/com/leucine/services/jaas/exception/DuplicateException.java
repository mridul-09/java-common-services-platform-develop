package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.IErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateException extends RuntimeException {
  private static final long serialVersionUID = -2904524968643214209L;

  private final IErrorCode errorCode;

  public DuplicateException() {
    super();
    errorCode = null;
  }

  public DuplicateException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
  }

  public DuplicateException(String message) {
    super(message);
    errorCode = null;
  }

  public DuplicateException(Throwable cause) {
    super(cause);
    errorCode = null;
  }

  public DuplicateException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public DuplicateException(IErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
  }

  public IErrorCode getErrorCode() {
    return errorCode;
  }
}
