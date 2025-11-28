package com.leucine.services.jaas.model.composite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RolePermissionCompositeKey implements Serializable {

  private static final long serialVersionUID = -3615198133900983294L;

  @Column(name = "roles_id", columnDefinition = "bigint")
  private Long roleId;

  @Column(name = "permissions_id", columnDefinition = "bigint")
  private Long permissionId;

  public RolePermissionCompositeKey(Long roleId, Long permissionId) {
    this.roleId = roleId;
    this.permissionId = permissionId;
  }
}
