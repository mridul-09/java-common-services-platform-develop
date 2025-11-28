package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.OrganisationServiceCompositeKey;
import com.leucine.services.jaas.model.helper.CreatedAuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ORGANISATION_SERVICES_MAPPING)
public class OrganisationServiceMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private OrganisationServiceCompositeKey organisationServiceId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "organisations_id", nullable = false, insertable = false, updatable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "services_id", nullable = false, insertable = false, updatable = false)
  private Services service;

  @Column(name = "services_id", columnDefinition = "varchar", length = 255, updatable = false, insertable = false)
  private String serviceId;

  @Column(name = "fqdn", columnDefinition = "text", nullable = false)
  private String fqdn;

  public OrganisationServiceMapping(Organisation organisation, Services service) {
    this.organisation = organisation;
    this.service = service;
    organisationServiceId = new OrganisationServiceCompositeKey(organisation.getId(), service.getId());
  }
}
