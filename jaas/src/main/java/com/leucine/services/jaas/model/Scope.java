package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = TableName.SCOPES, uniqueConstraints = @UniqueConstraint(columnNames = {"name", "scope_groups_id", "archived"}))
public class Scope extends AuditBaseEntity implements Serializable {
  public static final String DEFAULT_SORT = "name";
  private static final long serialVersionUID = 262860152711667404L;

  @Column(name = "name", columnDefinition = "varchar", length = 255)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "scope_groups_id", nullable = false)
  private ScopeGroup scopeGroup;

  @JsonIgnore
  @Column(name = "order_tree", columnDefinition = "int", nullable = false)
  @OrderColumn
  private Integer orderTree = 1;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;
}
