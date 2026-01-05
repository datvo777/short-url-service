package com.jp9.urlshortener.dto;


public class CreateShortUrlResponse {

    private String shortUrl;

    public CreateShortUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}