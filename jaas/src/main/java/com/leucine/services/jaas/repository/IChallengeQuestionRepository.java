package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.ChallengeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IChallengeQuestionRepository extends JpaRepository<ChallengeQuestion, Integer>, JpaSpecificationExecutor<Integer> {
}
