package com.typemaster.repository;

import com.typemaster.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Paragraph entity.
 */
@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    List<Paragraph> findByDurationMinutes(Integer durationMinutes);

    List<Paragraph> findByType(String type);

    List<Paragraph> findByTypeAndDurationMinutes(String type, Integer durationMinutes);

    List<Paragraph> findByTypeAndLanguage(String type, String language);

    List<Paragraph> findByTypeAndLanguageAndTopic(String type, String language, String topic);
}
