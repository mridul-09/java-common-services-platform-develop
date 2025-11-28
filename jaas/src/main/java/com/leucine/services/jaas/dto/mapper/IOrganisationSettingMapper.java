package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.OrganisationSettingDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialOrganisationSettingDto;
import com.leucine.services.jaas.model.OrganisationSetting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IOrganisationSettingMapper extends IBaseMapper<OrganisationSettingDto, OrganisationSetting> {

  //    @Mapping(source = "accessTokenExpiration", target = "accessTokenExpirationInMinutes")
//  @Mapping(source = "refreshTokenExpiration", target = "refreshTokenExpirationInMinutes")
  @Mapping(source = "entity.sessionIdleTimeout", target = "sessionIdleTimeoutInMinutes")
  PartialOrganisationSettingDto toPartialDto(OrganisationSetting entity, Misc.UserType ssoType);
}
