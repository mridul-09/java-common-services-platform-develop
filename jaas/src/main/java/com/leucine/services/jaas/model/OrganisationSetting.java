package com.leucine.services.jaas.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ORGANISATION_SETTINGS)
@TypeDef(name = "jsonb", typeClass = JsonNodeBinaryType.class)
public class OrganisationSetting extends AuditBaseEntity {

  private static final long serialVersionUID = -5186926456733049279L;

  @Column(name = "logo_url", columnDefinition = "varchar", length = 255)
  private String logoUrl;

  // In Minutes
  // Has to be defined between accessTokenExpirationInMinutes and refreshTokenExpirationInMinutes
  @Column(name = "session_idle_timeout", columnDefinition = "int")
  private Integer sessionIdleTimeout = 10;

  // In Minutes
  @Column(name = "registration_token_expiration", columnDefinition = "int")
  private Integer registrationTokenExpiration = 60;

  // In Minutes
  @Column(name = "password_reset_token_expiration", columnDefinition = "int")
  private Integer passwordResetTokenExpiration = 60;

  @Column(name = "max_failed_login_attempts", columnDefinition = "int")
  private Integer maxFailedLoginAttempts = 3;

  @Column(name = "max_failed_additional_verification_attempts", columnDefinition = "int")
  private Integer maxFailedAdditionalVerificationAttempts = 3;

  @Column(name = "max_failed_challenge_question_attempts", columnDefinition = "int")
  private Integer maxFailedChallengeQuestionAttempts = 3;

  // In Minutes
  @Column(name = "auto_unlock_after", columnDefinition = "int")
  private Integer autoUnlockAfter = 15;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organisations_id", nullable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb default '{}'::jsonb")
  /*
    Azure Example:
    {
      "azure": {
        "issuer": "https://login.microsoftonline.com/",
        "clientId": "12345-12345-12345-12345",
        "mappings": {
          "name": "displayName",
          "email": "mail",
          "lastName": "surname",
          "username": "userPrincipalName",
          "firstName": "givenName",
          "department": "department",
          "employeeId": "onPremisesSamAccountName"
        },
        "tenantId": "12345-12345-12345-12345",
        "redirectUri": "https://x.platform.leucinetech.com/sso/auth",
        "clientSecret": "shhhhhh! secret",
        "privateKeyPath": "",
        "postLogoutRedirectUri": "https://x.platform.leucinetech.com"
      }
    }
   */
  private JsonNode extras;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb default '{}'::jsonb")
  private JsonNode featureFlags;

  @Transient
  private Integer accessTokenExpirationInMinutes = 60;

  @Transient
  private Integer refreshTokenExpirationInMinutes = 60;
}
