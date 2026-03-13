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
    
    private Long id;
    private String fileName;
    private String filePath;
    private Integer duration;
    private Long fileSize;
    private String contentType;
    private LocalDateTime uploadDate;
    private String eventType;       // "CREATED" or "DELETED"

    // Factory method for CREATED events
    public static RecordingEvent created(Recording recording) {
        RecordingEvent event = new RecordingEvent();
        event.setId(recording.getId());
        event.setFileName(recording.getFileName());
        event.setFilePath(recording.getFilePath());
        event.setDuration(recording.getDuration());
        event.setFileSize(recording.getFileSize());
        event.setContentType(recording.getContentType());
        event.setUploadDate(recording.getUploadDate());
        event.setEventType("CREATED");
        return event;
    }

    // Factory method for DELETED events
    public static RecordingEvent deleted(Recording recording) {
        RecordingEvent event = new RecordingEvent();
        event.setId(recording.getId());
        event.setFileName(recording.getFileName());
        event.setFilePath(recording.getFilePath());
        event.setDuration(recording.getDuration());
        event.setFileSize(recording.getFileSize());
        event.setContentType(recording.getContentType());
        event.setUploadDate(recording.getUploadDate());
        event.setEventType("DELETED");
        return event;
    }

}
