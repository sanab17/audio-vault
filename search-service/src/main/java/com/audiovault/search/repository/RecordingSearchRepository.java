package com.audiovault.search.repository;

import org.springframework.stereotype.Repository;

import com.audiovault.search.model.RecordingDocument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface RecordingSearchRepository extends MongoRepository<RecordingDocument, String> {
    
    // Find by original recording ID
    Optional<RecordingDocument> findByRecordingId(Long recordingId);

    // Delete by original recording ID
    void deleteByRecordingId(Long recordingId);

    // Full-text search
    List<RecordingDocument> findByFileNameContainingIgnoreCase(String fileName);

    // Search by file extension
    List<RecordingDocument> findByFileExtension(String extension);

    // Find recordings by duration range
    List<RecordingDocument> findByDurationBetween(Integer minDuration, Integer maxDuration);

    // Find recordings by upload date range
    List<RecordingDocument> findByUploadDateBetween(LocalDateTime start, LocalDateTime end);

    // Find recordings by file size range
    List<RecordingDocument> findByFileSizeBetween(Long minSize, Long maxSize);

    // Find recordings by content type
    List<RecordingDocument> findByContentType(String contentType);

    // Complex search with multiple criteria (using @Query)
    @Query("{ $and: [ " +
           "{ $or: [ { 'fileName': { $regex: ?0, $options: 'i' } }, { 'fileNameLower': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'duration': { $gte: ?1, $lte: ?2 } } " +
           "] }")
    List<RecordingDocument> searchByFileNameAndDurationRange(String fileName, Integer minDuration, Integer maxDuration);
}
