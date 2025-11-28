package com.leucine.services.jaas.model;

import com.leucine.services.jaas.util.UUIDGenerator;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.helper.IBaseEntity;
import com.leucine.services.jaas.util.Utility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

//indexes = @Index(columnList = "type, token")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.TOKENS, indexes = @Index(columnList = "type, token"))
public class Token implements IBaseEntity, Serializable {
  private static final long serialVersionUID = 7739237883021961233L;

  @Id
  @Column(name = "token", columnDefinition = "varchar", length = 32)
  private String token;

  @Column(name = "type", columnDefinition = "varchar", length = 32, nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Type.Token type;

  @Column(name = "users_id", columnDefinition = "bigint")
  private Long userId;

  @Column(name = "expiration", columnDefinition = "bigint")
  private Long expiration;

  @PrePersist
  public void prePersist() {
    if (Utility.isNull(token)) {
      token = UUIDGenerator.getInstance().generateUnique();
    }
  }

}
