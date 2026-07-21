package com.typemaster.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a completed typing test score result.
 */
@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer wpm;

    @Column(nullable = false)
    private Integer accuracy;

    @Column(nullable = false)
    private Integer mistakes;

    @Column(name = "typed_chars", nullable = false)
    private Integer typedChars;

    @Column(name = "correct_chars", nullable = false)
    private Integer correctChars;

    @Column(name = "time_taken_seconds", nullable = false)
    private Integer timeTakenSeconds;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "username")
    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TestResult() {
        this.createdAt = LocalDateTime.now();
    }

    public TestResult(Integer wpm, Integer accuracy, Integer mistakes, Integer typedChars, 
                      Integer correctChars, Integer timeTakenSeconds, Integer durationMinutes) {
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.mistakes = mistakes;
        this.typedChars = typedChars;
        this.correctChars = correctChars;
        this.timeTakenSeconds = timeTakenSeconds;
        this.durationMinutes = durationMinutes;
        this.createdAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWpm() {
        return wpm;
    }

    public void setWpm(Integer wpm) {
        this.wpm = wpm;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getMistakes() {
        return mistakes;
    }

    public void setMistakes(Integer mistakes) {
        this.mistakes = mistakes;
    }

    public Integer getTypedChars() {
        return typedChars;
    }

    public void setTypedChars(Integer typedChars) {
        this.typedChars = typedChars;
    }

    public Integer getCorrectChars() {
        return correctChars;
    }

    public void setCorrectChars(Integer correctChars) {
        this.correctChars = correctChars;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
