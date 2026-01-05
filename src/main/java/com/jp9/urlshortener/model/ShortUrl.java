package com.jp9.urlshortener.model;

import java.time.Instant;

public class ShortUrl {

    private String shortKey;
    private String longUrl;
    private Instant createdAt;

    public ShortUrl(String shortKey, String longUrl, Instant createdAt) {
        this.shortKey = shortKey;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
    }

    public String getShortKey() {
        return shortKey;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
