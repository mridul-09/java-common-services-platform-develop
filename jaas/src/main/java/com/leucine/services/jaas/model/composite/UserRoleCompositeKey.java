package com.leucine.services.jaas.model.composite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserRoleCompositeKey implements Serializable {

  private static final long serialVersionUID = -3615198133900983294L;

  @Column(name = "users_id", columnDefinition = "bigint")
  private Long userId;

  @Column(name = "roles_id", columnDefinition = "bigint")
  private Long roleId;

  public UserRoleCompositeKey(Long userId, Long roleId) {
    this.roleId = roleId;
    this.userId = userId;
  }
}
