package com.jp9.urlshortener.entity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "short_urls")
public class ShortUrlEntity {

    @Id
    private Long id;

    @Column(name = "short_key", nullable = false, unique = true)
    private String shortKey;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    private Instant createdAt;
    private Instant expiresAt;
    private Long accessCount;
}
