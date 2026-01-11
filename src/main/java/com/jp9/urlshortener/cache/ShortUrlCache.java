package com.jp9.urlshortener.cache;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlCache {
  private final StringRedisTemplate redisTemplate;

    public ShortUrlCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get(String shortKey) {
        return redisTemplate.opsForValue()
                .get(buildKey(shortKey));
    }

    public void set(String shortKey, String url, Duration ttl) {
        redisTemplate.opsForValue()
                .set(buildKey(shortKey), url, ttl);
    }

    private String buildKey(String shortKey) {
        return "shorturl:" + shortKey;
    }
}
