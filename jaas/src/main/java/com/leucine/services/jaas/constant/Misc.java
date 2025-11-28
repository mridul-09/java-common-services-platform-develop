package com.leucine.services.jaas.constant;

import com.leucine.services.jaas.model.Facility;

import java.util.*;
import java.util.stream.Collectors;

public final class Misc {
  public static final String INTERCEPTOR_SERVICES_ID = "com.leucine.services.jaas.model.ServicesId";
  public static final String BEARER = "Bearer";
  public static final String LOGOUT_ENDPOINT = "/v1/auth/logout";

  // As per RFC 5322 pattern: ^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
  public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$";
  public static final Long ALL_FACILITY_ID = -1L;
  public static final String ALL_FACILITY_NAME = "Global Portal";
  public static final Facility ALL_FACILITY;
  public static final Set<Long> ACCOUNT_OWNER_IDS = Set.of(1L);

  public static final String AZURE_AD_DISCOVERY_KEYS = "https://login.microsoftonline.com/%s/discovery/v2.0/keys";

  static {
    ALL_FACILITY = new Facility();
    ALL_FACILITY.setName(ALL_FACILITY_NAME).setId(ALL_FACILITY_ID);
  }

  private Misc() {
  }

  public enum ValidationPurpose {
    NA, SIGN_OFF, REGISTRATION, PASSWORD_UPDATE, ADDITIONAL_VERIFICATION, CHALLENGE_QUESTION_UPDATE,
    NOTIFY_ADMIN, CHALLENGE_QUESTION_VALIDATION
  }

  public enum NotifyAdminPurpose {
    INVITE_EXPIRED,
    PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET,
    PASSWORD_RECOVERY_ACCOUNT_LOCKED,
    PASSWORD_RECOVERY_KEY_EXPIRED
  }

  public enum HttpMethod {
    OPTIONS, GET, POST, PUT, PATCH, DELETE, NA
  }

  public enum LicenseWorkflow {
    NONE, NOTIFICATION_UNBLOCKED, NOTIFICATION_BLOCKED

  }

  public enum UserType {
    LOCAL, AZURE, OKTA, SYSTEM
  }

  public static class AzureADJsonPrivateFields {
    public static final String SECRET_ID = "secretId";
    public static final String DIRECTORY = "directory";
  }

  public static class Services {
    public static class DWI {
      public static final String ID = "c6d8285b72a84efb8fbd608c7cada484";
      public static final Set<Long> ALL_FACILITY_ROLES = Collections.unmodifiableSet(Set.of(Roles.ACCOUNT_OWNER.id(), Roles.SYSTEM_ADMIN.id(), Roles.CENTRAL_ADMIN.id()));

      public enum Roles {

        ACCOUNT_OWNER(1L),
        SYSTEM_ADMIN(3L),
        FACILITY_ADMIN(2L),
        SUPERVISOR(4L),
        OPERATOR(5L),
        CHECKLIST_PUBLISHER(6L),
        CENTRAL_ADMIN(7L);

        private final Long id;

        Roles(Long id) {
          this.id = id;
        }

        public Long id() {
          return id;
        }
      }
    }
  }

  public enum UserFields {
    EMPLOYEE_ID("employeeId", "Employee Id"),
    USERNAME("username", "Username"),
    FIRST_NAME("firstName", "First Name"),
    LAST_NAME("lastName", "Last Name"),
    EMAIL("email", "Email"),
    DEPARTMENT("department", "Department"),
    ROLES("roles", "Role"),
    FACILITIES("facilities", "Facility");

    private final String fieldName;
    private final String mappedName;

    UserFields(String fieldName, String mappedName) {
      this.fieldName = fieldName;
      this.mappedName = mappedName;
    }

    public String getFieldName() {
      return this.fieldName;
    }

    public String getMappedName() {
      return this.mappedName;
    }

    public static List<String> getFieldNames() {
      return EnumSet.allOf(UserFields.class)
        .stream()
        .map(UserFields::getFieldName)
        .collect(Collectors.toList());
    }

    public static Optional<UserFields> findByFieldName(String fieldName) {
      for (UserFields field : UserFields.values()) {
        if (field.getFieldName().equals(fieldName)) {
          return Optional.of(field);
        }
      }
      return Optional.empty();
    }
  }

}
