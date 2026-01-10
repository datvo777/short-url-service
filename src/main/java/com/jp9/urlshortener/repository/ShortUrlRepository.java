package com.jp9.urlshortener.repository;

import java.util.Optional;

import com.jp9.urlshortener.model.ShortUrl;

public interface ShortUrlRepository {
    void save(ShortUrl shortUrl);
    Optional<ShortUrl> findByShortKey(String shortKey);
}
