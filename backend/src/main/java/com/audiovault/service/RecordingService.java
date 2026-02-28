package com.audiovault.service;

import com.audiovault.model.Recording;
import com.audiovault.repository.RecordingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordingService {

    private final RecordingRepository recordingRepository;
    private final FileStorageService fileStorageService;

    /**
     * Create a new recording with file upload
     */
    public Recording createRecording(MultipartFile file, Integer duration) {
        log.info("Creating new recording: {}", file.getOriginalFilename());

        // Store file in MinIO (returns object name, not path)
        String objectName = fileStorageService.storeFile(file);

        // Create recording entity
        Recording recording = new Recording();
        recording.setFileName(file.getOriginalFilename());
        recording.setFilePath(objectName); // Storw MinIO object name
        recording.setDuration(duration);
        recording.setFileSize(file.getSize());
        recording.setContentType(file.getContentType());

        Recording savedRecording = recordingRepository.save(recording);
        log.info("Recording created with ID: {}", savedRecording.getId());

        return savedRecording;
    }

    /**
     * Create a new recording (without file - for backward compatibility)
     */
    @Transactional
    public Recording createRecording(Recording recording) {
        log.info("Creating new recording in database: {}", recording.getFileName());
        Recording savedRecording = recordingRepository.save(recording);
        log.info("Recording created in database with ID: {}", savedRecording.getId());
        return savedRecording;
    }

    /**
     * Get all recordings
     */
    @Transactional(readOnly = true)
    public List<Recording> getAllRecordings() {
        log.info("Fetching all recordings from database");
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
     * Delete recording by ID (also deletes the file)
     */
    @Transactional
    public boolean deleteRecording(Long id) {
        log.info("Deleting recording from database with ID: {}", id);

        Optional<Recording> recordingOpt = recordingRepository.findById(id);
        if (recordingOpt.isPresent()) {

            // Delete file from disk
            Recording recording = recordingOpt.get();
            if (recording.getFilePath() != null) {
                fileStorageService.deleteFile(recording.getFilePath());
            }

            // Delete from database
            recordingRepository.deleteById(id);
            log.info("Recording deleted from database successfully with ID: {}", id);
            return true;
        }

        log.warn("Recording not found with ID: {}", id);
        return false;
    }
}
