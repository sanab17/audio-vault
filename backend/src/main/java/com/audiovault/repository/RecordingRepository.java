package com.audiovault.repository;

import com.audiovault.model.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository extends JpaRepository<Recording, Long> {
    
    // Spring Data JPA will automatically implement these methods

    // Find recordings by file name (partial match)
    List<Recording> findByFileNameContainingIgnoreCase(String fileName);

    // Find recordings with duration greater than specified value
    List<Recording> findByDurationGreaterThan(Integer duration);
}
