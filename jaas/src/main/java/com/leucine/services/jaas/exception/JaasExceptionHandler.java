package com.leucine.services.jaas.exception;

import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.IErrorCode;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.dto.response.ResponseError;
import com.leucine.services.jaas.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class JaasExceptionHandler extends ResponseEntityExceptionHandler {
  public JaasExceptionHandler() {
    super();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ResponseError> errors = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add((new ResponseError())
        .setCode(ErrorCode.BAD_REQUEST.code())
        .setField(error.getField())
        .setMessage(error.getDefaultMessage()));
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add((new ResponseError())
        .setCode(ErrorCode.BAD_REQUEST.code())
        .setMessage(error.getDefaultMessage())
        .setIgnore(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()));
    }
    return buildErrorResponse(ex, errors, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.BAD_REQUEST);
    return buildErrorResponse(ex, errors, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
    List<ResponseError> errors = new ArrayList<>();
    for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
      errors.add((new ResponseError())
        .setCode(ErrorCode.BAD_REQUEST.code())
        .setMessage(error.getMessage()));
    }
    return buildErrorResponse(ex, errors, HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.RESOURCE_NOT_FOUND);
    return buildErrorResponse(ex, errors, HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.ACCESS_DENIED);
    return buildErrorResponse(ex, errors, HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(value = {ForbiddenException.class})
  protected ResponseEntity<Object> handleForbiddenException(ForbiddenException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ex.getErrorCode());
    return buildErrorResponse(ex.getToken(), ex, errors, HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(value = {InvalidTokenException.class})
  protected ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ex.getErrorCode());
    return buildErrorResponse(ex.getToken(), ex, errors, HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(value = {AuthenticationException.class})
  protected ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.INVALID_CREDENTIALS);
    return buildErrorResponse(ex, errors, HttpStatus.FORBIDDEN, request);
  }

  @ExceptionHandler(value = {DuplicateException.class})
  protected ResponseEntity<Object> handleDuplicateException(DuplicateException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ex.getErrorCode());
    return buildErrorResponse(ex, errors, HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.BAD_REQUEST);
    return buildErrorResponse(ex, errors, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {JaasException.class})
  protected ResponseEntity<Object> handleJaasException(JaasException ex, WebRequest request) {
    List<ResponseError> errors = getResponseErrors(ex);
    return buildErrorResponse(ex, errors, HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

//  @ExceptionHandler(value = {Exception.class})
//  ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
//    List<ResponseError> errors = getResponseErrors(ex, ErrorCode.BAD_REQUEST);
//    return buildErrorResponse(ex, errors, HttpStatus.UNPROCESSABLE_ENTITY, request);
//  }

  // Used by AuthenticationFilter
  public void handleException(HttpServletResponse response, Exception exception) {
    HttpStatus status;
    List<ResponseError> errors = null;
    if (exception instanceof InvalidTokenException) {
      InvalidTokenException ex = (InvalidTokenException) exception;
      status = HttpStatus.FORBIDDEN;
      errors = getResponseErrors(ex, ex.getErrorCode());
    } else if (exception instanceof org.springframework.security.access.AccessDeniedException) {
      status = HttpStatus.FORBIDDEN;
      errors = getResponseErrors(exception, ErrorCode.ACCESS_DENIED);
    } else if (exception instanceof ForbiddenException) {
      ForbiddenException ex = (ForbiddenException) exception;
      status = HttpStatus.FORBIDDEN;
      errors = getResponseErrors(ex, ErrorCode.FORBIDDEN);
    } else if (exception instanceof TokenRevokedException) {
      status = HttpStatus.FORBIDDEN;
      errors = getResponseErrors(exception, ErrorCode.JTI_TOKEN_REVOKED);
    } else if (exception instanceof JaasException) {
      status = HttpStatus.UNPROCESSABLE_ENTITY;
      errors = getResponseErrors((JaasException) exception);
    } else {
      status = HttpStatus.UNPROCESSABLE_ENTITY;
      errors = getResponseErrors(exception, ErrorCode.UNKNOWN);
    }
    log.error("Exception occurred.", exception);
    handleException(status, response, errors);
  }

  private List<ResponseError> getResponseErrors(JaasException ex) {
    List<ResponseError> errors;
    if (ex.hasMoreThanOneError()) {
      errors = ex.getErrorCodes().stream().map(this::getResponseError).collect(Collectors.toList());
    } else if (!Utility.isNull(ex.getErrorCode())) {
      errors = getResponseErrors(ex, ex.getErrorCode());
    } else {
      errors = getResponseErrors(ex, ErrorCode.UNKNOWN);
    }
    return errors;
  }

  private List<ResponseError> getResponseErrors(Exception ex, IErrorCode errorCode) {
    return Collections.singletonList(getResponseError(errorCode).setIgnore(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()));
  }

  private ResponseError getResponseError(IErrorCode errorCode) {
    return (new ResponseError())
      .setCode(errorCode.code())
      .setMessage(errorCode.message());
  }

  // Used by AuthenticationFilter
  private void handleException(HttpStatus status, HttpServletResponse response, List<ResponseError> errors) {
    // TODO check the application log level, if debug then send the ignore parameter, if trace then also send the stacktrace
    response.setStatus(status.value());
    response.setContentType("application/json");
    try {
      Writer writer = response.getWriter();
      writer.write(Response.builder().object(Type.ObjectType.LIST).message("ERROR").errors(errors).status(status).build().toString());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ResponseEntity<Object> buildErrorResponse(Exception ex, List<ResponseError> errors, HttpStatus status, WebRequest request) {
    log.error("Exception occurred.", ex);
    // TODO check the application log level, if debug then send the ignore parameter, if trace then also send the stacktrace
    Response response = Response.builder().object(Type.ObjectType.LIST).message("ERROR").errors(errors).status(status).build();
    return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }

  private ResponseEntity<Object> buildErrorResponse(String token, Exception ex, List<ResponseError> errors, HttpStatus status, WebRequest request) {
    log.error("Exception occurred.", ex);
    Response response = Response.builder().object(Type.ObjectType.LIST).token(token).message("ERROR").errors(errors).status(status).build();
    return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }
}