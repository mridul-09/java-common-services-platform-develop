package com.leucine.services.jaas.constant;

public final class Query {

  //  public static final String GET_PASSWORD_TOKEN_BY_USER_ID = "select p from PasswordResetToken p inner join fetch p.user where p.user.id=?1";
  public static final String GET_ORGANISATION_SETTINGS = "SELECT os FROM OrganisationSetting os INNER JOIN Organisation o ON o.id = os.organisation WHERE o.archived = false";
  public static final String GET_PASSWORD_POLICY = "SELECT pp FROM PasswordPolicy pp INNER JOIN Organisation o ON o.id = pp.organisation WHERE o.archived = false";
  public static final String GET_USERS_WHOSE_LAST_UPDATED_PASSWORD_HAS_EXPIRED = "SELECT u FROM User u WHERE u.archived = false AND u.state != 'PASSWORD_EXPIRED' AND " +
    "u.organisationId = :organisationId AND ((u.passwordUpdatedAt + :offsetForPasswordUpdatedAtCalculation) < :now )";
  public static final String GET_USERS_WHOSE_LOCKED_AT_HAS_EXCEEDED_AUTO_UNLOCK = "SELECT u FROM User u WHERE u.archived = false AND u.organisationId = :organisationId AND " +
    "((u.lockedAt + :offsetForLockedAtCalculation) < :now) AND u.state IN ('UNREGISTERED_LOCKED', 'REGISTERED_LOCKED')";
  public static final String GET_USERS_BY_ROLES =
    "SELECT u FROM User u INNER JOIN UserRoleMapping urm on u.id = urm.user INNER JOIN Role r on r.id = urm.role WHERE u.archived = false AND u.organisationId = :organisationId " +
      "AND u.isSystemUser = false AND r.serviceId = :serviceId AND r.name in (:roles)";
  public static final String GET_ACTIVE_USERS_BY_ORGANISATION_ID_AND_ROLE_ID = "SELECT first_name AS firstName, last_name AS lastName, employee_id AS employeeId, email AS email FROM users u JOIN user_roles_mapping urm ON urm.users_id = u.id WHERE u.organisations_id = :organisationId AND urm.roles_id = :roleId AND u.archived = false AND state IN ('REGISTERED', 'PASSWORD_EXPIRED')";
  public static final String GET_TOKENS_THAT_HAVE_EXPIRED_AFTER_OFFSET = "SELECT t FROM Token t WHERE (t.expiration + :offsetForExpirationCalculation) < :now";
  public static final String GET_ALL_EXPIRED_REGISTRATION_TOKEN = "SELECT t FROM Token t WHERE t.expiration < :now AND type = 'REGISTRATION'";
  public static final String UPDATE_USER_STATE = "UPDATE User u SET u.state = :state WHERE u.id IN :userIds";
  public static final String GET_LICENSE_BY_FACILITY_IDS = "SELECT l FROM License l WHERE l.facilityId IN :facilityIds AND l.archived = false";
  public static final String FIND_ORGANISATION_ID_BY_FQDN = "select osm.organisations_id from organisation_services_mapping osm where osm.fqdn ilike %:fqdn limit 1";

  public static final String FIND_ROLE_NAME_BY_EMPLOYEE_ID = """     
    SELECT r.name
    FROM roles r
    JOIN user_roles_mapping urm ON r.id = urm.roles_id
    JOIN users u ON urm.users_id = u.id
    WHERE u.employee_id = :employeeId
    LIMIT 1
""";
  private Query() {
  }

}
