package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.PASSWORD_POLICIES)
public class PasswordPolicy extends AuditBaseEntity implements Serializable {

  private static final long serialVersionUID = -8437682673075270070L;

  @Column(name = "minimum_password_length", columnDefinition = "int")
  private Integer minimumPasswordLength = 8;


  @Column(name = "minimum_lowercase_characters", columnDefinition = "int")
  private Integer minimumLowercaseCharacters = 1;


  @Column(name = "minimum_uppercase_characters", columnDefinition = "int")
  private Integer minimumUppercaseCharacters = 1;


  @Column(name = "minimum_special_characters", columnDefinition = "int")
  private Integer minimumSpecialCharacters = 1;


  @Column(name = "minimum_numeric_characters", columnDefinition = "int")
  private Integer minimumNumericCharacters = 1;


  @Column(name = "allow_password_similar_to_username_or_email", columnDefinition = "boolean")
  private boolean allowPasswordSimilarToUsernameOrEmail = false;

  // In Days
  @Column(name = "maximum_password_age", columnDefinition = "int")
  private Integer maximumPasswordAge = 90;

  // Determines how often an old password can be reused
  @Column(name = "minimum_password_history", columnDefinition = "int")
  private Integer minimumPasswordHistory = 10;

  // In Days
  @Column(name = "password_expiration", columnDefinition = "int")
  private Integer passwordExpiration = 1;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organisations_id", nullable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

}
