package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.OrganisationFacilityCompositeKey;
import com.leucine.services.jaas.model.helper.CreatedAuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.ORGANISATION_FACILITIES_MAPPING)
public class OrganisationFacilityMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private OrganisationFacilityCompositeKey organisationFacilityId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "organisations_id", nullable = false, insertable = false, updatable = false)
  private Organisation organisation;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "facilities_id", nullable = false, insertable = false, updatable = false)
  private Facility facility;

  public OrganisationFacilityMapping(Organisation organisation, Facility facility) {
    this.organisation = organisation;
    this.facility = facility;
    organisationFacilityId = new OrganisationFacilityCompositeKey(organisation.getId(), facility.getId());
  }
}
