package com.typemaster.model;

import jakarta.persistence.*;

/**
 * JPA Entity representing a typing practice paragraph.
 */
@Entity
@Table(name = "paragraphs", indexes = {
    @Index(name = "idx_para_type_active", columnList = "type, active"),
    @Index(name = "idx_para_category_diff", columnList = "category, difficulty, active")
})
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 1;

    @Column(name = "type")
    private String type = "PARAGRAPH";

    @Column(name = "category")
    private String category = "General English";

    @Column(name = "difficulty")
    private String difficulty = "Medium"; // Easy, Medium, Hard

    @Column(name = "language")
    private String language = "English";

    @Column(name = "topic")
    private String topic = "GENERAL";

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Paragraph() {
    }

    public Paragraph(String content, Integer durationMinutes) {
        this.content = content;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 1;
        this.type = "PARAGRAPH";
        this.category = "General English";
        this.difficulty = "Medium";
        this.language = "English";
        this.topic = "GENERAL";
        this.active = true;
    }

    public Paragraph(String content, Integer durationMinutes, String type, String language, String topic) {
        this.content = content;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 1;
        this.type = type;
        this.category = type;
        this.difficulty = "Medium";
        this.language = language;
        this.topic = topic;
        this.active = true;
    }

    public Paragraph(String content, String category, String difficulty, String language) {
        this.content = content;
        this.category = category;
        this.type = category.toUpperCase().replaceAll("\\s+", "_");
        this.difficulty = difficulty;
        this.language = language;
        this.durationMinutes = 1;
        this.topic = "GENERAL";
        this.active = true;
    }

    public Paragraph(String content, String category, String difficulty, String language, Integer durationMinutes) {
        this.content = content;
        this.category = category;
        this.type = category.toUpperCase().replaceAll("\\s+", "_");
        this.difficulty = difficulty;
        this.language = language;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 1;
        this.topic = "GENERAL";
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
