package com.audiovault.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recordings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recording {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Positive(message = "Duration must be positive")
    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}
