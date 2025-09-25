package com.cyberedu.learning;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LearningModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer moduleOrder;

    @Column(nullable = false)
    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED

    @Column(length = 5000)
    private String content;

    @Column(length = 2000)
    private String realLifeExample;

    @Column(length = 1000)
    private String keyTakeaways;

    @OneToMany(mappedBy = "learningModule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LearningStep> steps;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

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
    public LearningModule() {}

    public LearningModule(Long id, String title, String description, Integer moduleOrder, String difficulty, String content, String realLifeExample, String keyTakeaways, List<LearningStep> steps, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.moduleOrder = moduleOrder;
        this.difficulty = difficulty;
        this.content = content;
        this.realLifeExample = realLifeExample;
        this.keyTakeaways = keyTakeaways;
        this.steps = steps;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getModuleOrder() { return moduleOrder; }
    public void setModuleOrder(Integer moduleOrder) { this.moduleOrder = moduleOrder; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRealLifeExample() { return realLifeExample; }
    public void setRealLifeExample(String realLifeExample) { this.realLifeExample = realLifeExample; }

    public String getKeyTakeaways() { return keyTakeaways; }
    public void setKeyTakeaways(String keyTakeaways) { this.keyTakeaways = keyTakeaways; }

    public List<LearningStep> getSteps() { return steps; }
    public void setSteps(List<LearningStep> steps) { this.steps = steps; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
