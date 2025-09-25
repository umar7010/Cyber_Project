package com.cyberedu.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByIsPublishedTrueOrderByPublishedAtDesc();
    List<NewsArticle> findByIncidentTypeOrderByPublishedAtDesc(String incidentType);
    List<NewsArticle> findBySeverityOrderByPublishedAtDesc(String severity);
    
    @Query("SELECT na FROM NewsArticle na WHERE na.isPublished = true AND (na.title LIKE %?1% OR na.content LIKE %?1%) ORDER BY na.publishedAt DESC")
    List<NewsArticle> searchPublishedArticles(String searchTerm);
}
