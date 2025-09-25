package com.cyberedu.glossary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GlossaryTermRepository extends JpaRepository<GlossaryTerm, Long> {
    List<GlossaryTerm> findByIsPublishedTrueOrderByTermAsc();
    List<GlossaryTerm> findByCategoryOrderByTermAsc(String category);
    
    @Query("SELECT gt FROM GlossaryTerm gt WHERE gt.isPublished = true AND (gt.term LIKE %?1% OR gt.definition LIKE %?1%) ORDER BY gt.term ASC")
    List<GlossaryTerm> searchPublishedTerms(String searchTerm);
    
    List<GlossaryTerm> findByTermContainingIgnoreCase(String term);
}
