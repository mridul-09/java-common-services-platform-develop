package com.leucine.services.jaas.constant;

public final class Email {
  /* Template name constants */
  public static final String EMAIL_REGISTRATION_TEMPLATE = "REGISTER";
  public static final String EMAIL_REGISTRATION_REMINDER_TEMPLATE = "REMINDER_TO_REGISTER";
  public static final String EMAIL_RESET_PASSWORD_TEMPLATE = "RESET_PASSWORD";
  public static final String EMAIL_NOTIFY_ADMIN_INVITE_EXPIRED_TEMPLATE = "NOTIFY_ADMIN_INVITE_EXPIRED";
  public static final String EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET_TEMPLATE = "NOTIFY_ADMIN_PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET";
  public static final String EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_ACCOUNT_LOCKED_TEMPLATE = "NOTIFY_ADMIN_PASSWORD_RECOVERY_ACCOUNT_LOCKED";
  public static final String EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_KEY_EXPIRED_TEMPLATE = "NOTIFY_ADMIN_PASSWORD_RECOVERY_KEY_EXPIRED";

  /* Template variable constants */
  public static final String INVITEE = "invitee";
  public static final String REGISTRATION_LINK = "registrationLink";
  public static final String RESET_PASSWORD_LINK = "resetPasswordLink";
  public static final String LOGIN_LINK = "loginLink";
  public static final String USER_FULL_NAME = "userFullName";
  public static final String EMPLOYEE_ID = "employeeId";
  public static final String TOKEN = "token";
  /* Email Subjects */
  public static final String SUBJECT_REGISTRATION_INVITE = "Invitation to register with Leucine";
  public static final String SUBJECT_REMINDER_TO_REGISTER = "Reminder to complete your Registration";
  public static final String SUBJECT_RESET_PASSWORD = "Did you forget your Leucine password?";
  public static final String SUBJECT_NOTIFY_ADMIN_INVITE_EXPIRED = "Request to reset user's Registration invite";
  public static final String SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET = "Request to reset user's password";
  public static final String SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_ACCOUNT_LOCKED = "Request to unlock user's account";
  public static final String SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_KEY_EXPIRED = "Request to reset user's password";

  private Email() {
  }
}
