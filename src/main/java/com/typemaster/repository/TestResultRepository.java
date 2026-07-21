package com.typemaster.repository;

import com.typemaster.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for TestResult entity.
 */
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    Optional<TestResult> findTopByOrderByCreatedAtDesc();

    Optional<TestResult> findTopByUsernameOrderByCreatedAtDesc(String username);

    java.util.List<TestResult> findByUsernameOrderByCreatedAtDesc(String username);
}
