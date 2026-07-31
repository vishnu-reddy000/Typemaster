package com.typemaster.repository;

import com.typemaster.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    java.util.List<TestResult> findByCreatedAtAfter(java.time.LocalDateTime dateTime);

    void deleteByCreatedAtBefore(java.time.LocalDateTime dateTime);

    @Query("SELECT AVG(t.wpm) FROM TestResult t")
    Double getAverageWpm();

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("UPDATE TestResult t SET t.username = :newUsername WHERE t.username = :oldUsername")
    int updateUsernameInTestResults(@org.springframework.data.repository.query.Param("oldUsername") String oldUsername, @org.springframework.data.repository.query.Param("newUsername") String newUsername);
}
