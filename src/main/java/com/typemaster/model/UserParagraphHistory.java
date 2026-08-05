package com.typemaster.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity tracking paragraph rotation history per user.
 * Enforces 8-9 hour non-repetition cooldown and rotation algorithms.
 */
@Entity
@Table(
    name = "user_paragraph_history",
    indexes = {
        @Index(name = "idx_uph_user_shown", columnList = "user_id, shown_at"),
        @Index(name = "idx_uph_user_para", columnList = "user_id, paragraph_id")
    }
)
public class UserParagraphHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "paragraph_id", nullable = false)
    private Long paragraphId;

    @Column(name = "shown_at", nullable = false)
    private LocalDateTime shownAt;

    public UserParagraphHistory() {
    }

    public UserParagraphHistory(String userId, Long paragraphId, LocalDateTime shownAt) {
        this.userId = userId;
        this.paragraphId = paragraphId;
        this.shownAt = shownAt != null ? shownAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(Long paragraphId) {
        this.paragraphId = paragraphId;
    }

    public LocalDateTime getShownAt() {
        return shownAt;
    }

    public void setShownAt(LocalDateTime shownAt) {
        this.shownAt = shownAt;
    }
}
