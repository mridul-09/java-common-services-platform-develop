package com.leucine.services.jaas.model.composite;

import com.leucine.services.jaas.model.ClientCredential;
import com.leucine.services.jaas.model.Facility;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ClientFacilityCompositeKey implements Serializable {
  @Serial
  private static final long serialVersionUID = 2306194029815289888L;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private ClientCredential clientCredential;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "facilities_id", nullable = false, insertable = false, updatable = false)
  private Facility facility;

  public ClientFacilityCompositeKey(ClientCredential clientId, Facility facility) {
    this.clientCredential = clientId;
    this.facility = facility;
  }
}
