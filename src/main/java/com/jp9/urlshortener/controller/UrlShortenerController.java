package com.jp9.urlshortener.controller;

import java.net.URI;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jp9.urlshortener.dto.CreateShortUrlRequest;
import com.jp9.urlshortener.dto.CreateShortUrlResponse;
import com.jp9.urlshortener.service.ShortUrlService;

import jakarta.validation.Valid;

@RestController
public class UrlShortenerController {
    private final ShortUrlService shortUrlService;

    public UrlShortenerController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping
    public ResponseEntity<CreateShortUrlResponse> shorten(
            @Valid @RequestBody CreateShortUrlRequest request
    ) {
        CreateShortUrlResponse shortUrl = shortUrlService.create(request.getOriginalUrl());

        return ResponseEntity.ok(
                shortUrl
        );
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) throws ChangeSetPersister.NotFoundException {
        String originalUrl = shortUrlService.resolve(shortKey);
        if (originalUrl != null) {
            return ResponseEntity.status(302)
                    .location(URI.create(originalUrl))
                    .build();
        }
        return ResponseEntity.notFound().build();
    }
}

