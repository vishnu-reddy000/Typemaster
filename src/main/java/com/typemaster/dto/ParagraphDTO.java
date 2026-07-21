package com.typemaster.dto;

/**
 * Data Transfer Object for Paragraph content responses.
 */
public class ParagraphDTO {

    private Long id;
    private String content;
    private Integer durationMinutes;
    private String type;
    private String language;
    private String topic;

    public ParagraphDTO() {
    }

    public ParagraphDTO(Long id, String content, Integer durationMinutes) {
        this.id = id;
        this.content = content;
        this.durationMinutes = durationMinutes;
    }

    public ParagraphDTO(Long id, String content, Integer durationMinutes, String type, String language, String topic) {
        this.id = id;
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
