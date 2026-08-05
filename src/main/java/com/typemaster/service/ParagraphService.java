package com.typemaster.service;

import com.typemaster.dto.ParagraphDTO;
import com.typemaster.model.Paragraph;
import com.typemaster.repository.ParagraphRepository;
import com.typemaster.repository.UserParagraphHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enterprise Paragraph Management Service.
 * Implements deterministic randomization via ThreadLocalRandom, ID-only candidate fetching,
 * in-memory metadata caching, and asynchronous non-blocking history logging for O(log N) scalability across 10,000+ paragraphs.
 */
@Service
public class ParagraphService {

    private static final int COOLDOWN_HOURS = 9;
    private static final int RECENT_BUFFER_SIZE = 20;

    private final ParagraphRepository paragraphRepository;
    private final UserParagraphHistoryRepository historyRepository;
    private final AsyncHistoryService asyncHistoryService;

    // High-Performance In-Memory Paragraph Cache for O(1) entity lookups
    private final ConcurrentHashMap<Long, Paragraph> metadataCache = new ConcurrentHashMap<>(1024);

    @Autowired
    public ParagraphService(ParagraphRepository paragraphRepository,
                            UserParagraphHistoryRepository historyRepository,
                            AsyncHistoryService asyncHistoryService) {
        this.paragraphRepository = paragraphRepository;
        this.historyRepository = historyRepository;
        this.asyncHistoryService = asyncHistoryService;
    }

    public ParagraphDTO getPracticeMaterial(Integer durationMinutes, String mode, String language, String topic, String difficulty) {
        return getPracticeMaterial(durationMinutes, mode, language, topic, difficulty, null);
    }

