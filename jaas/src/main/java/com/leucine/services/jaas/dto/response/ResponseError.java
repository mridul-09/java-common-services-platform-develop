package com.leucine.services.jaas.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
//@Builder
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseError {
  private long timestamp = DateTimeUtils.now();
  private String code = String.valueOf(ErrorCode.UNKNOWN.code());
  private String field = null;
  private String message = null;
  private String ignore = null;

  public ResponseError setCode(int code) {
    this.code = String.valueOf(code);
    return this;
  }

  @Override
  public String toString() {
    return "{"
             + "\"timestamp\":\"" + timestamp + "\""
             + ", \"code\":\"" + code + "\""
             + ", \"field\":\"" + field + "\""
             + ", \"message\":\"" + message + "\""
             + ", \"ignore\":\"" + ignore + "\""
             + "}";
  }
}
