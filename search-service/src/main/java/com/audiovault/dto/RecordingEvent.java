// package com.audiovault.search.dto;
package com.audiovault.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String eventType;   // CREATED, UPDATED, DELETED
}
