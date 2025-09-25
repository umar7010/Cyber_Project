package com.cyberedu.news;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsArticleRepository newsRepo;

    public NewsController(NewsArticleRepository newsRepo) {
        this.newsRepo = newsRepo;
    }

    @GetMapping
    public List<NewsArticle> getPublishedArticles() {
        return newsRepo.findByIsPublishedTrueOrderByPublishedAtDesc();
    }

    @GetMapping("/search")
    public List<NewsArticle> searchArticles(@RequestParam String query) {
        return newsRepo.searchPublishedArticles(query);
    }

    @GetMapping("/incident/{incidentType}")
    public List<NewsArticle> getArticlesByIncidentType(@PathVariable String incidentType) {
        return newsRepo.findByIncidentTypeOrderByPublishedAtDesc(incidentType);
    }

    @GetMapping("/severity/{severity}")
    public List<NewsArticle> getArticlesBySeverity(@PathVariable String severity) {
        return newsRepo.findBySeverityOrderByPublishedAtDesc(severity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticle> getArticleById(@PathVariable Long id) {
        Optional<NewsArticle> article = newsRepo.findById(id);
        return article.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NewsArticle createArticle(@RequestBody NewsArticle article) {
        return newsRepo.save(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsArticle> updateArticle(@PathVariable Long id, @RequestBody NewsArticle articleDetails) {
        Optional<NewsArticle> optionalArticle = newsRepo.findById(id);
        if (optionalArticle.isPresent()) {
            NewsArticle article = optionalArticle.get();
            article.setTitle(articleDetails.getTitle());
            article.setSummary(articleDetails.getSummary());
            article.setContent(articleDetails.getContent());
            article.setIncidentType(articleDetails.getIncidentType());
            article.setSeverity(articleDetails.getSeverity());
            article.setSourceUrl(articleDetails.getSourceUrl());
            article.setImageUrl(articleDetails.getImageUrl());
            article.setIsPublished(articleDetails.getIsPublished());
            return ResponseEntity.ok(newsRepo.save(article));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (newsRepo.existsById(id)) {
            newsRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
