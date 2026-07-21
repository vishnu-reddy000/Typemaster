package com.typemaster.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for TestResult API requests and responses.
 */
public class TestResultDTO {

    private Long id;
    private Integer wpm;
    private Integer accuracy;
    private Integer mistakes;
    private Integer typedChars;
    private Integer correctChars;
    private Integer timeTakenSeconds;
    private Integer durationMinutes;
    private String username;
    private LocalDateTime createdAt;

    public TestResultDTO() {
    }

    public TestResultDTO(Long id, Integer wpm, Integer accuracy, Integer mistakes, Integer typedChars, 
                         Integer correctChars, Integer timeTakenSeconds, Integer durationMinutes, String username, LocalDateTime createdAt) {
        this.id = id;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.mistakes = mistakes;
        this.typedChars = typedChars;
        this.correctChars = correctChars;
        this.timeTakenSeconds = timeTakenSeconds;
        this.durationMinutes = durationMinutes;
        this.username = username;
        this.createdAt = createdAt;
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
