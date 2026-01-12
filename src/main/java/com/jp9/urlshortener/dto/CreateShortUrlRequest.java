package com.jp9.urlshortener.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateShortUrlRequest {

    @NotBlank
    @Pattern(
        regexp = "https?://.*",
        message = "URL must start with http or https"
    )
    private String originalUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String longUrl) {
        this.originalUrl = longUrl;
    }
}
