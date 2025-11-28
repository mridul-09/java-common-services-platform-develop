package com.leucine.services.jaas.model.composite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RoleScopeGroupCompositeKey implements Serializable {

  private static final long serialVersionUID = -3615198133900983294L;

  @Column(name = "roles_id", columnDefinition = "bigint")
  private Long roleId;

  @Column(name = "scope_groups_id", columnDefinition = "bigint")
  private Long scopeGroupsId;

  public RoleScopeGroupCompositeKey(Long roleId, Long scopeGroupsId) {
    this.roleId = roleId;
    this.scopeGroupsId = scopeGroupsId;
  }
}
