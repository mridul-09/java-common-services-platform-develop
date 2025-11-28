package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@NamedEntityGraph(
  name = "scopeGroup-scope-entity-graph",
  attributeNodes = {
    @NamedAttributeNode(value = "scopes")
  }
)
@Entity
@Table(name = TableName.SCOPE_GROUPS, uniqueConstraints = @UniqueConstraint(columnNames = {"name", "services_id", "archived"}))
public class ScopeGroup extends AuditBaseEntity implements Serializable {
  public static final String DEFAULT_SORT = "name";
  private static final long serialVersionUID = 262860152711667404L;

  @Column(name = "name", columnDefinition = "varchar", length = 90)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "services_id")
  private Services service;

  @OneToMany(mappedBy = "scopeGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy("orderTree")
  @Where(clause = "archived = false")
  private Set<Scope> scopes = new HashSet<>();

  @JsonIgnore
  @Column(name = "order_tree", columnDefinition = "int", nullable = false)
  private Integer orderTree = 1;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;
}
