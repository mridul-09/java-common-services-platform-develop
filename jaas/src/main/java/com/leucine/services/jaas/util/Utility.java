package com.leucine.services.jaas.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

public final class Utility {
  private static final AlternativeJdkIdGenerator alternativeJdkIdGenerator;

  static {
    alternativeJdkIdGenerator = new AlternativeJdkIdGenerator();
  }

  private Utility() {
    throw new IllegalStateException("Utility class");
  }

  public static UUID getUuid(String s) {
    return UUID.fromString(s);
  }

  public static UUID generateUuid() {
    return Utility.alternativeJdkIdGenerator.generateId();
  }

  public static String generateUnique() {
    return generateUuid().toString().replace("-", "");
  }

  public static String trim(String s) {
    return s.trim();
  }

  public static String toString(Object o) {
    if (!Utility.isNull(o)) {
      return o.toString();
    }
    return "";
  }

  public static boolean isNull(Object o) {
    return Objects.isNull(o);
  }

//  public static boolean isNotNull(Object o) {
//    return Objects.nonNull(o);
//  }

//  public static boolean isEmpty(Collection<?> o) {
//    return CollectionUtils.isEmpty(o);
//  }
//
//  public static boolean isNotEmpty(Collection<?> o) {
//    return !CollectionUtils.isEmpty(o);
//  }
//
//  public static boolean isEmpty(double[] o) {
//    return isNull(o) || o.length == 0;
//  }
//
//  public static boolean isNotEmpty(double[] o) {
//    return !isEmpty(o);
//  }

  public static boolean isEmpty(Object object) {
    return object instanceof JsonNode jsonNode ? jsonNode.isEmpty() : ObjectUtils.isEmpty(object);
  }

//  public static boolean isNotEmpty(Object obj) {
//    return !isEmpty(obj);
//  }

  public static String formatId(String id) {
    if (!isNull(id)) {
      return "id" + id.replaceAll("[^a-zA-Z0-9]", "");
    }
    return null;
  }

  public static String cleanPath(String originalFilename) {
    return StringUtils.cleanPath(originalFilename);
  }

  public static String getValueFromJson(JsonNode rootNode, String compositeKey) {
    JsonNode iterateNode = rootNode;
    String[] keys = compositeKey.split("\\.");
    for (String key : keys) {
      iterateNode = iterateNode.get(key);
      if (iterateNode == null) {
        return "";
      }
    }
    return iterateNode.asText();

  }

  public static String snakeToTitleCase(String roleName) {
    if (roleName == null || roleName.isEmpty()) {
      return "";
    }
    String[] roles = roleName.toLowerCase().split("_");

    StringBuilder titleCase = new StringBuilder();
    for (String role : roles) {
      if (!role.isEmpty()) {
        titleCase.append(Character.toUpperCase(role.charAt(0)))
          .append(role.substring(1))
          .append(" ");
      }
    }
    return titleCase.toString().trim();
  }

}
