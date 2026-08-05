package com.typemaster.controller;

import com.typemaster.dto.ParagraphDTO;
import com.typemaster.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller providing API endpoints for fetching typing practice paragraphs across all categories
 * with 9-hour non-repetition rotation algorithm.
 */
@RestController
@RequestMapping("/api/paragraphs")
@CrossOrigin(origins = "*")
public class ParagraphController {

    private final ParagraphService paragraphService;

    @Autowired
    public ParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    /**
     * Endpoint to fetch practice material (text paragraph, coding snippet, quotes, numbers, etc.).
     * GET /api/paragraphs?duration={minutes}&mode={PARAGRAPH|CODING|...}&language={JAVA|...}&topic={...}&difficulty={...}&userId={username}
     */
    @GetMapping
    public ResponseEntity<ParagraphDTO> getRandomParagraph(
            @RequestParam(name = "duration", required = false, defaultValue = "1") Integer durationMinutes,
            @RequestParam(name = "mode", required = false, defaultValue = "PARAGRAPH") String mode,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "topic", required = false) String topic,
            @RequestParam(name = "difficulty", required = false) String difficulty,
            @RequestParam(name = "userId", required = false) String userId) {
        ParagraphDTO paragraph = paragraphService.getPracticeMaterial(durationMinutes, mode, language, topic, difficulty, userId);
        return ResponseEntity.ok(paragraph);
    }
}
