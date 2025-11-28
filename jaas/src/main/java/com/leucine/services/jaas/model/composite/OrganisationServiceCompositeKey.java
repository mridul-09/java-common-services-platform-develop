package com.leucine.services.jaas.model.composite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class OrganisationServiceCompositeKey implements Serializable {
  private static final long serialVersionUID = -2637738888688115603L;

  @Column(name = "organisations_id")
  private Long organisationId;

  @Column(name = "services_id")
  private String serviceId;

  public OrganisationServiceCompositeKey(Long organisationId, String serviceId) {
    this.serviceId = serviceId;
    this.organisationId = organisationId;
  }
}
