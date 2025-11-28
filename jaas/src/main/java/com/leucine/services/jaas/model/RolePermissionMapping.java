package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.RolePermissionCompositeKey;
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
@Table(name = TableName.ROLE_PERMISSIONS_MAPPING)
@NamedEntityGraph(name = "role-permission-entity-graph",
  attributeNodes = {
    @NamedAttributeNode(value = "permission")
  }
)
public class RolePermissionMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private RolePermissionCompositeKey rolePermissionId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "roles_id", nullable = false, insertable = false, updatable = false)
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "permissions_id", nullable = false, insertable = false, updatable = false)
  private Permission permission;

  public RolePermissionMapping(Role role, Permission permission) {
    this.role = role;
    this.permission = permission;
    rolePermissionId = new RolePermissionCompositeKey(role.getId(), permission.getId());
  }
}
