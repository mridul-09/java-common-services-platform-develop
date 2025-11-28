package com.leucine.services.jaas.model.helper.search;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SearchCriteria {
  private String field;
  private String op;
  private List<Object> values = new ArrayList<>();
}
