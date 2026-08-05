package com.typemaster.repository;

import com.typemaster.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Paragraph entity.
 * Provides ID-only projection queries for O(log N) scalable deterministic selection.
 */
@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    long countByActive(Boolean active);

    List<Paragraph> findByActive(Boolean active);

    List<Paragraph> findByType(String type);

    /**
     * ID-Only Queries for O(log N) zero-memory candidate selection
     */
    @Query("SELECT p.id FROM Paragraph p WHERE p.category = :category AND p.difficulty = :difficulty AND p.active = :active")
    List<Long> findIdsByCategoryAndDifficultyAndActive(@Param("category") String category, @Param("difficulty") String difficulty, @Param("active") Boolean active);

    @Query("SELECT p.id FROM Paragraph p WHERE p.category = :category AND p.active = :active")
    List<Long> findIdsByCategoryAndActive(@Param("category") String category, @Param("active") Boolean active);

    @Query("SELECT p.id FROM Paragraph p WHERE p.type = :type AND p.active = :active")
    List<Long> findIdsByTypeAndActive(@Param("type") String type, @Param("active") Boolean active);

    @Query("SELECT p.id FROM Paragraph p WHERE p.type = :type AND p.language = :language AND p.active = :active")
    List<Long> findIdsByTypeAndLanguageAndActive(@Param("type") String type, @Param("language") String language, @Param("active") Boolean active);

    @Query("SELECT p.id FROM Paragraph p WHERE p.type = :type AND p.language = :language AND p.topic = :topic AND p.active = :active")
    List<Long> findIdsByTypeAndLanguageAndTopicAndActive(@Param("type") String type, @Param("language") String language, @Param("topic") String topic, @Param("active") Boolean active);

    @Query("SELECT p.id FROM Paragraph p WHERE p.active = :active")
    List<Long> findIdsByActive(@Param("active") Boolean active);

    List<Paragraph> findByCategoryAndActive(String category, Boolean active);

    List<Paragraph> findByCategoryAndDifficultyAndActive(String category, String difficulty, Boolean active);

    List<Paragraph> findByTypeAndDurationMinutes(String type, Integer durationMinutes);

    List<Paragraph> findByTypeAndLanguage(String type, String language);

    List<Paragraph> findByTypeAndLanguageAndTopic(String type, String language, String topic);
}
