package com.audiovault.controller;

import com.audiovault.model.Recording;
import com.audiovault.service.FileStorageService;
import com.audiovault.service.RecordingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recordings")
@RequiredArgsConstructor
@Slf4j
public class RecordingController {

    private final RecordingService recordingService;
    private final FileStorageService fileStorageService;

    /*
     * Upload a recording file
     * POST /api/recordings/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Recording> uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("duration") Integer duration) {
        log.info("Received file upload request: {}", file.getOriginalFilename());
        log.info("File content type: {}", file.getContentType());

        // Validate file is not empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate file type (audio files only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            log.warn("Invalid file type: {}", contentType);
            return ResponseEntity.badRequest().build();
        }

        Recording recording = recordingService.createRecording(file, duration);
        return ResponseEntity.status(HttpStatus.CREATED).body(recording);
    }

    /**
     * Create a new recording (metadata only - for backward compatibility)
     * POST /api/recordings
     */
    @PostMapping
    public ResponseEntity<Recording> createRecording(@Valid @RequestBody Recording recording) {
        log.info("Received request to create recording: {}", recording.getFileName());
        Recording createdRecording = recordingService.createRecording(recording);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecording);

    }

    /*
     * Download a recording file
     * GET /api/recordings/{id}/download
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadRecording(@PathVariable Long id) {
        log.info("Received download request for recording ID: {}", id);

        Optional<Recording> recordingOpt = recordingService.getRecordingById(id);

        if (recordingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recording recording = recordingOpt.get();

        try {
            Path filePath = fileStorageService.loadFile(recording.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(recording.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + recording.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error loading file for recording {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Download a recording file
     * GET /api/recordings/{id}/download
     */
    /*
     * @GetMapping("/{id}/download")
     * public ResponseEntity<Resource> downloadRecording(@PathVariable Long id) {
     * log.info("Received download request for recording ID: {}", id);
     * 
     * return recordingService.getRecordingById(id)
     * .<ResponseEntity<Resource>>map(recording -> {
     * try {
     * Path filePath = fileStorageService.loadFile(recording.getFilePath());
     * Resource resource = new UrlResource(filePath.toUri());
     * 
     * return ResponseEntity.ok()
     * .contentType(MediaType.parseMediaType(recording.getContentType()))
     * .header(HttpHeaders.CONTENT_DISPOSITION,
     * "attachment; filename=\"" + recording.getFileName() + "\"")
     * .body(resource);
     * } catch (Exception e) {
     * log.error("Error loading file for recording {}", id, e);
     * return ResponseEntity.<Resource>notFound().build();
     * }
     * })
     * .orElse(ResponseEntity.notFound().build());
     * }
     */

    /**
     * Get all recordings
     * GET /api/recordings
     */
    @GetMapping
    public ResponseEntity<List<Recording>> getAllRecordings() {
        log.info("Received request to get all recordings");
        List<Recording> recordings = recordingService.getAllRecordings();
        return ResponseEntity.ok(recordings);
    }

    /**
     * Get recording by ID
     * GET /api/recordings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recording> getRecordingById(@PathVariable Long id) {
        log.info("Received request to get recording with ID: {}", id);
        return recordingService.getRecordingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search recordings by file name
     * GET /api/recordings/search?fileName=xyz
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recording>> searchRecordings(@RequestParam String fileName) {
        log.info("Received request to search recordings by file name: {}", fileName);
        List<Recording> recordings = recordingService.searchByFileName(fileName);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Delete recordings by ID
     * DELETE /api/recordings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecording(@PathVariable Long id) {
        log.info("Received request to delete recording with ID: {}", id);
        boolean deleted = recordingService.deleteRecording(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
