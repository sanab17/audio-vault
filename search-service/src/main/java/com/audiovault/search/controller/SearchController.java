package com.audiovault.search.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audiovault.search.model.RecordingDocument;
import com.audiovault.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    
    private final SearchService searchService;

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Search Service");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all recordings
     */
    @GetMapping("/recordings")
    public ResponseEntity<List<RecordingDocument>> getAllRecordings() {
        log.info("Received request to get all recordings");
        List<RecordingDocument> recordings = searchService.getAllRecordings();
        return ResponseEntity.ok(recordings);
    }

    /**
     * Get recording by ID
     */
    @GetMapping("/recordings/{id}")
    public ResponseEntity<RecordingDocument> getRecordingById(@PathVariable Long id) {
        log.info("Received request to get recording ID: {}", id);
        return searchService.getRecordingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search recordings by file name
     * GET /api/search/recordings/by-name?fileName=meeting
     */
    @GetMapping("/recordings/by-name")
    public ResponseEntity<List<RecordingDocument>> searchByFileName(
            @RequestParam String fileName) {
        log.info("Searching recordings by fileName: {}", fileName);
        List<RecordingDocument> recordings = searchService.searchByFileName(fileName);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Search recordings by file extension
     * GET /api/search/recordings/by-extension?extension=mp3
     */
    @GetMapping("/recordings/by-extension")
    public ResponseEntity<List<RecordingDocument>> searchByExtension(
            @RequestParam String extension) {
        log.info("Searching recordings by extension: {}", extension);
        List<RecordingDocument> recordings = searchService.searchByExtension(extension);
        return ResponseEntity.ok(recordings);
    }

   /**
     * Search recordings by duration range
     * GET /api/search/recordings/by-duration?min=60&max=300
     */
    @GetMapping("/recordings/by-duration")
    public ResponseEntity<List<RecordingDocument>> searchByDuration(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        log.info("Searching recordings by duration: {} - {}", min, max);
        List<RecordingDocument> recordings = searchService.searchByDurationRange(min, max);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Search recordings by upload date range
     * GET /api/search/recordings/by-date?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
     */
    @GetMapping("/recordings/by-date")
    public ResponseEntity<List<RecordingDocument>> searchByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("Searching recordings by date: {} - {}", start, end);
        List<RecordingDocument> recordings = searchService.searchByDateRange(start, end);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Search recordings by file size range (in bytes)
     * GET /api/search/recordings/by-size?min=1000000&max=10000000
     */
    @GetMapping("/recordings/by-size")
    public ResponseEntity<List<RecordingDocument>> searchByFileSize(
            @RequestParam Long min,
            @RequestParam Long max) {
        log.info("Searching recordings by file size: {} - {}", min, max);
        List<RecordingDocument> recordings = searchService.searchByFileSizeRange(min, max);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Complex search: file name + duration range
     * GET /api/search/recordings/complex?fileName=meeting&minDuration=60&maxDuration=300
     */
    @GetMapping("/recordings/complex")
    public ResponseEntity<List<RecordingDocument>> complexSearch(
            @RequestParam String fileName,
            @RequestParam Integer minDuration,
            @RequestParam Integer maxDuration) {
        log.info("Complex search - fileName: {}, duration: {} - {}", fileName, minDuration, maxDuration);
        List<RecordingDocument> recordings = searchService.complexSearch(fileName, minDuration, maxDuration);
        return ResponseEntity.ok(recordings);
    }

    /**
     * Get search statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.info("Fetching search statistics");
        
        List<RecordingDocument> allRecordings = searchService.getAllRecordings();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecordings", allRecordings.size());
        stats.put("timestamp", LocalDateTime.now());
        
        // Calculate total file size
        long totalSize = allRecordings.stream()
                .mapToLong(RecordingDocument::getFileSize)
                .sum();
        stats.put("totalFileSizeBytes", totalSize);
        stats.put("totalFileSizeMB", totalSize / (1024.0 * 1024.0));
        
        // Calculate average duration
        double avgDuration = allRecordings.stream()
                .mapToInt(RecordingDocument::getDuration)
                .average()
                .orElse(0.0);
        stats.put("averageDurationSeconds", avgDuration);
        
        return ResponseEntity.ok(stats);
    }
}
