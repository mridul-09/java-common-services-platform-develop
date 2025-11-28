package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.UserRoleCompositeKey;
import com.leucine.services.jaas.model.helper.CreatedAuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode
@Accessors(chain = true)
@Entity
@Table(name = TableName.USER_ROLES_MAPPING)
public class UserRoleMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private UserRoleCompositeKey userRoleId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "users_id", nullable = false, insertable = false, updatable = false)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "roles_id", nullable = false, insertable = false, updatable = false)
  private Role role;

  public UserRoleMapping(User user, Role role) {
    this.user = user;
    createdBy = user;
    this.role = role;
    userRoleId = new UserRoleCompositeKey(user.getId(), role.getId());
  }
}
