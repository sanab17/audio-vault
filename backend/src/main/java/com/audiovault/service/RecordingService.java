package com.audiovault.service;

import com.audiovault.model.Recording;
import com.audiovault.repository.RecordingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordingService {
    
    private final RecordingRepository recordingRepository;

    /**
     * Create a new recording
     */
    @Transactional
    public Recording createRecording(Recording recording) {
        log.info("Creating new recording: {}", recording.getFileName());
        Recording savedRecording = recordingRepository.save(recording);
        log.info("Recording created with ID: {}", savedRecording.getId());
        return savedRecording;
    }

    /**
     * Get all recordings
     */
    @Transactional(readOnly = true)
    public List<Recording> getAllRecordings() {
        log.info("Fetching all recordings");
        return recordingRepository.findAll();
    }

    /**
     * Get recording by ID
     */
    @Transactional(readOnly = true)
    public Optional<Recording> getRecordingById(Long id) {
        log.info("Fetching recording with ID: {}", id);
        return recordingRepository.findById(id);
    }

    /**
     * Search recordings by file name
     */
    @Transactional(readOnly = true)
    public List<Recording> searchByFileName(String fileName) {
        log.info("Searching recordings by file name: {}", fileName);
        return recordingRepository.findByFileNameContainingIgnoreCase(fileName);
    }

    /**
     * Delete recording by ID
     */
    @Transactional
    public boolean deleteRecording(Long id) {
        log.info("Deleting recording with ID: {}", id);
        if (recordingRepository.existsById(id)) {
            recordingRepository.deleteById(id);
            log.info("Recording deleted successfully with ID: {}", id);
            return true;
        }
        log.warn("Recording not found with ID: {}", id);
        return false;
    }
}