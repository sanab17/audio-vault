package com.audiovault.search.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import com.audiovault.search.dto.RecordingEvent;
import com.audiovault.dto.RecordingEvent;
import com.audiovault.search.model.RecordingDocument;
import com.audiovault.search.repository.RecordingSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    
    private final RecordingSearchRepository repository;

    /**
     * Index a new recording from Kafka event
     */
    @Transactional
    public RecordingDocument indexRecording(RecordingEvent event) {
        log.info("Indexing recording: {}", event.getFileName());
        
        RecordingDocument document = new RecordingDocument();
        document.setRecordingId(event.getId());
        document.setFileName(event.getFileName());
        document.setFilePath(event.getFilePath());
        document.setDuration(event.getDuration());
        document.setFileSize(event.getFileSize());
        document.setContentType(event.getContentType());
        document.setUploadDate(event.getUploadDate());
        document.setLastModified(LocalDateTime.now());
        
        return repository.save(document);
    }

    /**
     * Update existing recording from Kafka event
     */
    @Transactional
    public RecordingDocument updateRecording(RecordingEvent event) {
        log.info("Updating recording ID: {}", event.getId());
        
        Optional<RecordingDocument> existingDoc = repository.findByRecordingId(event.getId());
        
        if (existingDoc.isPresent()) {
            RecordingDocument document = existingDoc.get();
            document.setFileName(event.getFileName());
            document.setFilePath(event.getFilePath());
            document.setDuration(event.getDuration());
            document.setFileSize(event.getFileSize());
            document.setContentType(event.getContentType());
            document.setLastModified(LocalDateTime.now());
            
            return repository.save(document);
        } else {
            log.warn("Recording ID {} not found for update, creating new document", event.getId());
            return indexRecording(event);
        }
    }

    /**
     * Delete recording from search index
     */
    @Transactional
    public void deleteRecording(Long recordingId) {
        log.info("Deleting recording ID: {}", recordingId);
        repository.deleteByRecordingId(recordingId);
    }

    /**
     * Search recordings by file name
     */
    public List<RecordingDocument> searchByFileName(String fileName) {
        log.info("Searching recordings by fileName: {}", fileName);
        return repository.findByFileNameContainingIgnoreCase(fileName);
    }

    /**
     * Search recordings by file extension
     */
    public List<RecordingDocument> searchByExtension(String extension) {
        log.info("Searching recordings by extension: {}", extension);
        return repository.findByFileExtension(extension.toLowerCase());
    }

    /**
     * Search recordings by duration range
     */
    public List<RecordingDocument> searchByDurationRange(Integer minDuration, Integer maxDuration) {
        log.info("Searching recordings by duration range: {} - {}", minDuration, maxDuration);
        return repository.findByDurationBetween(minDuration, maxDuration);
    }

    /**
     * Search recordings by upload date range
     */
    public List<RecordingDocument> searchByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Searching recordings by date range: {} - {}", start, end);
        return repository.findByUploadDateBetween(start, end);
    }

    /**
     * Search recordings by file size range
     */
    public List<RecordingDocument> searchByFileSizeRange(Long minSize, Long maxSize) {
        log.info("Searching recordings by file size range: {} - {}", minSize, maxSize);
        return repository.findByFileSizeBetween(minSize, maxSize);
    }

    /**
     * Get all recordings
     */
    public List<RecordingDocument> getAllRecordings() {
        log.info("Fetching all recordings from search index");
        return repository.findAll();
    }

    /**
     * Get recording by ID
     */
    public Optional<RecordingDocument> getRecordingById(Long recordingId) {
        log.info("Fetching recording by ID: {}", recordingId);
        return repository.findByRecordingId(recordingId);
    }

    /**
     * Complex search: file name + duration range
     */
    public List<RecordingDocument> complexSearch(String fileName, Integer minDuration, Integer maxDuration) {
        log.info("Complex search - fileName: {}, duration: {} - {}", fileName, minDuration, maxDuration);
        return repository.searchByFileNameAndDurationRange(fileName, minDuration, maxDuration);
    }
}
