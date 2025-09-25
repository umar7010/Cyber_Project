package com.cyberedu.glossary;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GlossaryTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String term;

    @Column(length = 2000)
    private String definition;

    @Column(length = 200)
    private String category; // NETWORKING, CRYPTOGRAPHY, MALWARE, etc.

    @Column(length = 1000)
    private String mediaUrl; // For images, videos, or documents

    @Column(length = 50)
    private String mediaType; // IMAGE, VIDEO, DOCUMENT, NONE

    @Column(length = 500)
    private String example;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isPublished = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public GlossaryTerm() {}

    public GlossaryTerm(Long id, String term, String definition, String category, String mediaUrl, String mediaType, String example, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isPublished) {
        this.id = id;
        this.term = term;
        this.definition = definition;
        this.category = category;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.example = example;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPublished = isPublished;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
}
