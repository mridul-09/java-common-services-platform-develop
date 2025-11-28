package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.composite.UserFacilityCompositeKey;
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
@Table(name = TableName.USER_FACILITIES_MAPPING)
public class UserFacilityMapping extends CreatedAuditOnlyBaseEntity {

  private static final long serialVersionUID = -7055617691258375851L;

  @EmbeddedId
  private UserFacilityCompositeKey userFacilityId;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "users_id", nullable = false, insertable = false, updatable = false)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "facilities_id", nullable = false, insertable = false, updatable = false)
  private Facility facility;

  public UserFacilityMapping(User user, Facility facility) {
    this.user = user;
    createdBy = user;
    this.facility = facility;
    userFacilityId = new UserFacilityCompositeKey(user.getId(), facility.getId());
  }
}
