package com.leucine.services.jaas.model.composite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class OrganisationFacilityCompositeKey implements Serializable {
  private static final long serialVersionUID = -2637738888688115603L;

  @Column(name = "organisations_id", columnDefinition = "bigint")
  private Long organisationId;

  @Column(name = "facilities_id", columnDefinition = "bigint")
  private Long facilityId;

  public OrganisationFacilityCompositeKey(Long organisationId, Long facilityId) {
    this.facilityId = facilityId;
    this.organisationId = organisationId;
  }
}
