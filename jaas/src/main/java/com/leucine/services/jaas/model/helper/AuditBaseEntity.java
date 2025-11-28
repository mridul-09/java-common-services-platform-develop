package com.leucine.services.jaas.model.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class AuditBaseEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -6784372930267256341L;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", insertable = true, updatable = false)
  protected User createdBy;

  @Column(name = "created_by", columnDefinition = "bigint", insertable = false, updatable = false)
  protected Long createdById;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by", insertable = true, updatable = true)
  protected User modifiedBy;

  @Column(name = "modified_by", columnDefinition = "bigint", insertable = false, updatable = false)
  protected Long modifiedById;

  @JsonIgnore
  @Column(name = "created_at", columnDefinition = "bigint", insertable = true, updatable = false, nullable = false)
  protected Long createdAt;

  @JsonIgnore
  @Column(name = "modified_at", columnDefinition = "bigint", insertable = true, updatable = true, nullable = false)
  protected Long modifiedAt;

  @PrePersist
  public void prePersist() {
    createdAt = DateTimeUtils.now();
    modifiedAt = DateTimeUtils.now();
  }

  @PreUpdate
  public void preUpdate() {
    modifiedAt = DateTimeUtils.now();
  }

}