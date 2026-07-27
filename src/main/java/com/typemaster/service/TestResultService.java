package com.typemaster.service;

import com.typemaster.dto.TestResultDTO;
import com.typemaster.dto.SystemStatsDTO;
import com.typemaster.model.TestResult;
import com.typemaster.repository.TestResultRepository;
import com.typemaster.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public TestResultService(TestResultRepository testResultRepository, UserRepository userRepository) {
        this.testResultRepository = testResultRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets system-wide summary statistics for home page.
     */
    public SystemStatsDTO getSystemStats() {
        long testsCount = testResultRepository.count();
        long usersCount = userRepository.count();
        long countriesCount = 120 + (usersCount / 15);
        Double avg = testResultRepository.getAverageWpm();
        double avgWpm = (avg != null && avg > 0) ? avg : 85.0;
        return new SystemStatsDTO(testsCount, usersCount, countriesCount, avgWpm);
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
        java.time.LocalDateTime oneWeekAgo = java.time.LocalDateTime.now().minusWeeks(1);
        return testResultRepository.findByCreatedAtAfter(oneWeekAgo).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes test results older than 1 week from the database.
     * Scheduled to run daily at midnight.
     */
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 0 * * ?")
    @org.springframework.transaction.annotation.Transactional
    public void deleteOldResults() {
        java.time.LocalDateTime oneWeekAgo = java.time.LocalDateTime.now().minusWeeks(1);
        testResultRepository.deleteByCreatedAtBefore(oneWeekAgo);
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
