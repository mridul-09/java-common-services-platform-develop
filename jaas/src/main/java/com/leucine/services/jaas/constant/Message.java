package com.leucine.services.jaas.constant;

public final class Message {
  public static final String CANCELLED_THE_INVITE = "Invite cancelled";
  public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
  public static final String EMAIL_AVAILABLE = "Email available";
  public static final String EMPLOYEE_ID_ALREADY_EXISTS = "Employee Id already exists";
  public static final String EMPLOYEE_ID_AVAILABLE = "Employee Id available";
  public static final String INVITE_RESENT_SUCCESSFULLY = "Invite resent successfully";
  public static final String PASSWORD_RESET_REQUEST_SUCCESS = "Password reset request success";
  public static final String PASSWORD_UPDATED = "Password updated";
  public static final String SUCCESS = "Updated successfully.";
  public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
  public static final String USERNAME_AVAILABLE = "Username available";

  private Message() {
  }

  public static final class Error {
    public static final String ATLEAST_ONE_FACILITY_REQUIRED = "At least one facility must be selected";
    public static final String FORBIDDEN = "You do not have sufficient privilege(s) to access the resource";
    public static final String ILLEGAL_FILTER = "Error parsing filters";
    public static final String INVALID_CHARACTERS_IN_FILE_NAME = "Filename contains invalid characters: {0}";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or Expired Token";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";
    public static final String MAXIMUM_FILE_SIZE_EXCEEDED = "Maximum upload size exceeded";
    public static final String ORGANISATION_NOT_FOUND = "Organisation not found";
    public static final String RESOURCE_NOT_FOUND = "Resource {0} not found";
    public static final String STORING_FILE = "Error storing file: {0}";
    //    public static final String USER_ALREADY_VERIFIED = "User already verified";
    public static final String USER_NOT_FOUND = "User not found";

    private Error() {
      throw new IllegalStateException("Error class cannot be instantiated");
    }
  }

  public static final class UserAudit {
    public static final String ADD_USER = "{0} {1} (ID: {2}) invited {3} {4} (ID: {5}) to register as \"{6}\" with reason \"{7}\" .";
    public static final String CANCEL_INVITE = "{0} {1} (ID: {2}) cancelled the registration invite of {3} {4} (ID: {5}).";
    public static final String ARCHIVE_USER = "{0} {1} (ID: {2}) archived {3} {4} (ID: {5}) with reason \"{6}\".";
    public static final String UNARCHIVE_USER = "{0} {1} (ID: {2}) restored {3} {4} (ID: {5}) with reason \"{6}\".";
    public static final String UNLOCK_USER = "{0} {1} (ID: {2}) unlocked {3} {4} (ID: {5}).";
    public static final String LOCK_USER = "{0} {1}''s (ID: {2}) account was locked due to multiple failed login attempts.";
    public static final String REGISTER = "{0} {1} (ID: {2}) registered themselves with Platform as \"{3}\".";
    public static final String LOGIN_SUCCESS = "{0} {1} (ID: {2}) logged in.";
    public static final String LOGIN_FAILED = "Security Alert: Someone attempted to log in to {0} {1}''s (ID: {2}) account with a wrong password.";
    public static final String LOGIN_FAILED_USERNAME_NOT_FOUND = "Security Alert: Login failed due to username not being found.";
    public static final String LOGOUT = "{0} {1} (ID: {2}) logged out.";
    public static final String UPDATE_PASSWORD = "{0} {1} (ID: {2}) updated their Account password.";
    public static final String UPDATE_USER = "{0} {1} (ID: {2}) updated the \"{6}\" of {3} {4} (ID: {5}) from \"{7}\" to \"{8}\" with reason \"{9}\".";
    public static final String UPDATE_CHALLENGE_QUESTIONS_ANSWER = "{0} {1} (ID: {2}) updated their Challenge question.";
    public static final String RESET_TOKEN = "{0} {1} (ID: {2}) regenerated secret key for {3} {4} (ID: {5}).";
    public static final String PASSWORD_RECOVERY_EMAIL = "{0} {1} (ID: {2}) sent an email to themselves for password recovery.";
    public static final String CHALLENGE_QUESTIONS_ANSWER_VALIDATION_FAILED = "{0} {1} (ID: {2}) incorrectly answered their challenge question for password recovery.";
    public static final String SYSTEM_ADMIN_NOTIFY_FOR_PASSWORD_RESET = "{0} {1} (ID: {2}) contacted administrator to reset their password.";
    public static final String SYSTEM_ADMIN_NOTIFY_FOR_UNLOCK_ACCOUNT = "{0} {1} (ID: {2}) contacted administrator to unlock their account.";
    public static final String SYSTEM_ADMIN_NOTIFY_FOR_RESET_INVITE = "{0} {1} (ID: {2}) contacted administrator to reset their invite.";
    public static final String INVALID_SECRET_KEY = "Security Alert: Someone entered invalid Secret key.";
    public static final String ADDITIONAL_VERIFICATION_FAILED = "Security Alert: Additional verification failed for {0} {1} (ID: {2}).";
    public static final String INVITE_EXPIRED_AS_ADDITIONAL_VERIFICATION_FAILED_ATTEMPTS_EXCEEDED = "{0} {1}''s (ID: {2}) invite was expired due to multiple incorrect answers to additional verification.";
    public static final String ACCOUNT_LOCKED_AS_CHALLENGE_QUESTION_FAILED_ATTEMPTS_EXCEEDED = "{0} {1}''s (ID: {2}) account was locked due to multiple incorrect answers to challenge questions.";
    public static final String SYSTEM_PASSWORD_EXPIRED = "{0} {1}''s (ID: {2}) password expired.";
    public static final String SYSTEM_AUTO_UNLOCK = "{0} {1}''s (ID: {2}) account was auto-unlocked.";
    public static final String SYSTEM_INVITE_EXPIRED = "{0} {1}''s (ID: {2}) invite expired.";
    public static final String FACILITY_SWITCH = "{0} {1} (ID: {2}) switched to {3}.";

    private UserAudit() {
      throw new IllegalStateException("UserAudit class cannot be instantiated");
    }
  }

}
