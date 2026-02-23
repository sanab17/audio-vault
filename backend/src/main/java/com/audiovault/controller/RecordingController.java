package com.audiovault.controller;

import com.audiovault.model.Recording;
import com.audiovault.service.RecordingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
@RequiredArgsConstructor
@Slf4j
public class RecordingController {
	
    private final RecordingService recordingService;

    /**
     * Create a new recording
     * POST /api/recordings
     */
    @PostMapping
    public ResponseEntity<Recording> createRecording(@Valid @RequestBody Recording recording) {
        log.info("Received request to create recording: {}", recording.getFileName());
        Recording createdRecording = recordingService.createRecording(recording);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecording);

    }

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
