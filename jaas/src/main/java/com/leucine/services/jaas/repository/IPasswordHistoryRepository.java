package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.PasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPasswordHistoryRepository extends CrudRepository<PasswordHistory, Long> {
  //  List<PasswordHistory> findAllByUserId(Long userId, Page page);
  List<PasswordHistory> findAllByUserId(Long userId, Pageable page);

}
