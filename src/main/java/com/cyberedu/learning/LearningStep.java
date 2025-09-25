package com.cyberedu.learning;

import jakarta.persistence.*;

@Entity
public class LearningStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_module_id", nullable = false)
    private LearningModule learningModule;

    @Column(nullable = false)
    private String stepTitle;

    @Column(length = 3000)
    private String stepContent;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(length = 1000)
    private String mediaUrl; // For images, videos, or documents

    @Column(length = 50)
    private String mediaType; // IMAGE, VIDEO, DOCUMENT, NONE

    // Constructors
    public LearningStep() {}

    public LearningStep(Long id, LearningModule learningModule, String stepTitle, String stepContent, Integer stepOrder, String mediaUrl, String mediaType) {
        this.id = id;
        this.learningModule = learningModule;
        this.stepTitle = stepTitle;
        this.stepContent = stepContent;
        this.stepOrder = stepOrder;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LearningModule getLearningModule() { return learningModule; }
    public void setLearningModule(LearningModule learningModule) { this.learningModule = learningModule; }

    public String getStepTitle() { return stepTitle; }
    public void setStepTitle(String stepTitle) { this.stepTitle = stepTitle; }

    public String getStepContent() { return stepContent; }
    public void setStepContent(String stepContent) { this.stepContent = stepContent; }

    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
}
