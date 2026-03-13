package com.audiovault.search.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "recordings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordingDocument {
    
    @Id
    private String id;  // MongoDB uses String IDs

    private Long recordingId;   // Original ID from PostgreSQL

    @TextIndexed(weight = 3)    // Higher weight for file name in search
    private String fileName;

    private String filePath;

    private Integer duration;

    private Long fileSize;

    private String contentType;

    private LocalDateTime uploadDate;

    private LocalDateTime lastModified;

    // Denormalized fields for search optimization
    @TextIndexed
    private String fileNameLower;   // For case-insensitive search

    private String fileExtension;

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fileNameLower = fileName != null ? fileName.toLowerCase() : null;

        // Extract file extension
        if (fileName != null && fileName.contains(".")) {
            this.fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
    }
}
