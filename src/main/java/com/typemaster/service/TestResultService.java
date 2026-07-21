package com.typemaster.service;

import com.typemaster.dto.TestResultDTO;
import com.typemaster.model.TestResult;
import com.typemaster.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service handling business logic for storing and querying typing test results.
 */
@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;

    @Autowired
    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    /**
     * Saves a test result DTO to persistent storage.
     */
    public TestResultDTO saveResult(TestResultDTO dto) {
        TestResult entity = new TestResult();
        entity.setWpm(dto.getWpm());
        entity.setAccuracy(dto.getAccuracy());
        entity.setMistakes(dto.getMistakes());
        entity.setTypedChars(dto.getTypedChars());
        entity.setCorrectChars(dto.getCorrectChars());
        entity.setTimeTakenSeconds(dto.getTimeTakenSeconds());
        entity.setDurationMinutes(dto.getDurationMinutes());
        entity.setUsername(dto.getUsername());

        TestResult saved = testResultRepository.save(entity);
        return mapToDTO(saved);
    }

    /**
     * Fetches the latest test result (optional username filter).
     */
    public Optional<TestResultDTO> getLatestResult(String username) {
        if (username != null && !username.trim().isEmpty()) {
            return testResultRepository.findTopByUsernameOrderByCreatedAtDesc(username.trim())
                    .map(this::mapToDTO);
        }
        return testResultRepository.findTopByOrderByCreatedAtDesc()
                .map(this::mapToDTO);
    }

    /**
     * Fetches test results history (optional username filter).
     */
    public List<TestResultDTO> getAllResults(String username) {
        if (username != null && !username.trim().isEmpty()) {
            return testResultRepository.findByUsernameOrderByCreatedAtDesc(username.trim()).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        return testResultRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TestResultDTO mapToDTO(TestResult entity) {
        return new TestResultDTO(
                entity.getId(),
                entity.getWpm(),
                entity.getAccuracy(),
                entity.getMistakes(),
                entity.getTypedChars(),
                entity.getCorrectChars(),
                entity.getTimeTakenSeconds(),
                entity.getDurationMinutes(),
                entity.getUsername(),
                entity.getCreatedAt()
        );
    }
}
