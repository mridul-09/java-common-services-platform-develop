package com.leucine.services.jaas.dto.partial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PartialFacilityDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = 2760294416143995669L;
  String id;
  String name;
  String dateFormat;
  String timeFormat;
  String dateTimeFormat;
  String timeZone;
  String logoUrl;
}
