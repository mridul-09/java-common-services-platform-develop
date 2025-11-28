package com.leucine.services.jaas.model.helper.search;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchFilter {
  List<SearchCriteria> fields;
  private String op;
}
