package com.cyberedu.learning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/learning")
public class LearningController {
    private final LearningModuleRepository moduleRepo;
    private final LearningStepRepository stepRepo;

    public LearningController(LearningModuleRepository moduleRepo, LearningStepRepository stepRepo) {
        this.moduleRepo = moduleRepo;
        this.stepRepo = stepRepo;
    }

    @GetMapping("/modules")
    public List<LearningModule> getAllModules() {
        return moduleRepo.findAllOrderedByModuleOrder();
    }

    @GetMapping("/modules/difficulty/{difficulty}")
    public List<LearningModule> getModulesByDifficulty(@PathVariable String difficulty) {
        return moduleRepo.findByDifficultyOrderByModuleOrderAsc(difficulty);
    }

    @GetMapping("/modules/{id}")
    public ResponseEntity<LearningModule> getModuleById(@PathVariable Long id) {
        Optional<LearningModule> module = moduleRepo.findById(id);
        return module.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/modules/{id}/steps")
    public List<LearningStep> getModuleSteps(@PathVariable Long id) {
        return stepRepo.findByLearningModuleIdOrderByStepOrderAsc(id);
    }

    @PostMapping("/modules")
    public LearningModule createModule(@RequestBody LearningModule module) {
        return moduleRepo.save(module);
    }

    @PutMapping("/modules/{id}")
    public ResponseEntity<LearningModule> updateModule(@PathVariable Long id, @RequestBody LearningModule moduleDetails) {
        Optional<LearningModule> optionalModule = moduleRepo.findById(id);
        if (optionalModule.isPresent()) {
            LearningModule module = optionalModule.get();
            module.setTitle(moduleDetails.getTitle());
            module.setDescription(moduleDetails.getDescription());
            module.setModuleOrder(moduleDetails.getModuleOrder());
            module.setDifficulty(moduleDetails.getDifficulty());
            module.setContent(moduleDetails.getContent());
            module.setRealLifeExample(moduleDetails.getRealLifeExample());
            module.setKeyTakeaways(moduleDetails.getKeyTakeaways());
            return ResponseEntity.ok(moduleRepo.save(module));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/steps")
    public LearningStep createStep(@RequestBody LearningStep step) {
        return stepRepo.save(step);
    }

    @PutMapping("/steps/{id}")
    public ResponseEntity<LearningStep> updateStep(@PathVariable Long id, @RequestBody LearningStep stepDetails) {
        Optional<LearningStep> optionalStep = stepRepo.findById(id);
        if (optionalStep.isPresent()) {
            LearningStep step = optionalStep.get();
            step.setStepTitle(stepDetails.getStepTitle());
            step.setStepContent(stepDetails.getStepContent());
            step.setStepOrder(stepDetails.getStepOrder());
            step.setMediaUrl(stepDetails.getMediaUrl());
            step.setMediaType(stepDetails.getMediaType());
            return ResponseEntity.ok(stepRepo.save(step));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/modules/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        if (moduleRepo.existsById(id)) {
            moduleRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
