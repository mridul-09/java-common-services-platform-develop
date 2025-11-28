package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.BaseEntity;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.Utility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.PASSWORD_HISTORY, indexes = @Index(columnList = "users_id"))
public class PasswordHistory extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -2471460703879092625L;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", columnDefinition = "bigint")
  protected User user;

  @Column(name = "users_id", columnDefinition = "bigint", insertable = false, updatable = false)
  private Long userId;

  @Column(name = "password", columnDefinition = "text")
  private String password;

  @JsonIgnore
  @Column(name = "created_at", columnDefinition = "bigint", nullable = false, updatable = false)
  @OrderColumn
  private Long createdAt;

  @PrePersist
  public void prePersist() {
    createdAt = DateTimeUtils.now();
  }

  public PasswordHistory setPassword(String password) {
    if (!Utility.isNull(password)) {
      this.password = User.getEncoder().encode(password);
    }
    return this;
  }
}
