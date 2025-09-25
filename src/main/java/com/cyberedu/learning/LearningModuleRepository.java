package com.cyberedu.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LearningModuleRepository extends JpaRepository<LearningModule, Long> {
    List<LearningModule> findByDifficultyOrderByModuleOrderAsc(String difficulty);
    
    @Query("SELECT lm FROM LearningModule lm ORDER BY lm.moduleOrder ASC")
    List<LearningModule> findAllOrderedByModuleOrder();
    
    List<LearningModule> findByTitleContainingIgnoreCase(String title);
}
