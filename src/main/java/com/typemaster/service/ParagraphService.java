package com.typemaster.service;

import com.typemaster.dto.ParagraphDTO;
import com.typemaster.model.Paragraph;
import com.typemaster.repository.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Service handling business logic for typing practice paragraphs across all 12 categories.
 */
@Service
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final Random random = new Random();

    @Autowired
    public ParagraphService(ParagraphRepository paragraphRepository) {
        this.paragraphRepository = paragraphRepository;
    }

    /**
     * Retrieves practice material matched to mode, language, topic, difficulty, and duration.
     */
    public ParagraphDTO getPracticeMaterial(Integer durationMinutes, String mode, String language, String topic, String difficulty) {
        int duration = (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 1;
        String targetMode = (mode != null && !mode.trim().isEmpty()) ? mode.trim().toUpperCase() : "PARAGRAPH";

        if ("CODING".equals(targetMode)) {
            String targetLang = (language != null && !language.trim().isEmpty()) ? language.trim().toUpperCase() : "JAVA";
            String targetTopic = (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("ALL")) ? topic.trim().toUpperCase() : null;

            List<Paragraph> matches;
            if (targetTopic != null) {
                matches = paragraphRepository.findByTypeAndLanguageAndTopic("CODING", targetLang, targetTopic);
                if (matches.isEmpty()) {
                    matches = paragraphRepository.findByTypeAndLanguage("CODING", targetLang);
                }
            } else {
                matches = paragraphRepository.findByTypeAndLanguage("CODING", targetLang);
            }

            if (matches.isEmpty()) {
                matches = paragraphRepository.findByTypeAndLanguage("CODING", "JAVA");
            }

            StringBuilder scaledCode = new StringBuilder();
            if (!matches.isEmpty()) {
                List<Paragraph> pool = new ArrayList<>(matches);
                Collections.shuffle(pool, random);

                for (int i = 0; i < duration; i++) {
                    Paragraph snippet = pool.get(i % pool.size());
                    if (i > 0) scaledCode.append("\n\n// --- Program Block ").append(i + 1).append(" ---\n");
                    scaledCode.append(snippet.getContent());
                }
            } else {
                scaledCode.append("public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}");
            }

            return new ParagraphDTO(0L, scaledCode.toString(), duration, "CODING", targetLang, targetTopic != null ? targetTopic : "ALL");
        } else {
            // Mode lookup for PARAGRAPH, CHILDREN_POETS, QUOTES, NUMBERS, SYMBOLS, MIXED, INTERVIEW, OFFICE, COMPETITIVE, EMAIL
            List<Paragraph> matches = paragraphRepository.findByType(targetMode);

            if (matches.isEmpty() && !"PARAGRAPH".equals(targetMode)) {
                matches = paragraphRepository.findByType("PARAGRAPH");
            }

            if (matches.isEmpty()) {
                return new ParagraphDTO(0L, "Practice typing every day to build speed, rhythm, and precision across all practice categories.", duration, targetMode, "ENGLISH", "GENERAL");
            }

            List<Paragraph> pool = new ArrayList<>(matches);
            Collections.shuffle(pool, random);
            Paragraph selected = pool.get(0);

            return new ParagraphDTO(selected.getId(), selected.getContent().trim(), duration, targetMode, selected.getLanguage(), selected.getTopic());
        }
    }
}
