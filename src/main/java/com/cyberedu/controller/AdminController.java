package com.cyberedu.controller;

import com.cyberedu.glossary.GlossaryTerm;
import com.cyberedu.glossary.GlossaryTermRepository;
import com.cyberedu.learning.LearningModule;
import com.cyberedu.learning.LearningModuleRepository;
import com.cyberedu.learning.LearningStep;
import com.cyberedu.learning.LearningStepRepository;
import com.cyberedu.news.NewsArticle;
import com.cyberedu.news.NewsArticleRepository;
import com.cyberedu.cheatsheet.CheatSheet;
import com.cyberedu.cheatsheet.CheatSheetRepository;
import com.cyberedu.inquiry.Inquiry;
import com.cyberedu.inquiry.InquiryRepository;
import com.cyberedu.inquiry.InquiryStatus;
import com.cyberedu.service.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final GlossaryTermRepository glossaryRepo;
    private final LearningModuleRepository learningRepo;
    private final LearningStepRepository stepRepo;
    private final NewsArticleRepository newsRepo;
    private final CheatSheetRepository cheatSheetRepo;
    private final InquiryRepository inquiryRepo;
    private final FileUploadService fileUploadService;
    
    public AdminController(GlossaryTermRepository glossaryRepo,
                          LearningModuleRepository learningRepo,
                          LearningStepRepository stepRepo,
                          NewsArticleRepository newsRepo,
                          CheatSheetRepository cheatSheetRepo,
                          InquiryRepository inquiryRepo,
                          FileUploadService fileUploadService) {
        this.glossaryRepo = glossaryRepo;
        this.learningRepo = learningRepo;
        this.stepRepo = stepRepo;
        this.newsRepo = newsRepo;
        this.cheatSheetRepo = cheatSheetRepo;
        this.inquiryRepo = inquiryRepo;
        this.fileUploadService = fileUploadService;
    }
    
    @GetMapping("")
    public String adminDashboard(Model model) {
        model.addAttribute("totalTerms", glossaryRepo.count());
        model.addAttribute("totalModules", learningRepo.count());
        model.addAttribute("totalNews", newsRepo.count());
        model.addAttribute("totalCheatSheets", cheatSheetRepo.count());
        model.addAttribute("pendingInquiries", inquiryRepo.findByStatus(InquiryStatus.PENDING).size());
        model.addAttribute("recentInquiries", inquiryRepo.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }
    
    // Glossary Management
    @GetMapping("/glossary")
    public String adminGlossary(Model model) {
        model.addAttribute("terms", glossaryRepo.findAll());
        return "admin/glossary";
    }
    
    @GetMapping("/glossary/add")
    public String newGlossaryTerm(Model model) {
        model.addAttribute("term", new GlossaryTerm());
        return "admin/glossary-form";
    }

    @GetMapping("/glossary/new")
    public String newGlossaryTermAlias(Model model) {
        model.addAttribute("term", new GlossaryTerm());
        return "admin/glossary-form";
    }
    
    @GetMapping("/glossary/edit/{id}")
    public String editGlossaryTerm(@PathVariable Long id, Model model) {
        Optional<GlossaryTerm> term = glossaryRepo.findById(id);
        if (term.isPresent()) {
            model.addAttribute("term", term.get());
            return "admin/glossary-form";
        }
        return "redirect:/admin/glossary";
    }
    
    @PostMapping("/glossary")
    public String saveGlossaryTerm(@ModelAttribute GlossaryTerm term,
                                  @RequestParam(required = false) MultipartFile mediaFile,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (mediaFile != null && !mediaFile.isEmpty()) {
                String fileName = fileUploadService.storeFile(mediaFile);
                term.setMediaUrl("/uploads/" + fileName);
                term.setMediaType(mediaFile.getContentType());
            }
            
            // Check if this is an update or create operation
            if (term.getId() == null) {
                // Creating new term
                term.setCreatedAt(LocalDateTime.now());
                redirectAttributes.addFlashAttribute("success", "Glossary term created successfully!");
            } else {
                // Updating existing term - preserve original creation date
                Optional<GlossaryTerm> existingTerm = glossaryRepo.findById(term.getId());
                if (existingTerm.isPresent()) {
                    GlossaryTerm existing = existingTerm.get();
                    term.setCreatedAt(existing.getCreatedAt());
                }
                redirectAttributes.addFlashAttribute("success", "Glossary term updated successfully!");
            }
            
            term.setUpdatedAt(LocalDateTime.now());
            glossaryRepo.save(term);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving glossary term: " + e.getMessage());
        }
        return "redirect:/admin/glossary";
    }
    
    @PostMapping("/glossary/delete/{id}")
    public String deleteGlossaryTerm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            glossaryRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Glossary term deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting glossary term: " + e.getMessage());
        }
        return "redirect:/admin/glossary";
    }
    
    // Learning Modules Management
    @GetMapping("/learning")
    public String adminLearning(Model model) {
        model.addAttribute("modules", learningRepo.findAll());
        return "admin/learning";
    }
    
    @GetMapping("/learning/add")
    public String newLearningModule(Model model) {
        model.addAttribute("module", new LearningModule());
        return "admin/learning-form";
    }
    
    @GetMapping("/learning/edit/{id}")
    public String editLearningModule(@PathVariable Long id, Model model) {
        Optional<LearningModule> module = learningRepo.findById(id);
        if (module.isPresent()) {
            model.addAttribute("module", module.get());
            model.addAttribute("steps", stepRepo.findByLearningModuleOrderByStepOrder(module.get()));
            return "admin/learning-form";
        }
        return "redirect:/admin/learning";
    }
    
    @PostMapping("/learning")
    public String saveLearningModule(@ModelAttribute LearningModule module, RedirectAttributes redirectAttributes) {
        try {
            if (module.getId() == null) {
                module.setCreatedAt(LocalDateTime.now());
            }
            module.setUpdatedAt(LocalDateTime.now());
            
            learningRepo.save(module);
            redirectAttributes.addFlashAttribute("success", "Learning module saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving learning module: " + e.getMessage());
        }
        return "redirect:/admin/learning";
    }
    
    @PostMapping("/learning/delete/{id}")
    public String deleteLearningModule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            learningRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Learning module deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting learning module: " + e.getMessage());
        }
        return "redirect:/admin/learning";
    }
    
    // News Management
    @GetMapping("/news")
    public String adminNews(Model model) {
        model.addAttribute("articles", newsRepo.findAll());
        return "admin/news";
    }
    
    @GetMapping("/news/add")
    public String newNewsArticle(Model model) {
        model.addAttribute("article", new NewsArticle());
        return "admin/news-form";
    }
    
    @GetMapping("/news/edit/{id}")
    public String editNewsArticle(@PathVariable Long id, Model model) {
        Optional<NewsArticle> article = newsRepo.findById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return "admin/news-form";
        }
        return "redirect:/admin/news";
    }
    
    @PostMapping("/news")
    public String saveNewsArticle(@ModelAttribute NewsArticle article,
                                 @RequestParam(required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = fileUploadService.storeFile(imageFile);
                article.setImageUrl("/uploads/" + fileName);
            }
            
            // Check if this is an update or create operation
            if (article.getId() == null) {
                // Creating new article
                article.setCreatedAt(LocalDateTime.now());
                article.setPublishedAt(LocalDateTime.now());
                redirectAttributes.addFlashAttribute("success", "News article created successfully!");
            } else {
                // Updating existing article - preserve original creation and publication dates
                Optional<NewsArticle> existingArticle = newsRepo.findById(article.getId());
                if (existingArticle.isPresent()) {
                    NewsArticle existing = existingArticle.get();
                    article.setCreatedAt(existing.getCreatedAt());
                    if (existing.getPublishedAt() != null) {
                        article.setPublishedAt(existing.getPublishedAt());
                    } else if (article.getIsPublished()) {
                        article.setPublishedAt(LocalDateTime.now());
                    }
                }
                redirectAttributes.addFlashAttribute("success", "News article updated successfully!");
            }
            
            article.setUpdatedAt(LocalDateTime.now());
            newsRepo.save(article);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving news article: " + e.getMessage());
        }
        return "redirect:/admin/news";
    }
    
    @PostMapping("/news/delete/{id}")
    public String deleteNewsArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "News article deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting news article: " + e.getMessage());
        }
        return "redirect:/admin/news";
    }
    
    // Cheat Sheets Management
    @GetMapping("/cheatsheets")
    public String adminCheatSheets(Model model) {
        model.addAttribute("cheatSheets", cheatSheetRepo.findAll());
        return "admin/cheatsheets";
    }
    
    @GetMapping("/cheatsheets/add")
    public String newCheatSheet(Model model) {
        model.addAttribute("cheatSheet", new CheatSheet());
        return "admin/cheatsheet-form";
    }
    
    @GetMapping("/cheatsheets/edit/{id}")
    public String editCheatSheet(@PathVariable Long id, Model model) {
        Optional<CheatSheet> cheatSheet = cheatSheetRepo.findById(id);
        if (cheatSheet.isPresent()) {
            model.addAttribute("cheatSheet", cheatSheet.get());
            return "admin/cheatsheet-form";
        }
        return "redirect:/admin/cheatsheets";
    }
    
    @PostMapping("/cheatsheets")
    public String saveCheatSheet(@ModelAttribute CheatSheet cheatSheet,
                                @RequestParam(required = false) MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.storeFile(file);
                cheatSheet.setFileUrl("/uploads/" + fileName);
                cheatSheet.setFileType(file.getContentType());
            }
            
            if (cheatSheet.getId() == null) {
                cheatSheet.setCreatedAt(LocalDateTime.now());
            }
            cheatSheet.setUpdatedAt(LocalDateTime.now());
            
            cheatSheetRepo.save(cheatSheet);
            redirectAttributes.addFlashAttribute("success", "Cheat sheet saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving cheat sheet: " + e.getMessage());
        }
        return "redirect:/admin/cheatsheets";
    }
    
    @PostMapping("/cheatsheets/delete/{id}")
    public String deleteCheatSheet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cheatSheetRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Cheat sheet deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting cheat sheet: " + e.getMessage());
        }
        return "redirect:/admin/cheatsheets";
    }
    
    // Inquiries Management
    @GetMapping("/inquiries")
    public String adminInquiries(Model model) {
        model.addAttribute("inquiries", inquiryRepo.findAll());
        return "admin/inquiries";
    }
    
    @GetMapping("/inquiries/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        Optional<Inquiry> inquiry = inquiryRepo.findById(id);
        if (inquiry.isPresent()) {
            model.addAttribute("inquiry", inquiry.get());
            return "admin/inquiry-detail";
        }
        return "redirect:/admin/inquiries";
    }
    
    @PostMapping("/inquiries/{id}/respond")
    public String respondToInquiry(@PathVariable Long id,
                                  @RequestParam String adminResponse,
                                  RedirectAttributes redirectAttributes) {
        try {
            Optional<Inquiry> inquiryOpt = inquiryRepo.findById(id);
            if (inquiryOpt.isPresent()) {
                Inquiry inquiry = inquiryOpt.get();
                inquiry.setAdminResponse(adminResponse);
                inquiry.setStatus(InquiryStatus.RESPONDED);
                inquiry.setRespondedAt(LocalDateTime.now());
                inquiryRepo.save(inquiry);
                redirectAttributes.addFlashAttribute("success", "Response sent successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending response: " + e.getMessage());
        }
        return "redirect:/admin/inquiries";
    }
}
