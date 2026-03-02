package com.audiovault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedUrlResponse {
    
    private String url;
    private int expirySeconds;
    private String message;

    public PreSignedUrlResponse(String url, int expirySeconds) {
        this.url = url;
        this.expirySeconds = expirySeconds;
        this.message = "URL expires in " + expirySeconds + " seconds";
    }
}
