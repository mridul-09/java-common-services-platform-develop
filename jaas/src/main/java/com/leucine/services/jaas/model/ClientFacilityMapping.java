package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.ClientFacilityCompositeKey;
import com.leucine.services.jaas.model.helper.AuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.CLIENT_FACILITY_MAPPING, uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "facilities_id"}))

public class ClientFacilityMapping extends AuditOnlyBaseEntity {
  @EmbeddedId
  private ClientFacilityCompositeKey clientFacilityCompositeId;
}
