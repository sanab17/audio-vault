package com.audiovault.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /*
     * Initialize MinIO bucket on application startup. This ensures 
     * that the bucket exists before any file operations are performed.
     */
    @PostConstruct
    public void init() {
        try {
            // Check if the bucket already exists
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );

            if (!bucketExists) {
                // Create the bucket if it does not exist
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                log.info("Created MinIO bucket: {}", bucketName);
            } else {
                log.info("MinIO bucket already exists: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to initialize MinIO bucket: {}", bucketName, e);
            throw new RuntimeException("Failed to initialize MinIO bucket: " + bucketName, e);
        }
    }

    /*
     * Store a file in MinIO bucket and return the unique filename. The original filename 
     * is preserved as part of the unique filename to maintain file type information.
     */
    public String storeFile(MultipartFile file) {
        try {
            // Generate unique filename to avoid collisions
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";

            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Upload file to MinIO bucket
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uniqueFilename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            log.info("Stored file in MinIO bucket: {}", uniqueFilename);
            return uniqueFilename;
        } catch (Exception e) {
            log.error("Failed to store file in MinIO", e);
            throw new RuntimeException("Failed to store file in MinIO", e);
        }
    }

    /**
     * Get file from MinIO bucket as an InputStream. This allows for 
     * efficient streaming of large files without loading them entirely into memory.
     */
    public InputStream getFileStream(String filename) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to load file from MinIO: {}", filename, e);
            throw new RuntimeException("Failed to load file from MinIO: " + filename, e);
        }
    }

    /*
     * Generate a pre-signed URL for a file in MinIO bucket. This URL 
     * can be used to access the file directly from MinIO without going 
     * through the application, which is useful for serving files to clients.
     * The URL is valid for 1 hour, after which it will expire and no longer be usable.
     */
    public String getPreSignedUrl(String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(filename)
                    .expiry(1, TimeUnit.HOURS)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to generate pre-signed URL for file: {}", filename, e);
            throw new RuntimeException("Failed to generate pre-signed URL for file: " + filename, e);
        }
    }

    /*
     * Delete a file from MinIO bucket. This method returns true if the file was successfully 
     * deleted, and false if there was an error during deletion. It logs the outcome of the 
     * deletion attempt for better traceability.
     */
    public boolean deleteFile(String filename) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
            log.info("Deleted file from MinIO bucket: {}", filename);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file from MinIO: {}", filename, e);
            return false;
        }
    }

    /*
     * Check if a file exists in MinIO bucket. This method uses the statObject 
     * method to check for the existence of the file. If the file exists, it returns true; 
     * if an exception occurs (e.g., file not found), it logs a warning and returns false.
     */
    public boolean fileExists(String filename) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
            return true; // If statObject succeeds, the file exists
        } catch (Exception e) {
            log.warn("File does not exist in MinIO bucket: {}", filename);
            return false; // If an exception occurs, the file likely does not exist
        }
    }
}

/*

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // 
    //  Initialize upload directory
    //  
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
    
    // 
    //  Store file on disk and return the file path
    //  
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

    // 
    //  Load file from disk
    //  
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

    // 
    //  Delete file from disk
    //  
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
*/