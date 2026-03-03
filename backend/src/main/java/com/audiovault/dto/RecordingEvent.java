package com.audiovault.dto;

import com.audiovault.model.Recording;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordingEvent {
    
    private String eventType;       // "CREATED" or "DELETED"
    private Long recordingId;
    private String fileName;
    private String filePath;
    private String contentType;
    private Integer duration;
    private Long fileSize;
    private LocalDateTime occurredAt;

    public static RecordingEvent created(Recording recording) {
        return new RecordingEvent(
            "CREATED",
            recording.getId(),
            recording.getFileName(),
            recording.getFilePath(),
            recording.getContentType(),
            recording.getDuration(),
            recording.getFileSize(),
            LocalDateTime.now()
        );
    }

    public static RecordingEvent deleted(Recording recording) {
        return new RecordingEvent(
            "DELETED",
            recording.getId(),
            recording.getFileName(),
            recording.getFilePath(),
            recording.getContentType(),
            recording.getDuration(),
            recording.getFileSize(),
            LocalDateTime.now()
        );
    }
}
