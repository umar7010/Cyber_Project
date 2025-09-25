package com.cyberedu.controller;

import com.cyberedu.glossary.GlossaryTermRepository;
import com.cyberedu.learning.LearningModuleRepository;
import com.cyberedu.news.NewsArticleRepository;
import com.cyberedu.cheatsheet.CheatSheetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    private final GlossaryTermRepository glossaryRepo;
    private final LearningModuleRepository learningRepo;
    private final NewsArticleRepository newsRepo;
    private final CheatSheetRepository cheatSheetRepo;
    
    public HomeController(GlossaryTermRepository glossaryRepo, 
                         LearningModuleRepository learningRepo,
                         NewsArticleRepository newsRepo,
                         CheatSheetRepository cheatSheetRepo) {
        this.glossaryRepo = glossaryRepo;
        this.learningRepo = learningRepo;
        this.newsRepo = newsRepo;
        this.cheatSheetRepo = cheatSheetRepo;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        // Get recent content for homepage
        model.addAttribute("recentNews", newsRepo.findByIsPublishedTrueOrderByPublishedAtDesc().stream().limit(3).toList());
        model.addAttribute("recentCheatSheets", cheatSheetRepo.findByIsPublishedTrueOrderByCreatedAtDesc().stream().limit(3).toList());
        model.addAttribute("totalTerms", glossaryRepo.findByIsPublishedTrueOrderByTermAsc().size());
        model.addAttribute("totalModules", learningRepo.findAll().size());
        return "index";
    }
    
    @GetMapping("/glossary")
    public String glossary(Model model) {
        model.addAttribute("terms", glossaryRepo.findByIsPublishedTrueOrderByTermAsc());
        return "glossary";
    }
    
    @GetMapping("/learning")
    public String learning(Model model) {
        model.addAttribute("modules", learningRepo.findAllOrderedByModuleOrder());
        return "learning";
    }
    
    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("articles", newsRepo.findByIsPublishedTrueOrderByPublishedAtDesc());
        return "news";
    }
    
    @GetMapping("/cheatsheets")
    public String cheatSheets(Model model) {
        model.addAttribute("cheatSheets", cheatSheetRepo.findByIsPublishedTrueOrderByCreatedAtDesc());
        return "cheatsheets";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
