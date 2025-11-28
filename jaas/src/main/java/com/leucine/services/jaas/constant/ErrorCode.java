package com.leucine.services.jaas.constant;

public enum ErrorCode implements IErrorCode {
  UNKNOWN(101000, "Unknown error."),
  // Authentication & Authorization
  INVALID_TOKEN(101001, "Secret key is invalid, missing or expired. Contact your Administrator."),
  ACCESS_DENIED(101002, "You do not have sufficient privilege(s) to access the resource."),
  FORBIDDEN(101002, "You do not have sufficient privilege(s) to access the resource."),
  INVALID_CREDENTIALS(101003, "Provided credentials are incorrect. Please check and try again."),
  PASSWORD_EXPIRED(101004, "Password Expired. Use Forgot Password to set a new one."),
  REGISTRATION_TOKEN_EXPIRED(101005, "Invite has expired. Contact your Administrator for re-sending the invite."),
  FORGOT_PASSWORD_TOKEN_EXPIRED(101006, "Request has expired. Use the Forgot Password option again."),
  SECRET_KEY_GENERATION_LIMIT_EXCEEDED(101007, "We are facing issues generating secret key, please try after sometime."),
  INVALID_ENCRYPTION(101008, "Sensitive data need to be encrypted with appropriate public key."),
  INVALID_USERNAME_OR_EMAIL(101009, "Provided details are incorrect. Please check and try again."),
  ACTION_DENIED(101010, "You do not have sufficient privilege(s) to perform the action."),

  JWT_TOKEN_INVALID(101101, "Session is invalid."),
  JTI_TOKEN_REVOKED(101102, "Session has been revoked."),
  JWT_REFRESH_TOKEN_EXPIRED(101103, "Session has expired. Please re-login."),
  JWT_ACCESS_TOKEN_EXPIRED(101104, "Session has expired."),
  JWT_TOKEN_EXPIRED(101105, "Session has expired. Please refresh your page."),
  //  JTI_FORBIDDEN(101106, "You do not have sufficient privilege(s) to access the resource."),
  INVALID_TOKEN_STATE(101106, "Invalid token state."),


  // General
  ILLEGAL_FILTER(102001, "Error parsing filters."),
  BAD_REQUEST(102002, "Invalid request."),
  RESOURCE_NOT_FOUND(102003, "Resource not found."),
  INVALID_SERVICE_ID(102004, "Invalid service in X-Service-Id header."),

  // Password Policy
  PASSWORD_POLICY_TOO_SHORT(103001, "Password must be {0} or more characters in length."),
  PASSWORD_POLICY_INSUFFICIENT_LOWERCASE(103002, "Password must contain {0} or more lowercase characters."),
  PASSWORD_POLICY_INSUFFICIENT_UPPERCASE(103003, "Password must contain {0} or more uppercase characters."),
  PASSWORD_POLICY_INSUFFICIENT_SPECIAL_CHARACTERS(103004, "Password must contain {0} or more special characters."),
  PASSWORD_POLICY_INSUFFICIENT_NUMERIC_CHARACTERS(103005, "Password must contain {0} or more numeric characters."),
  PASSWORD_POLICY_HISTORY_VIOLATION(103006, "Your new Password cannot be the same as your last {0} Passwords."),
  PASSWORD_POLICY_SIMILAR_TO_USERNAME_OR_EMAIL(103007, "Password cannot be similar to username or email address."),
  PASSWORD_POLICY_OLD_PASSWORD_AND_NEW_PASSWORD_ARE_SAME(103008, "Old password and new password must not match."),
  EXISTING_PASSWORD_DOES_NOT_MATCH(103009, "Existing password does not match with our records."),
  PASSWORD_POLICY_CONFIRM_PASSWORD_AND_PASSWORD_ARE_NOT_SAME(103010, "Confirm password and password must match."),

  // User
  USER_WITH_USERNAME_ALREADY_EXISTS(104001, "Username already exists."),
  USER_WITH_EMPLOYEE_ID_ALREADY_EXISTS(104002, "Employee ID already exists."),
  USER_WITH_EMAIL_ALREADY_EXISTS(104003, "Email address already exists."),
  USER_ALREADY_REGISTERED(104004, "User already registered."),
  USER_ALREADY_ARCHIVED(104005, "User already archived"),
  USER_SELF_LOCKED(104006, "Your account has been locked. Contact your Administrator."),
  USER_ARCHIVED(104007, "Your account has been archived. Contact your Administrator."),
  USER_NOT_REGISTERED(104008, "Please complete registration"),
  USER_INVITE_CANCELLED(104009, "Your invite has been cancelled. Contact your Administrator."),
  USER_INVITE_EXPIRED(104010, "Your invite has expired. Contact your Administrator."),
  USER_ADDITIONAL_VERIFICATION_FAILED(104011, "Supplied details do not match with our system."),
  USER_LOCKED(104012, "User is locked."),
  USER_CHALLENGE_QUESTION_VALIDATION_FAILED(104013, "Supplied details do not match with our system."),
  USER_ACCOUNT_LOCKED(104014, "User is locked."),
  USER_CANNOT_ARCHIVE_SELF(104015, "One cannot archive its own account."),
  USER_ACCOUNT_OWNER_CANNOT_BE_ARCHIVED(104016, "Account owner cannot be archived."),
  INVALID_ID_TOKEN_MISSING_KID(104017, "KID missing in id token"),
  USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS(104018, "Username or email already exists."),
  INVALID_USER_TYPE(104019, "Invalid user type."),
  USER_TYPE_CANNOT_BE_UPDATED(104020, "User type cannot be updated."),
  USER_IS_ARCHIVED(104021, "User is archived."),

  // OKTA
  USER_DETAILS_CHANGED_ON_SIGN_IN_PAGE(105001, "User details were changed on the Sign-in page! To successfully log in, do not change User details on the sign-in page"),
  SSO_NOT_CONFIGURED(105002, "OKTA not configured."),
  // Client Credentials
  INVALID_CLIENT_CREDENTIALS(106001, "Invalid client credentials.");

  private final int code;
  private final String message;

  ErrorCode(int code, String message) {
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


