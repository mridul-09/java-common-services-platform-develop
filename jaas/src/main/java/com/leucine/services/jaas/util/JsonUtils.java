package com.leucine.services.jaas.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.exception.InvalidTokenException;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;

public final class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JsonUtils() {
  }

  public static <T> T jsonToCollectionType(String data, Class<? extends Collection> collection, Class<?> type) throws IOException {
    return (T) objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(collection, type));
  }

  public static JsonNode valueToNode(Object value) {
    return objectMapper.valueToTree(value);
  }

  public static JsonNode valueToNode(String content) throws JsonProcessingException {
    return objectMapper.readTree(content);
  }

  public static <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
    return objectMapper.readValue(content, valueType);
  }
  public static String getKID(String token) throws JsonProcessingException {
    String[] chunks = token.split("\\.");
    String headers = new String(Base64.getDecoder().decode(chunks[0]));
    JsonNode toJsonHeader = objectMapper.readTree(headers);
    var kid = toJsonHeader.get("preferred_username").asText();
    if (Utility.isEmpty(kid)) {
      throw new InvalidTokenException(ErrorCode.INVALID_ID_TOKEN_MISSING_KID);
    }
    return kid;
  }

  public static JsonNode removeUnwantedFieldIfExists(JsonNode extras) {
    var mutableJsonNode = (ObjectNode) extras;
    if(extras.get(Misc.AzureADJsonPrivateFields.SECRET_ID) != null && extras.get(Misc.AzureADJsonPrivateFields.DIRECTORY) != null) {
      mutableJsonNode.remove(Misc.AzureADJsonPrivateFields.SECRET_ID);
      mutableJsonNode.remove(Misc.AzureADJsonPrivateFields.DIRECTORY);
    }
    return mutableJsonNode;
  }

  public static String getPreferredUsername(String token, String usernameKey) throws JsonProcessingException {
    String[] chunks = token.split("\\.");
    String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
    JsonNode toJsonPayload = objectMapper.readTree(payload);
    var username = toJsonPayload.get(usernameKey).asText();
    if (Utility.isEmpty(username)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    return username;
  }

  public static String writeValueAsString(Object value) throws JsonProcessingException {
    return objectMapper.writeValueAsString(value);
  }

  public static JsonNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

  public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws IllegalArgumentException {
    return objectMapper.convertValue(fromValue, toValueTypeRef);
  }

}
