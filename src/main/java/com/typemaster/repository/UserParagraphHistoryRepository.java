package com.typemaster.repository;

import com.typemaster.model.UserParagraphHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for UserParagraphHistory entity.
 * Provides optimized indexed queries to enforce Monkeytype-grade non-repetition rotation.
 */
@Repository
public interface UserParagraphHistoryRepository extends JpaRepository<UserParagraphHistory, Long> {

    /**
     * Finds the very last paragraph ID shown to a specific user.
     */
    @Query("SELECT h.paragraphId FROM UserParagraphHistory h WHERE h.userId = :userId ORDER BY h.shownAt DESC, h.id DESC")
    List<Long> findLastShownParagraphIdsForUser(@Param("userId") String userId, Pageable pageable);

    /**
     * Finds all paragraph IDs ever shown to a user (for prioritizing unseen paragraphs).
     */
    @Query("SELECT DISTINCT h.paragraphId FROM UserParagraphHistory h WHERE h.userId = :userId")
    List<Long> findAllSeenParagraphIdsForUser(@Param("userId") String userId);

    /**
     * Finds distinct paragraph IDs shown to a user within a cutoff window (e.g. last 9 hours).
     */
    @Query("SELECT DISTINCT h.paragraphId FROM UserParagraphHistory h WHERE h.userId = :userId AND h.shownAt >= :cutoff")
    List<Long> findRecentParagraphIds(@Param("userId") String userId, @Param("cutoff") LocalDateTime cutoff);

    /**
     * Finds recent paragraph IDs shown to a user ordered by most recent first.
     */
    @Query("SELECT DISTINCT h.paragraphId FROM UserParagraphHistory h WHERE h.userId = :userId ORDER BY h.shownAt DESC")
    List<Long> findRecentParagraphIdsForUser(@Param("userId") String userId, Pageable pageable);

    /**
     * Finds paragraph IDs from candidate list ordered by oldest shownAt timestamp for a user.
     */
    @Query("SELECT h.paragraphId FROM UserParagraphHistory h WHERE h.userId = :userId AND h.paragraphId IN :candidateIds ORDER BY h.shownAt ASC")
    List<Long> findOldestShownParagraphIds(@Param("userId") String userId, @Param("candidateIds") List<Long> candidateIds, Pageable pageable);

    /**
     * Checks if a paragraph was recorded for a user within a short threshold to prevent duplicate history spam.
     */
    @Query("SELECT COUNT(h) FROM UserParagraphHistory h WHERE h.userId = :userId AND h.paragraphId = :paragraphId AND h.shownAt >= :threshold")
    long countRecentDuplicateHistory(@Param("userId") String userId, @Param("paragraphId") Long paragraphId, @Param("threshold") LocalDateTime threshold);

    /**
     * Deletes paragraph history entries older than specified cutoff timestamp.
     */
    void deleteByShownAtBefore(LocalDateTime cutoff);
}
