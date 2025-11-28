package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.IErrorCode;
import com.leucine.services.jaas.util.Utility;

import java.util.List;

public class JaasException extends RuntimeException {
  private static final long serialVersionUID = -4953235098385409196L;

  private final IErrorCode errorCode;
  private final List<IErrorCode> errorCodes;

  public JaasException() {
    super();
    errorCode = null;
    errorCodes = null;
  }

  public JaasException(String message, Throwable cause) {
    super(message, cause);
    errorCode = null;
    errorCodes = null;
  }

  public JaasException(String message) {
    super(message);
    errorCode = null;
    errorCodes = null;
  }

  public JaasException(Throwable cause) {
    super(cause);
    errorCode = null;
    errorCodes = null;
  }

  public JaasException(IErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
    errorCodes = null;
  }

  public JaasException(List<IErrorCode> errorCodes) {
    super();
    errorCode = null;
    this.errorCodes = errorCodes;
  }

  public JaasException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
    errorCodes = null;
  }

  public IErrorCode getErrorCode() {
    return errorCode;
  }

  public List<IErrorCode> getErrorCodes() {
    return errorCodes;
  }

  public boolean hasMoreThanOneError() {
    return !Utility.isEmpty(errorCodes);
  }
}
