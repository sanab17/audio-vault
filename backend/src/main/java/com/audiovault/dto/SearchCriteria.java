package com.audiovault.dto;

import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SearchCriteria {
    private String fileName;
    private String contentType; // e.g. "audio/mp3"
    private Integer minDuration; // seconds
    private Integer maxDuration;
    private Long minFileSize;         // bytes
    private Long maxFileSize;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime uploadedAfter;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime uploadedBefore;
}
