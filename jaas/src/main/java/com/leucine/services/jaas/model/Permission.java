package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
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
@Table(name = TableName.PERMISSIONS, uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "services_id", "archived"})})
public class Permission extends AuditBaseEntity implements Serializable {
  private static final long serialVersionUID = 262860152711667404L;

  @Column(name = "name", columnDefinition = "varchar", length = 255)
  private String name;

  @Column(name = "method", columnDefinition = "varchar", length = 15)
  @Enumerated(value = EnumType.STRING)
  private Misc.HttpMethod method;

  @Column(name = "path", columnDefinition = "text")
  private String path;

  @JsonIgnore
  @Column(name = "description", columnDefinition = "text")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "services_id")
  private Services service;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

}
