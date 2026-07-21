package com.typemaster.model;

import jakarta.persistence.*;

/**
 * JPA Entity representing a typing practice paragraph.
 */
@Entity
@Table(name = "paragraphs")
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "type")
    private String type = "PARAGRAPH"; // PARAGRAPH or CODING

    @Column(name = "language")
    private String language = "ENGLISH"; // JAVA, PYTHON, JAVASCRIPT, CPP, ENGLISH

    @Column(name = "topic")
    private String topic = "GENERAL"; // HELLO_WORLD, VARIABLES, ARITHMETIC, TYPECASTING, GENERAL

    public Paragraph() {
    }

    public Paragraph(String content, Integer durationMinutes) {
        this.content = content;
        this.durationMinutes = durationMinutes;
        this.type = "PARAGRAPH";
        this.language = "ENGLISH";
        this.topic = "GENERAL";
    }

    public Paragraph(String content, Integer durationMinutes, String type, String language, String topic) {
        this.content = content;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.language = language;
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    // Getters and Setters
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
}
