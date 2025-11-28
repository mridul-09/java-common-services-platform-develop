package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.RoleDto;
import com.leucine.services.jaas.dto.SecuredUserDto;
import com.leucine.services.jaas.dto.UserDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialFetchUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserForAudit;
import com.leucine.services.jaas.dto.request.UserBasicInformationUpdateRequest;
import com.leucine.services.jaas.dto.request.UserUpdateRequest;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.UserRoleMapping;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {IFacilityMapper.class, IRoleMapper.class, IUserFacilityAndFacilityMapper.class})
public interface IUserMapper extends IBaseMapper<UserDto, User> {

  @Named("rolesMapper")
  static Set<RoleDto> userRolesMappingsToRoles(List<UserRoleMapping> userRoleMappings) {
    return userRoleMappings.stream().map(m -> (new RoleDto()).setId(m.getRole().getIdAsString()).setName(m.getRole().getName())).collect(Collectors.toSet());
  }

  @Override
  @Mapping(source = "facilities", target = "facilities", qualifiedByName = "userFacilityMappingsToFacilities")
  UserDto toDto(User entity);

  @Named("toSecuredDto")
  @Mapping(source = "facilities", target = "facilities", qualifiedByName = "userFacilityMappingsToFacilities")
  SecuredUserDto toSecuredDto(User entity);

  PartialUserDto toPartialDto(User entity);

  PartialFetchUserDto toPartialFetchDto(User entity);

  List<PartialUserDto> toPartialDto(List<User> list);

  List<PartialFetchUserDto> toPartialFetchDto(List<User> list);

  @Mapping(source = "organisation.id", target = "organisationId")
  @Mapping(source = "id", target = "idAsString")
  @Mapping(source = "roles", target = "roleNames")
  PrincipalUser toPrincipalUser(User entity);

  @Mapping(source = "organisation.id", target = "organisationId")
  PartialUserForAudit toPartialUserForAudit(User entity);

  @Mapping(source = "facilities", target = "facilities", ignore = true)
  @Mapping(source = "roles", target = "roles", ignore = true)
  @Mapping(source = "email", target = "email", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(UserUpdateRequest userUpdateRequest, @MappingTarget User entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "email", target = "email", ignore = true)
  void update(UserBasicInformationUpdateRequest userBasicInformationUpdateRequest, @MappingTarget User entity);

  @AfterMapping
  default void handlePrivateFields(User user, @MappingTarget UserDto dto) {
    if (!State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      dto.setUsername("");
    }
  }

  @AfterMapping
  default void handlePrivateFields(User user, @MappingTarget PrincipalUser principalUser) {
    if (!State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      principalUser.setUsername("");
    }
  }

}
