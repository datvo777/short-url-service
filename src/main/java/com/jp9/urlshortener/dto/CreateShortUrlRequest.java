package com.jp9.urlshortener.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateShortUrlRequest {

    @NotBlank
    @Pattern(
        regexp = "https?://.*",
        message = "URL must start with http or https"
    )
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
