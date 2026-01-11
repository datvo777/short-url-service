package com.jp9.urlshortener.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.jp9.urlshortener.cache.ShortUrlCache;
import com.jp9.urlshortener.dto.CreateShortUrlResponse;
import com.jp9.urlshortener.entity.ShortUrlEntity;
import com.jp9.urlshortener.model.ShortUrl;
import com.jp9.urlshortener.repository.ShortUrlRepository;
import com.jp9.urlshortener.util.Base62Encoder;

@Service
public class ShortUrlService {

    private final ShortUrlCache cache;
    private final ShortUrlRepository repository;
    private final Duration ttl = Duration.ofHours(24);
    private final ShortKeyGenerator shortKeyGenerator;

    public ShortUrlService(
            ShortUrlCache cache,
            ShortUrlRepository repository,
            ShortKeyGenerator shortKeyGenerator
    ) {
        this.cache = cache;
        this.repository = repository;
        this.shortKeyGenerator = shortKeyGenerator;
    }

    // POST flow
    public CreateShortUrlResponse create(String originalUrl) {

        String shortKey = shortKeyGenerator.generate();

        ShortUrlEntity entity = new ShortUrlEntity();
        entity.setShortKey(shortKey);
        entity.setOriginalUrl(originalUrl);
        entity.setCreatedAt(Instant.now());

        repository.save(entity);

        // Optional: cache warm-up
        cache.set(shortKey, originalUrl, ttl);

        return new CreateShortUrlResponse(shortKey);
    }

    public String resolve(String shortKey) throws NotFoundException {
        // Phase 5: cache
        String url = cache.get(shortKey);
        if (url != null) {
            return url;
        }

        // Phase 4: DB
        ShortUrlEntity shortUrl = repository.findByShortKey(shortKey)
                .orElseThrow(NotFoundException::new);

        // Phase 5: populate cache
        cache.set(shortKey, shortUrl.getOriginalUrl(), ttl);
        return shortUrl.getOriginalUrl();
    }
}

