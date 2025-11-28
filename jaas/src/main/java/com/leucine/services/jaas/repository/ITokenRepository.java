package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.model.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends CrudRepository<Token, String> {

  Optional<Token> findByTokenAndType(String token, Type.Token type);

  Optional<Token> findByUserId(Long userId);

  void deleteByToken(String token);

  long deleteByUserIdAndType(Long userId, Type.Token type);

//  List<Token> deleteByUserIdInAndType(Set<Long> userIds, Type.Token type);

  boolean existsByTokenAndType(String token, Type.Token type);

  boolean existsByToken(String token);

  @Query(com.leucine.services.jaas.constant.Query.GET_TOKENS_THAT_HAVE_EXPIRED_AFTER_OFFSET)
  List<Token> getAllExpiredTokenAfterOffset(@Param("offsetForExpirationCalculation") Long offsetForExpirationCalculation, @Param("now") Long now);

  @Query(com.leucine.services.jaas.constant.Query.GET_ALL_EXPIRED_REGISTRATION_TOKEN)
  List<Token> getAllExpiredRegistrationToken(@Param("now") Long now);

}
