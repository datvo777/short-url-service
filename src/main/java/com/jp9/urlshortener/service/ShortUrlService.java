package com.jp9.urlshortener.service;

import java.time.Duration;
import java.time.Instant;
import io.micrometer.core.instrument.*;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.jp9.urlshortener.cache.ShortUrlCache;
import com.jp9.urlshortener.dto.CreateShortUrlResponse;
import com.jp9.urlshortener.dto.GeneratedShortUrl;
import com.jp9.urlshortener.entity.ShortUrlEntity;
import com.jp9.urlshortener.repository.ShortUrlRepository;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShortUrlService {

    private final ShortUrlCache cache;
    private final ShortUrlRepository repository;
    private final Duration ttl = Duration.ofHours(24);
    private final ShortKeyGenerator shortKeyGenerator;

    private final Timer cacheTimer;
    private final Timer dbTimer;
    private final Counter cacheHit;
    private final Counter cacheMiss;

    public ShortUrlService(
            ShortUrlCache cache,
            ShortUrlRepository repository,
            ShortKeyGenerator shortKeyGenerator,
            MeterRegistry meterRegistry
    ) {
        this.cache = cache;
        this.repository = repository;
        this.shortKeyGenerator = shortKeyGenerator;

        // 🔥 Metrics are DEFINED here
        this.cacheTimer = Timer.builder("shorturl.cache.time")
                .description("Time spent reading from cache")
                .register(meterRegistry);

        this.dbTimer = Timer.builder("shorturl.db.time")
                .description("Time spent querying database")
                .register(meterRegistry);

        this.cacheHit = Counter.builder("shorturl.cache.hit")
                .description("Cache hit count")
                .register(meterRegistry);

        this.cacheMiss = Counter.builder("shorturl.cache.miss")
                .description("Cache miss count")
                .register(meterRegistry);
    }

    // POST flow
    public CreateShortUrlResponse create(String originalUrl) {

        GeneratedShortUrl generatedShortUrl = shortKeyGenerator.generate();

        ShortUrlEntity entity = new ShortUrlEntity();
        entity.setId(generatedShortUrl.getId());
        entity.setShortKey(generatedShortUrl.getShortKey());
        entity.setOriginalUrl(originalUrl);
        entity.setCreatedAt(Instant.now());

        repository.save(entity);

        // Optional: cache warm-up
        cache.set(generatedShortUrl.getShortKey(), originalUrl, ttl);

        return new CreateShortUrlResponse(generatedShortUrl.getShortKey());
    }

    @Timed(
        value = "shorturl.resolve.latency",
        description = "End-to-end short URL resolve latency",
        percentiles = {0.5, 0.95, 0.99}
    )
    public String resolve(String shortKey) {

        return cacheTimer.record(() -> {
            String cached = cache.get(shortKey);

            if (cached != null) {
                cacheHit.increment();
                return cached;
            }

            cacheMiss.increment();

            return dbTimer.record(() -> {
                ShortUrlEntity entity;
                try {
                    entity = repository.findByShortKey(shortKey)
                            .orElseThrow(() -> new NotFoundException());
                            cache.set(shortKey, entity.getOriginalUrl(), ttl);
                    return entity.getOriginalUrl();
                } catch (NotFoundException e) {
                    
                    e.printStackTrace();
                    return null;
                }

                
            });
        });
    }

    public String resolveManualTimer(String shortKey) throws NotFoundException {
        long start, end;
        // Phase 5: cache
        // start = System.nanoTime();
        // String url = cache.get(shortKey);
        // end = System.nanoTime();

        // log.info("Cache lookup took {} ms", (end - start) / 1_000_000); // 8 ms first time, 2 ms subsequent times

        // if (url != null) {
        //     return url;
        // }

        // Phase 4: DB
        start = System.nanoTime();

        ShortUrlEntity shortUrl = repository.findByShortKey(shortKey)
                .orElseThrow(NotFoundException::new);

        end = System.nanoTime();
        log.info("DB lookup took {} ms", (end - start) / 1_000_000); // 25 ms 1st time, 5 ms subsequent times


        // Phase 5: populate cache
        cache.set(shortKey, shortUrl.getOriginalUrl(), ttl);
        return shortUrl.getOriginalUrl();
    }
}

