package com.cyberedu.cheatsheet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cheatsheets")
public class CheatSheetController {
    private final CheatSheetRepository cheatSheetRepo;

    public CheatSheetController(CheatSheetRepository cheatSheetRepo) {
        this.cheatSheetRepo = cheatSheetRepo;
    }

    @GetMapping
    public List<CheatSheet> getPublishedCheatSheets() {
        return cheatSheetRepo.findByIsPublishedTrueOrderByCreatedAtDesc();
    }

    @GetMapping("/category/{category}")
    public List<CheatSheet> getCheatSheetsByCategory(@PathVariable String category) {
        return cheatSheetRepo.findByCategoryOrderByCreatedAtDesc(category);
    }

    @GetMapping("/search")
    public List<CheatSheet> searchCheatSheets(@RequestParam String query) {
        return cheatSheetRepo.findByTitleContainingIgnoreCase(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheatSheet> getCheatSheetById(@PathVariable Long id) {
        Optional<CheatSheet> cheatSheet = cheatSheetRepo.findById(id);
        return cheatSheet.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CheatSheet createCheatSheet(@RequestBody CheatSheet cheatSheet) {
        return cheatSheetRepo.save(cheatSheet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheatSheet> updateCheatSheet(@PathVariable Long id, @RequestBody CheatSheet cheatSheetDetails) {
        Optional<CheatSheet> optionalCheatSheet = cheatSheetRepo.findById(id);
        if (optionalCheatSheet.isPresent()) {
            CheatSheet cheatSheet = optionalCheatSheet.get();
            cheatSheet.setTitle(cheatSheetDetails.getTitle());
            cheatSheet.setDescription(cheatSheetDetails.getDescription());
            cheatSheet.setCategory(cheatSheetDetails.getCategory());
            cheatSheet.setContent(cheatSheetDetails.getContent());
            cheatSheet.setFileType(cheatSheetDetails.getFileType());
            cheatSheet.setFileUrl(cheatSheetDetails.getFileUrl());
            cheatSheet.setIsPublished(cheatSheetDetails.getIsPublished());
            return ResponseEntity.ok(cheatSheetRepo.save(cheatSheet));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheatSheet(@PathVariable Long id) {
        if (cheatSheetRepo.existsById(id)) {
            cheatSheetRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
