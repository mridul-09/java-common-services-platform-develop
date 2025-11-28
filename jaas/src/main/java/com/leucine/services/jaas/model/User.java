package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.Utility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@NamedEntityGraphs({
  @NamedEntityGraph(
    name = "user.facility",
    attributeNodes = {
      @NamedAttributeNode(value = "facilities", subgraph = "facilities"),
    },
    subgraphs = {
      @NamedSubgraph(name = "facilities", attributeNodes = {
        @NamedAttributeNode(value = "facility")
      }),
    }
  ),
})
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.USERS, uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}),
  @UniqueConstraint(columnNames = {"username"}),
  @UniqueConstraint(columnNames = {"employee_id", "organisations_id", "archived"}),
})
public class User extends AuditBaseEntity implements UserDetails, Cloneable, Serializable {
  public static final Long SYSTEM_USER_ID = 1L;
  public static final String SORT_DEFAULT = "createdAt";
  public static final String SORT_FIRST_NAME = "firstName";
  public static final String SORT_LAST_NAME = "lastName";
  public static final String ORGANISATION_ID = "organisationId";
  public static final String SERVICE_ID = "organisation.services.service.id";
  public static final String IS_SYSTEM_USER = "isSystemUser";
  public static final String ARCHIVED = "archived";
  public static final String STATE = "state";
  public static final String ROLE_NAME = "roles.role.name";
  public static final String FACILITY_ID = "facilities.facility.id";
  public static final String FIRST_NAME = "firstName";
  public static final String ID = "id";

  private static final long serialVersionUID = 4711899714395819508L;
  private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Column(name = "username", columnDefinition = "varchar", length = 255, nullable = false)
  private String username;

  @Column(name = "employee_id", columnDefinition = "varchar", length = 45)
  private String employeeId;

  @Column(name = "first_name", columnDefinition = "varchar", length = 255)
  @OrderBy
  private String firstName;

  @Column(name = "last_name", columnDefinition = "varchar", length = 255)
  private String lastName;

  @Column(name = "email", columnDefinition = "varchar", length = 255)
  private String email;

  @Column(name = "state", columnDefinition = "varchar", length = 50, nullable = false)
  @Enumerated(EnumType.STRING)
  private State.User state;

  @Column(name = "department", columnDefinition = "varchar", length = 255)
  private String department;

  @Column(name = "password", columnDefinition = "text")
  private String password;

  @JsonIgnore
  @Column(name = "failed_login_attempts", columnDefinition = "int", nullable = true, insertable = true, updatable = true)
  private Integer failedLoginAttempts;

  @JsonIgnore
  @Column(name = "failed_additional_verification_attempts", columnDefinition = "int", nullable = true, insertable = true, updatable = true)
  private Integer failedAdditionalVerificationAttempts;

  @JsonIgnore
  @Column(name = "failed_challenge_question_attempts", columnDefinition = "int", nullable = true, insertable = true, updatable = true)
  private Integer failedChallengeQuestionAttempts;

  //TODO Track token reset attempts and disallow if exceeds beyond 3 attempts for 1 hour

  @JsonIgnore
  @Column(name = "password_updated_at", columnDefinition = "bigint", nullable = true, insertable = true, updatable = true)
  private Long passwordUpdatedAt;

  @JsonIgnore
  @Column(name = "locked_at", columnDefinition = "bigint", nullable = true, insertable = true, updatable = true)
  private Long lockedAt;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

  @JsonIgnore
  @Column(name = "is_system_user", columnDefinition = "boolean")
  private boolean isSystemUser = false;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "organisations_id", referencedColumnName = "id", nullable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

  @Column(name = "type", columnDefinition = "varchar", updatable = false)
  @Enumerated(EnumType.STRING)
  private Misc.UserType userType;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserFacilityMapping> facilities = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserRoleMapping> roles = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "challenge_questions_id", referencedColumnName = "id", nullable = false)
  private ChallengeQuestion challengeQuestion;

  @Column(name = "challenge_questions_answer", columnDefinition = "varchar", length = 255)
  private String challengeQuestionsAnswer;

  @Transient
  private String serviceId;

  @Transient
  private List<Facility> myMappedFacilities;

  @Transient
  private Long currentFacilityId;

  @Transient
  private String token;

  public static boolean validatePassword(String plainText, String encrypted) {
    return bCryptPasswordEncoder.matches(plainText, encrypted);
  }

  public static BCryptPasswordEncoder getEncoder() {
    return bCryptPasswordEncoder;
  }

  public User setPassword(String password) {
    if (!Utility.isNull(password)) {
      this.password = bCryptPasswordEncoder.encode(password);
      passwordUpdatedAt = DateTimeUtils.now();
    }
    return this;
  }

  public boolean isHasSetChallengeQuestion() {
    //TODO: Optimise user type as it is Azure AD specific
    return challengeQuestion != null || userType != Misc.UserType.LOCAL;
  }

  public List<Facility> getFacilities() {
    return facilities.stream()
      .sorted(Comparator.comparing(m -> m.getFacility().getName()))
      .map(userFacilityMapping -> new Facility(
        userFacilityMapping.getFacility().getId(),
        userFacilityMapping.getFacility().getName(),
        userFacilityMapping.getFacility().getDateFormat(),
        userFacilityMapping.getFacility().getTimeFormat(),
        userFacilityMapping.getFacility().getDateTimeFormat(),
        userFacilityMapping.getFacility().getTimeZone(),
        userFacilityMapping.getFacility().getLogoUrl()
      ))
      .collect(Collectors.toList());
  }

  public void setFacilities(List<Facility> facilities) {
    this.facilities.clear();
    this.facilities.addAll(facilities.stream().map(facility -> new UserFacilityMapping(this, facility)).collect(Collectors.toSet()));
  }

  public String[] getFacilityIds() {
    return facilities.stream().map(userFacilityMapping -> userFacilityMapping.getFacility().getIdAsString()).toArray(String[]::new);
  }

  public Set<UserFacilityMapping> getUserFacilityMapping() {
    return facilities;
  }

  public Set<UserRoleMapping> getUserRoleMapping() {
    return roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<>();
    getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
    return authorities;
  }

  public Set<Role> getRoles() {
    return roles.stream().map(userRoleMapping -> new Role(userRoleMapping.getRole().getId(), userRoleMapping.getRole().getName())).collect(Collectors.toSet());
  }

  public void setRoles(List<Role> roles) {
    this.roles.clear();
    this.roles.addAll(roles.stream().map(role -> new UserRoleMapping(this, role)).collect(Collectors.toList()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  @PrePersist
  public void prePersist() {
    isSystemUser = false;
    if (!Utility.isNull(password)) {
      password = User.bCryptPasswordEncoder.encode(password);
    }
    createdAt = DateTimeUtils.now();
    modifiedAt = DateTimeUtils.now();
    if (!Utility.isNull(username)) {
      username = username.toLowerCase();
    }
    if (!Utility.isNull(email)) {
      email = email.toLowerCase();
    }
  }

  @Override
  @PreUpdate
  public void preUpdate() {
    modifiedAt = DateTimeUtils.now();
    if (!Utility.isNull(username)) {
      username = username.toLowerCase();
    }
    if (!Utility.isNull(email)) {
      email = email.toLowerCase();
    }
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}
