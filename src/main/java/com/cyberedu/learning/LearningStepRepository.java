package com.cyberedu.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LearningStepRepository extends JpaRepository<LearningStep, Long> {
    List<LearningStep> findByLearningModuleOrderByStepOrderAsc(LearningModule learningModule);
    List<LearningStep> findByLearningModuleOrderByStepOrder(LearningModule learningModule);
    List<LearningStep> findByLearningModuleIdOrderByStepOrderAsc(Long learningModuleId);
}
