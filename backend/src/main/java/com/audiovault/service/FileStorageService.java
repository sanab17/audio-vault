package com.audiovault.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Initialize upload directory
     */
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }
    
    /**
     * Store file on disk and return the file path
     */
    public String storeFile(MultipartFile file) {
        init(); // Ensure directory exists

        try {
            // Generate unique filename to avoid collisions
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Copy file to upload directory
            Path targetLocation = Paths.get(uploadDir).resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Stored file: {} as {}", originalFilename, uniqueFilename);
            return targetLocation.toString();
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    /**
     * Load file from disk
     */
    public Path loadFile(String filePath) {
        try {
            Path file = Paths.get(filePath);
            if(Files.exists(file) && Files.isReadable(file)) {
                return file;
            }
            else {
                throw new RuntimeException("File not accessible: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file: " + filePath, e);
        }
    }

    /**
     * Delete file from disk
     */
    public boolean deleteFile(String filePath) {
        try {
            Path file = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(file);
            if (deleted) {
                log.info("Deleted file: {}", filePath);
            } else {
                log.warn("File not found for deletion: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }
}
