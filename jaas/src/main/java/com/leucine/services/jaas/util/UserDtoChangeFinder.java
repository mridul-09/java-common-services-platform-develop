package com.leucine.services.jaas.util;

import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.UserChangeDto;
import com.leucine.services.jaas.dto.UserDto;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import org.apache.commons.text.CaseUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDtoChangeFinder {

  public static List<UserChangeDto> findChanges(UserDto oldDto, UserDto newDto) {
    final List<String> fieldsToCheck = Misc.UserFields.getFieldNames();
    List<UserChangeDto> changes = new ArrayList<>();
    if (oldDto == null || newDto == null) {
      throw new IllegalArgumentException("DTOs must not be null");
    }
    compareFields(oldDto, newDto, changes, fieldsToCheck);
    return changes;
  }

  private static void compareFields(Object oldDto, Object newDto, List<UserChangeDto> changes, List<String> fieldsToCheck) {
    if (oldDto == null || newDto == null) {
      throw new IllegalArgumentException("DTOs must not be null");
    }

    for (String fieldName : fieldsToCheck) {
      try {
        Field field = UserDto.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Object oldValue = field.get(oldDto);
        Object newValue = field.get(newDto);

        oldValue = processFieldValue(oldValue, fieldName);
        newValue = processFieldValue(newValue, fieldName);

        if (!Objects.equals(oldValue, newValue)) {
          UserChangeDto change = UserChangeDto.builder().field(Misc.UserFields.findByFieldName(fieldName).get().getMappedName()).oldValue(oldValue.toString()).newValue(newValue.toString()).build();
          changes.add(change);
        }

      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException("Error accessing field: " + fieldName, e);
      }
    }
  }

  private static Object processFieldValue(Object value, String fieldName) {
    if (isSpecialField(fieldName)) {
      return processSpecialField(value, fieldName);
    }
    return (value == null) ? "" : value;
  }

  private static boolean isSpecialField(String fieldName) {
    return fieldName.equals("roles") || fieldName.equals("facilities");
  }

  private static Object processSpecialField(Object fieldValue, String fieldName) {
    if (fieldValue == null) {
      return null;
    }

    if (fieldName.equals("roles")) {
      List<PartialRoleDto> roles = (List<PartialRoleDto>) fieldValue;
      return roles.stream()
        .map(role -> titleCase(role.getName()))
        .collect(Collectors.toList());
    } else if (fieldName.equals("facilities")) {
      List<PartialFacilityDto> facilities = (List<PartialFacilityDto>) fieldValue;
      return facilities.stream()
        .map(facility -> titleCase(facility.getName()))
        .collect(Collectors.toList());
    }

    return fieldValue;
  }

  private static String titleCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    return CaseUtils.toCamelCase(input.toLowerCase(), true, '_');
  }
}
