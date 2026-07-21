package com.typemaster.controller;

import com.typemaster.dto.TestResultDTO;
import com.typemaster.service.TestResultService;
import com.typemaster.websocket.ResultWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for saving and retrieving typing test performance results.
 */
@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class TestResultController {

    private final TestResultService testResultService;
    private final ResultWebSocketHandler webSocketHandler;

    @Autowired
    public TestResultController(TestResultService testResultService, ResultWebSocketHandler webSocketHandler) {
        this.testResultService = testResultService;
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * Endpoint to save a completed test result.
     * POST /api/results
     */
    @PostMapping
    public ResponseEntity<TestResultDTO> saveResult(@RequestBody TestResultDTO resultDTO) {
        TestResultDTO saved = testResultService.saveResult(resultDTO);
        try {
            String jsonPayload = String.format(
                "{\"type\":\"NEW_RESULT\",\"wpm\":%d,\"accuracy\":%d,\"username\":\"%s\"}",
                saved.getWpm(),
                saved.getAccuracy(),
                saved.getUsername() != null ? saved.getUsername() : "Anonymous"
            );
            webSocketHandler.broadcast(jsonPayload);
        } catch (Exception e) {
            // Ignore broadcast error
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Endpoint to retrieve the most recent test result.
     * GET /api/results/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<TestResultDTO> getLatestResult(@RequestParam(name = "username", required = false) String username) {
        return testResultService.getLatestResult(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to retrieve historic test results (optionally filtered by username).
     * GET /api/results?username={username}
     */
    @GetMapping
    public ResponseEntity<List<TestResultDTO>> getAllResults(@RequestParam(name = "username", required = false) String username) {
        return ResponseEntity.ok(testResultService.getAllResults(username));
    }
}
