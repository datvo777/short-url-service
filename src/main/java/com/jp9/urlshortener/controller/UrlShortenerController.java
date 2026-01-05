package com.jp9.urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jp9.urlshortener.dto.CreateShortUrlRequest;
import com.jp9.urlshortener.dto.CreateShortUrlResponse;

import jakarta.validation.Valid;

@RestController
public class UrlShortenerController {

    @PostMapping("/shorten")
    public ResponseEntity<CreateShortUrlResponse> shorten(
            @Valid @RequestBody CreateShortUrlRequest request
    ) {
        // Phase 3: ID generation
        // Phase 4: persistence
        // Phase 5: caching

        return ResponseEntity.ok(
                new CreateShortUrlResponse("https://short.ly/placeholder")
        );
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        // Phase 5: cache + redirect logic
        return ResponseEntity.notFound().build();
    }
}

