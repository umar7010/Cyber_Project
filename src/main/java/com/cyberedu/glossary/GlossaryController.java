package com.cyberedu.glossary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/glossary")
public class GlossaryController {
    private final GlossaryTermRepository repo;

    public GlossaryController(GlossaryTermRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<GlossaryTerm> getAllPublishedTerms() {
        return repo.findByIsPublishedTrueOrderByTermAsc();
    }

    @GetMapping("/search")
    public List<GlossaryTerm> searchTerms(@RequestParam String query) {
        return repo.searchPublishedTerms(query);
    }

    @GetMapping("/category/{category}")
    public List<GlossaryTerm> getTermsByCategory(@PathVariable String category) {
        return repo.findByCategoryOrderByTermAsc(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlossaryTerm> getTermById(@PathVariable Long id) {
        Optional<GlossaryTerm> term = repo.findById(id);
        return term.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public GlossaryTerm addTerm(@RequestBody GlossaryTerm term) {
        return repo.save(term);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlossaryTerm> updateTerm(@PathVariable Long id, @RequestBody GlossaryTerm termDetails) {
        Optional<GlossaryTerm> optionalTerm = repo.findById(id);
        if (optionalTerm.isPresent()) {
            GlossaryTerm term = optionalTerm.get();
            term.setTerm(termDetails.getTerm());
            term.setDefinition(termDetails.getDefinition());
            term.setCategory(termDetails.getCategory());
            term.setMediaUrl(termDetails.getMediaUrl());
            term.setMediaType(termDetails.getMediaType());
            term.setExample(termDetails.getExample());
            term.setIsPublished(termDetails.getIsPublished());
            return ResponseEntity.ok(repo.save(term));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
