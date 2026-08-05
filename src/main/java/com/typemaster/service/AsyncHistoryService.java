package com.typemaster.service;

import com.typemaster.model.UserParagraphHistory;
import com.typemaster.repository.UserParagraphHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Asynchronous History Recording Service.
 * Offloads user paragraph history writes to background threads, guaranteeing 0ms blocking on API HTTP responses.
 */
@Service
public class AsyncHistoryService {

    private static final int DUPLICATE_SPAM_THRESHOLD_SECONDS = 5;
    private final UserParagraphHistoryRepository historyRepository;

    @Autowired
    public AsyncHistoryService(UserParagraphHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Non-blocking asynchronous method to record user paragraph view history.
     */
    @Async
    public void recordParagraphShownAsync(String userId, Long paragraphId) {
        if (userId == null || paragraphId == null || paragraphId <= 0) return;
        try {
            LocalDateTime threshold = LocalDateTime.now().minusSeconds(DUPLICATE_SPAM_THRESHOLD_SECONDS);
            long recentSpamCount = historyRepository.countRecentDuplicateHistory(userId, paragraphId, threshold);
            if (recentSpamCount == 0) {
                UserParagraphHistory history = new UserParagraphHistory(userId, paragraphId, LocalDateTime.now());
                historyRepository.save(history);
            }
        } catch (Exception e) {
            System.err.println("Async history record warning: " + e.getMessage());
        }
    }
}
