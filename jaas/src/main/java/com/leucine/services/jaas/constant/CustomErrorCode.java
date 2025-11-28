package com.leucine.services.jaas.constant;

public class CustomErrorCode implements IErrorCode {
  private final int code;
  private final String message;

  public CustomErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String message() {
    return message;
  }

  @Override
  public int code() {
    return code;
  }

  @Override
  public String toString() {
    return code + ": " + message;
  }

}


