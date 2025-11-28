package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ORGANISATIONS)
public class Organisation extends AuditBaseEntity {

  private static final long serialVersionUID = -5186926456733049279L;

  @Column(name = "name", columnDefinition = "varchar", length = 255)
  private String name;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

  @JsonIgnore
  @Column(name = "is_master", columnDefinition = "boolean", nullable = false)
  private boolean isMaster = false;

  @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
  private OrganisationSetting settings;

  @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
  private PasswordPolicy passwordPolicy;

  @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<OrganisationFacilityMapping> facilities = new HashSet<>();

  @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<OrganisationServiceMapping> services = new HashSet<>();
}
