package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ValidationToken implements IBaseDto, Serializable {
  private static final long serialVersionUID = 6020303889984097680L;
  private String identifier;
  private Set<Misc.ValidationPurpose> purposes = new HashSet<>();
//  private Misc.ValidationPurpose purposes;

  public ValidationToken addPurpose(Misc.ValidationPurpose purpose) {
    if (null == purposes) {
      purposes = new HashSet<>();
    }
    purposes.add(purpose);
    return this;
  }
}
