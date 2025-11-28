package com.leucine.services.jaas.model.helper;

import com.leucine.services.jaas.constant.Message;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.model.helper.search.SearchCriteria;
import com.leucine.services.jaas.model.helper.search.SearchFilter;
import com.leucine.services.jaas.model.helper.search.SearchOperator;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.Utility;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecificationBuilder<T> {
  private static final String INVALID_SEARCH_OPERATION = "Invalid filter search operation : {0}";
  private static final String INVALID_FILTER_CONDITION = "Invalid filter condition : {0}";
  private static final String WILDCARD_PERCENT = "%";

  public static SearchCriteria createSearchCriteria(String field, SearchOperator operator, List<Object> values) {
    return (new SearchCriteria()).setField(field).setOp(operator.toString()).setValues(values);
  }

  public static SearchCriteria createSearchCriteria(String field, SearchOperator operator, Object values) {
    return SpecificationBuilder.createSearchCriteria(field, operator, Collections.singletonList(values));
  }

  public static <E extends BaseEntity> Specification<E> createSpecification(String filters) {
    SpecificationBuilder<E> specificationBuilder = new SpecificationBuilder<>();
    Specification<E> specification = null;
    if (!Utility.isEmpty(filters)) {
      try {
        SearchFilter searchFilter = JsonUtils.readValue(URLDecoder.decode(filters, StandardCharsets.UTF_8), SearchFilter.class);
        specification = specificationBuilder.createSpecification(searchFilter.getFields(), searchFilter.getOp());
      } catch (Exception exception) {
        throw new JaasException(Message.Error.ILLEGAL_FILTER);
      }
    }
    return specification;
  }

  public static <E extends BaseEntity> Specification<E> createSpecification(String filters, List<SearchCriteria> mandatorySearchCriterias) {
    SpecificationBuilder<E> specificationBuilder = new SpecificationBuilder<>();
    Specification<E> specification = null;
    if (!Utility.isEmpty(filters)) {
      try {
        SearchFilter searchFilter = JsonUtils.readValue(URLDecoder.decode(filters, StandardCharsets.UTF_8.toString()), SearchFilter.class);
        if (!Utility.isEmpty(mandatorySearchCriterias)) {
          Specification<E> mandatorySpecification = specificationBuilder.createSpecification(mandatorySearchCriterias, SearchOperator.AND.toString());
          Specification<E> filtersSpecification = specificationBuilder.createSpecification(searchFilter.getFields(), searchFilter.getOp());
          specification = mandatorySpecification.and(filtersSpecification);
        } else {
          specification = specificationBuilder.createSpecification(searchFilter.getFields(), searchFilter.getOp());
        }
      } catch (Exception exception) {
        throw new JaasException(Message.Error.ILLEGAL_FILTER);
      }
    } else {
      specification = specificationBuilder.createSpecification(mandatorySearchCriterias, SearchOperator.AND.toString());
    }
    return specification;
  }

  public Specification<T> createSpecification(List<SearchCriteria> searchCriteria, String builderCondition) {
    return (root, query, criteriaBuilder) -> buildSpecification(searchCriteria, root, query, criteriaBuilder, builderCondition);
  }

  private Predicate buildSpecification(List<SearchCriteria> searchCriteriaList, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, String builderCondition) {
    query.distinct(true);
    List<Predicate> predicates = new ArrayList<>();

    for (SearchCriteria searchCriteria : searchCriteriaList) {
      if (searchCriteria != null && !Utility.isEmpty(searchCriteria.getValues())) {
        generatePredicate(root, criteriaBuilder, predicates, searchCriteria);
      }
    }
    switch (SearchOperator.valueOf(builderCondition)) {
      case AND:
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      case OR:
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
      default:
        throw new IllegalArgumentException(MessageFormat.format(INVALID_FILTER_CONDITION, builderCondition));
    }
  }

  private void generatePredicate(Root<T> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, SearchCriteria searchCriteria) {
    List<Object> list = searchCriteria.getValues();
    String field = searchCriteria.getField();
    String operator = searchCriteria.getOp();
    SearchOperator searchOperation = SearchOperator.valueOf(operator);
    Expression expression = null;
    if (field.contains(".")) {
      String[] fields = field.split("\\.");
      Join<Object, Object> rootJoin = root.join(fields[0]);
      int i = 1;
      for (; i < fields.length - 1; i++) {
        rootJoin = rootJoin.join(fields[i]);
      }
      expression = rootJoin.get(fields[i]);
    }

    expression = expression == null ? root.get(field) : expression;
    if (expression.getJavaType().isArray()) {
        /*
          select distinct count(distinct emailaudit0_.id) as col_0_0_
          from email_audits emailaudit0_
          where arraycontains(emailaudit0_.to_addresses,string_to_array('sathyam.lokare+all@leucinetech.com',','))=true

        Expression<String> exp = criteriaBuilder.function("string_to_array", String.class, criteriaBuilder.literal(list.get(0).toString()), criteriaBuilder.literal(","));
        predicates.add(criteriaBuilder.isTrue(criteriaBuilder.function("arraycontains", Boolean.class, root.get(field), exp)));
         */

//      Expression<Long> exp = criteriaBuilder.function("array", Long.class, criteriaBuilder.literal(list.get(0).toString() + "::bigint"));
//      predicates.add(criteriaBuilder.isTrue(criteriaBuilder.function("arraycontains", Boolean.class, root.get(field), exp)));
      Expression<String> exp = criteriaBuilder.function("string_to_array", String.class, criteriaBuilder.literal(list.get(0).toString()), criteriaBuilder.literal(","));
      predicates.add(criteriaBuilder.isTrue(criteriaBuilder.function("arraycontains", Boolean.class, root.get(field), exp)));
//      predicates.add();
    } else {
      switch (searchOperation) {
        case EQ:
          predicates.add(criteriaBuilder.equal(expression, list.get(0)));
          break;
        case ANY:
        case IN:
        case NOT_IN:
        case NEITHER:
          Expression<String> exp = expression;
          exp = handleEnumConversion(exp);
          Predicate predicate = exp.in(list);
          switch (searchOperation) {
            case ANY:
            case IN:
              predicates.add(predicate);
              break;
            case NOT_IN:
            case NEITHER:
              predicates.add(predicate.not());
              break;
            default:
              break;
          }
          break;
        case LT:
          predicates.add(criteriaBuilder.lessThan(expression, list.get(0).toString()));
          break;
        case GT:
          predicates.add(criteriaBuilder.greaterThan(expression, list.get(0).toString()));
          break;
        case LIKE:
          predicates.add(criteriaBuilder.like(criteriaBuilder.lower(expression), WILDCARD_PERCENT + list.get(0).toString().toLowerCase() + WILDCARD_PERCENT));
          break;
        case LOE:
          predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, list.get(0).toString()));
          break;
        case GOE:
          predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, list.get(0).toString()));
          break;
        case NE:
          predicates.add(criteriaBuilder.notEqual(expression, list.get(0)));
          break;
        default:
          throw new IllegalArgumentException(MessageFormat.format(INVALID_SEARCH_OPERATION, operator));
      }
    }
  }

  private Expression<String> handleEnumConversion(Expression<String> exp) {
    if (exp.getJavaType().isEnum()) {
      exp = exp.as(String.class);
    }
    return exp;
  }
}

