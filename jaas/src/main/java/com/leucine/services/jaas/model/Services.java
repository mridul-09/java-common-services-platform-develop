package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.util.UUIDGenerator;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.dto.helper.IBaseEntity;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.Utility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@NamedEntityGraph(
  name = "service-role-scopeGroup-scope-entity-graph",
  attributeNodes = {
    @NamedAttributeNode(value = "roles", subgraph = "roles")
  },
  subgraphs = {
    @NamedSubgraph(name = "roles", attributeNodes = {
      @NamedAttributeNode(value = "scopeGroups", subgraph = "scopeGroups.scope")
    }),
    @NamedSubgraph(name = "scopeGroups.scope", attributeNodes = {
      @NamedAttributeNode(value = "scopes")
    }),
  }
)
@Entity
@Table(name = TableName.SERVICES)
public class Services implements IBaseEntity, Serializable {

  private static final long serialVersionUID = -6414442617781012580L;

  @Id
  @Column(nullable = false, insertable = false, updatable = false, unique = true)
  protected String id;

  @Column(columnDefinition = "varchar", length = 45, insertable = false, updatable = false, unique = true)
  private String errorCodeRange;

  @Column(length = 45, unique = true)
  private String name;

  @Column(columnDefinition = "text")
  private String registrationPath;

  @Column(columnDefinition = "text")
  private String resetPasswordPath;

  @Column(columnDefinition = "text")
  private String loginPath;

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Role> roles = new HashSet<>();

  @JsonIgnore
  @Column(nullable = false)
  private boolean archived = false;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", insertable = true, updatable = false)
  private User createdBy;

  @Column(name = "created_by", insertable = false, updatable = false)
  private Long createdById;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by", insertable = true, updatable = true)
  private User modifiedBy;

  @Column(name = "modified_by", insertable = false, updatable = false)
  private Long modifiedById;

  @JsonIgnore
  @Column(nullable = false, insertable = true, updatable = false)
  private Long createdAt;

  @JsonIgnore
  @Column(nullable = false, insertable = true, updatable = true)
  private Long modifiedAt;

  @PrePersist
  public void prePersist() {
    createdAt = DateTimeUtils.now();
    modifiedAt = DateTimeUtils.now();
    if (Utility.isNull(id)) {
      id = UUIDGenerator.getInstance().generateUnique();
    }
  }

  @PreUpdate
  public void preUpdate() {
    modifiedAt = DateTimeUtils.now();
  }

}
