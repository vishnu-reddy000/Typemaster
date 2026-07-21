package com.typemaster.controller;

import com.typemaster.dto.ParagraphDTO;
import com.typemaster.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller providing API endpoints for fetching typing practice paragraphs.
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
     * Endpoint to fetch practice material (text paragraph or coding snippet).
     * GET /api/paragraphs?duration={minutes}&mode={PARAGRAPH|CODING}&language={JAVA|PYTHON|...}&topic={HELLO_WORLD|VARIABLES|...}
     */
    @GetMapping
    public ResponseEntity<ParagraphDTO> getRandomParagraph(
            @RequestParam(name = "duration", required = false, defaultValue = "1") Integer durationMinutes,
            @RequestParam(name = "mode", required = false, defaultValue = "PARAGRAPH") String mode,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "topic", required = false) String topic) {
        ParagraphDTO paragraph = paragraphService.getPracticeMaterial(durationMinutes, mode, language, topic);
        return ResponseEntity.ok(paragraph);
    }
}
