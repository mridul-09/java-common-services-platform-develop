package com.leucine.services.jaas.constant;

public final class Type {
  private Type() {
  }

  public enum ObjectType {
    OBJECT, LIST
  }

  public enum Object {
    AUDIT, USER, AUDIT_TRAILS
  }

  public enum Token {
    REGISTRATION, PASSWORD_RESET, JTI, ACCESS, REFRESH
  }

  public enum AdditionalVerification {
    EMPLOYEE_ID, EMAIL
  }
}
