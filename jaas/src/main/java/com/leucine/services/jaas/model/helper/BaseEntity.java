package com.leucine.services.jaas.model.helper;

import com.leucine.services.jaas.util.IdGenerator;
import com.leucine.services.jaas.dto.helper.IBaseEntity;
import com.leucine.services.jaas.util.Utility;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntity implements IBaseEntity, Serializable {
  public static final String DEFAULT_SORT = "createdAt";
  private static final long serialVersionUID = -2007166319939557483L;

  @Id
  @Column(columnDefinition = "bigint", updatable = false, nullable = false)
  protected Long id;

  public String getIdAsString() {
    return String.valueOf(id);
  }

  @PrePersist
  public void prePersistsId() {
    if (Utility.isNull(id)) {
      id = IdGenerator.getInstance().nextId();
    }
  }

}
