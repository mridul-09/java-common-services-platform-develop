package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ROLES, uniqueConstraints = @UniqueConstraint(columnNames = {"name", "services_id", "archived"}))
public class Role extends AuditBaseEntity implements Serializable {
  public static final String DEFAULT_SORT = "name";
  private static final long serialVersionUID = 262860152711667404L;

  @Column(name = "name", columnDefinition = "varchar", length = 90)
  @OrderColumn
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "services_id")
  private Services service;

  @Column(name = "services_id", columnDefinition = "varchar", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.StringType")
  private String serviceId;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @JoinTable(name = TableName.ROLE_SCOPE_GROUPS_MAPPING,
    joinColumns = @JoinColumn(name = "roles_id"),
    inverseJoinColumns = @JoinColumn(name = "scope_groups_id")
  )
  @OrderBy("orderTree")
  @Where(clause = "archived = false")
  private Set<ScopeGroup> scopeGroups = new HashSet<>();

  public Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
