package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.RoleScopeGroupCompositeKey;
import com.leucine.services.jaas.model.helper.CreatedAuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ROLE_SCOPE_GROUPS_MAPPING)
public class RoleScopeGroupMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private RoleScopeGroupCompositeKey roleScopeGroupId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "roles_id", nullable = false, insertable = false, updatable = false)
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "scope_groups_id", nullable = false, insertable = false, updatable = false)
  private ScopeGroup scopeGroup;

  public RoleScopeGroupMapping(Role role, ScopeGroup scopeGroup) {
    this.role = role;
    this.scopeGroup = scopeGroup;
    roleScopeGroupId = new RoleScopeGroupCompositeKey(role.getId(), scopeGroup.getId());
  }
}
