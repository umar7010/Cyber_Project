package com.cyberedu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "uploads/";
    
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return uniqueFilename;
    }
    
    public String uploadFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR + subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return "/" + UPLOAD_DIR + subDirectory + "/" + uniqueFilename;
    }
    
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath.substring(1)); // Remove leading slash
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
    
    public String getFileType(String filename) {
        if (filename == null) return "UNKNOWN";
        
        String extension = filename.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg") || 
            extension.endsWith(".png") || extension.endsWith(".gif")) {
            return "IMAGE";
        } else if (extension.endsWith(".mp4") || extension.endsWith(".avi") || 
                   extension.endsWith(".mov") || extension.endsWith(".wmv")) {
            return "VIDEO";
        } else if (extension.endsWith(".pdf") || extension.endsWith(".doc") || 
                   extension.endsWith(".docx") || extension.endsWith(".txt")) {
            return "DOCUMENT";
        }
        return "UNKNOWN";
    }
}