    /**
     * Main High-Performance Selection Method:
     * - Fetches lightweight candidate IDs only (0 full entity loading).
     * - Applies deterministic ThreadLocalRandom selection.
     * - Retrieves paragraph entity from metadata cache or DB.
     * - Offloads history logging asynchronously.
     */
    @Transactional(readOnly = true)
    public ParagraphDTO getPracticeMaterial(Integer durationMinutes, String mode, String language, String topic, String difficulty, String userId) {
        int duration = (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 1;
        String targetMode = (mode != null && !mode.trim().isEmpty()) ? mode.trim().toUpperCase() : "PARAGRAPH";
        String targetDiff = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty.trim() : null;
        String effectiveUserId = (userId != null && !userId.trim().isEmpty()) ? userId.trim() : "anonymous_user";

        if ("CODING".equals(targetMode)) {
            return getCodingPracticeMaterial(duration, language, topic, effectiveUserId);
        } else {
            return getTextPracticeMaterial(duration, targetMode, targetDiff, effectiveUserId);
        }
    }

    private ParagraphDTO getTextPracticeMaterial(int duration, String mode, String difficulty, String userId) {
        List<Long> candidateIds = new ArrayList<>();

        // 1. SQL Indexed ID-Only Retrieval
        if (difficulty != null) {
            candidateIds = paragraphRepository.findIdsByCategoryAndDifficultyAndActive(mode, difficulty, true);
        }

        if (candidateIds.isEmpty()) {
            candidateIds = paragraphRepository.findIdsByCategoryAndActive(mode, true);
        }

        if (candidateIds.isEmpty()) {
            candidateIds = paragraphRepository.findIdsByTypeAndActive(mode, true);
        }

        if (candidateIds.isEmpty()) {
            candidateIds = paragraphRepository.findIdsByActive(true);
        }

        if (candidateIds.isEmpty()) {
            return new ParagraphDTO(0L, "Practice typing every day to build speed, rhythm, and precision across all practice categories.", duration, mode, "English", "GENERAL");
        }

        // 2. Deterministic ID Selection
        Long selectedId = selectRotatedCandidateId(candidateIds, userId);

        // 3. Cached / Single-Row Entity Retrieval
        Paragraph selected = getCachedOrFetchParagraph(selectedId);

        // 4. Asynchronous Non-Blocking History Log
        asyncHistoryService.recordParagraphShownAsync(userId, selectedId);

        return new ParagraphDTO(selected.getId(), selected.getContent().trim(), duration, mode, selected.getLanguage(), selected.getTopic());
    }

    private ParagraphDTO getCodingPracticeMaterial(int duration, String language, String topic, String userId) {
        String targetLang = (language != null && !language.trim().isEmpty()) ? language.trim().toUpperCase() : "JAVA";
        String targetTopic = (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("ALL")) ? topic.trim().toUpperCase() : null;

        List<Long> candidateIds;
        if (targetTopic != null) {
            candidateIds = paragraphRepository.findIdsByTypeAndLanguageAndTopicAndActive("CODING", targetLang, targetTopic, true);
            if (candidateIds.isEmpty()) {
                candidateIds = paragraphRepository.findIdsByTypeAndLanguageAndActive("CODING", targetLang, true);
            }
        } else {
            candidateIds = paragraphRepository.findIdsByTypeAndLanguageAndActive("CODING", targetLang, true);
        }

        if (candidateIds.isEmpty()) {
            candidateIds = paragraphRepository.findIdsByTypeAndLanguageAndActive("CODING", "JAVA", true);
        }

        StringBuilder scaledCode = new StringBuilder();
        Long primaryId = 0L;

        if (!candidateIds.isEmpty()) {
            for (int i = 0; i < duration; i++) {
                Long selectedId = selectRotatedCandidateId(candidateIds, userId);
                Paragraph snippet = getCachedOrFetchParagraph(selectedId);

                if (i == 0) primaryId = snippet.getId();
                if (i > 0) scaledCode.append("\n\n// --- Program Block ").append(i + 1).append(" ---\n");
                scaledCode.append(snippet.getContent());
                asyncHistoryService.recordParagraphShownAsync(userId, selectedId);
            }
        } else {
            scaledCode.append("public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}");
        }

        return new ParagraphDTO(primaryId, scaledCode.toString(), duration, "CODING", targetLang, targetTopic != null ? targetTopic : "ALL");
    }

    /**
     * Deterministic Rotation Selection Algorithm:
     * - Uses ThreadLocalRandom.current() for lock-free random indexing.
     * - Rule 1: Zero consecutive repeat (excludes lastShownId).
     * - Rule 2: Prioritizes completely UNSEEN candidate IDs.
     * - Rule 3: Excludes IDs shown in last 9 hours or top 20 recent tests.
     * - Rule 4: Oldest shown fallback when candidate pool is exhausted.
     */
    private Long selectRotatedCandidateId(List<Long> candidateIds, String userId) {
        if (candidateIds == null || candidateIds.isEmpty()) {
            throw new IllegalArgumentException("Candidate ID list cannot be empty");
        }

        if (candidateIds.size() == 1) {
            return candidateIds.get(0);
        }

        // Rule 1: Fetch last shown ID for user
        Long lastShownId = null;
        try {
            List<Long> lastIds = historyRepository.findLastShownParagraphIdsForUser(userId, PageRequest.of(0, 1));
            if (!lastIds.isEmpty()) {
                lastShownId = lastIds.get(0);
            }
        } catch (Exception e) {
            System.err.println("Last shown query warning: " + e.getMessage());
        }

        // Rule 2: Fetch 9-hour cooldown + top 20 recent paragraph IDs
        LocalDateTime cutoff = LocalDateTime.now().minusHours(COOLDOWN_HOURS);
        Set<Long> excludedIdSet = new HashSet<>();
        if (lastShownId != null) {
            excludedIdSet.add(lastShownId);
        }

        try {
            List<Long> recentInWindow = historyRepository.findRecentParagraphIds(userId, cutoff);
            excludedIdSet.addAll(recentInWindow);

            List<Long> recentTop20 = historyRepository.findRecentParagraphIdsForUser(userId, PageRequest.of(0, RECENT_BUFFER_SIZE));
            excludedIdSet.addAll(recentTop20);
        } catch (Exception e) {
            System.err.println("UserParagraphHistory query warning: " + e.getMessage());
        }

        // Tier 1: Completely UNSEEN candidate IDs for this user
        try {
            Set<Long> allEverSeenIds = new HashSet<>(historyRepository.findAllSeenParagraphIdsForUser(userId));
            List<Long> unseenCandidateIds = new ArrayList<>();
            for (Long id : candidateIds) {
                if (id != null && !allEverSeenIds.contains(id)) {
                    unseenCandidateIds.add(id);
                }
            }
            if (!unseenCandidateIds.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(unseenCandidateIds.size());
                return unseenCandidateIds.get(randomIndex);
            }
        } catch (Exception e) {
            System.err.println("Unseen candidate ID check warning: " + e.getMessage());
        }

        // Tier 2: Fresh candidate IDs outside 9-hour cooldown & top 20 buffer
        List<Long> freshCandidateIds = new ArrayList<>();
        for (Long id : candidateIds) {
            if (id != null && !excludedIdSet.contains(id)) {
                freshCandidateIds.add(id);
            }
        }
        if (!freshCandidateIds.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(freshCandidateIds.size());
            return freshCandidateIds.get(randomIndex);
        }

        // Tier 3: Exhausted Pool Fallback -> Pick oldest shown candidate ID (strictly excluding lastShownId)
        final Long forbiddenId = lastShownId;
        List<Long> fallbackCandidateIds = candidateIds.stream()
                .filter(id -> id != null && !Objects.equals(id, forbiddenId))
                .toList();

        if (fallbackCandidateIds.isEmpty()) {
            fallbackCandidateIds = candidateIds;
        }

        try {
            List<Long> oldestIds = historyRepository.findOldestShownParagraphIds(userId, fallbackCandidateIds, PageRequest.of(0, 1));
            if (!oldestIds.isEmpty()) {
                return oldestIds.get(0);
            }
        } catch (Exception e) {
            System.err.println("Oldest candidate ID fallback warning: " + e.getMessage());
        }

        // Tier 4: Ultimate Fallback -> Deterministic ThreadLocalRandom choice from fallback candidate IDs
        int randomIndex = ThreadLocalRandom.current().nextInt(fallbackCandidateIds.size());
        return fallbackCandidateIds.get(randomIndex);
    }

    /**
     * High-Performance Metadata Caching: Returns cached Paragraph or fetches single row by ID.
     */
    private Paragraph getCachedOrFetchParagraph(Long paragraphId) {
        if (paragraphId == null || paragraphId <= 0) {
            return new Paragraph("Practice typing every day to build speed, rhythm, and precision.", "General English", "Medium", "English");
        }

        return metadataCache.computeIfAbsent(paragraphId, id ->
                paragraphRepository.findById(id).orElseGet(() ->
                        new Paragraph("Practice typing every day to build speed, rhythm, and precision.", "General English", "Medium", "English")
                )
        );
    }

    /**
     * Clears in-memory metadata cache when new paragraphs are seeded/inserted.
     */
    public void clearMetadataCache() {
        metadataCache.clear();
    }
}
