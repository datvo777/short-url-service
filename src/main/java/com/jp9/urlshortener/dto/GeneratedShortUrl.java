package com.jp9.urlshortener.dto;

public class GeneratedShortUrl {
    private final long id;
    private final String shortKey;

    public GeneratedShortUrl(long id, String shortKey) {
        this.id = id;
        this.shortKey = shortKey;
    }

    public long getId() { return id; }
    public String getShortKey() { return shortKey; }
}
