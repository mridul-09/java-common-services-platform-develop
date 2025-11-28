package com.leucine.services.jaas.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.constant.Message;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class Response<T> {
  private Type.ObjectType object;
  private HttpStatus status;
  private String message;
  private String token;
  private long timestamp;
  private T data;
  private PageMetadata pageable;
  private List<ResponseError> errors;

  @Override
  public String toString() {
    return "{"
      + "\"object\":\"" + object + "\""
      + ", \"status\":\"" + status + "\""
      + ", \"message\":\"" + message + "\""
      + ", \"timestamp\":\"" + timestamp + "\""
      + ", \"data\":" + data
      + ", \"errors\":" + errors
      + "}";
  }

  public static class ResponseBuilder<T> {
    private T data;
    private Type.ObjectType object = Type.ObjectType.OBJECT;
    private HttpStatus status = HttpStatus.OK;
    private String message = Message.SUCCESS;
    private long timestamp = DateTimeUtils.now();

    @Override
    public String toString() {
      return "{"
        + "\"data\":" + data
        + ", \"object\":\"" + object + "\""
        + ", \"status\":\"" + status + "\""
        + ", \"message\":\"" + message + "\""
        + ", \"timestamp\":\"" + timestamp + "\""
        + ", \"pageable\":" + pageable
        + ", \"errors\":" + errors
        + "}";
    }

    public ResponseBuilder data(T data) {
      this.data = data;
      if (data instanceof PageImpl) {
        PageImpl<T> pageImpl = (PageImpl) data;
        PageMetadata pageMetadata = PageMetadata.builder()
          .page(pageImpl.getNumber())
          .pageSize(pageImpl.getSize())
          .numberOfElements(pageImpl.getNumberOfElements())
          .totalPages(pageImpl.getTotalPages())
          .totalElements(pageImpl.getTotalElements())
          .first(pageImpl.isFirst())
          .last(pageImpl.isLast())
          .empty(pageImpl.isEmpty()).build();
        pageable = pageMetadata;
        this.data = (T) pageImpl.getContent();
        object = Type.ObjectType.LIST;
      }
      return this;
    }
  }

  @Builder
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class PageMetadata {
    private int page;
    private int pageSize;
    private long numberOfElements;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private boolean empty;
  }

}