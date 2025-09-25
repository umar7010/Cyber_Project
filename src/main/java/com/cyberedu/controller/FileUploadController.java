package com.cyberedu.controller;

import com.cyberedu.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    @PostMapping("/glossary")
    public ResponseEntity<Map<String, String>> uploadGlossaryMedia(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileUploadService.uploadFile(file, "glossary");
            String fileType = fileUploadService.getFileType(file.getOriginalFilename());
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("fileType", fileType);
            response.put("originalName", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/learning")
    public ResponseEntity<Map<String, String>> uploadLearningMedia(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileUploadService.uploadFile(file, "learning");
            String fileType = fileUploadService.getFileType(file.getOriginalFilename());
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("fileType", fileType);
            response.put("originalName", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/news")
    public ResponseEntity<Map<String, String>> uploadNewsImage(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileUploadService.uploadFile(file, "news");
            String fileType = fileUploadService.getFileType(file.getOriginalFilename());
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("fileType", fileType);
            response.put("originalName", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/cheatsheets")
    public ResponseEntity<Map<String, String>> uploadCheatSheetFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileUploadService.uploadFile(file, "cheatsheets");
            String fileType = fileUploadService.getFileType(file.getOriginalFilename());
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("fileType", fileType);
            response.put("originalName", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
