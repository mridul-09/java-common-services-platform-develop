package com.leucine.services.jaas.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.constant.Message;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.model.helper.search.SearchFilter;
import com.leucine.services.jaas.util.JsonUtils;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class SearchFilterFormatter implements Formatter<SearchFilter> {

  @Override
  public SearchFilter parse(String text, Locale locale) throws ParseException {
    SearchFilter searchFilter = null;
    try {
      searchFilter = JsonUtils.readValue(text, SearchFilter.class);
    } catch (JsonProcessingException ex) {
      throw new JaasException(Message.Error.ILLEGAL_FILTER, ex);
    }
    return searchFilter;
  }

  @Override
  public String print(SearchFilter object, Locale locale) {
    return object.toString();
  }
}
