package com.jp9.urlshortener.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp9.urlshortener.entity.ShortUrlEntity;
import com.jp9.urlshortener.model.ShortUrl;
@Repository
public interface ShortUrlRepository
        extends JpaRepository<ShortUrlEntity, Long> {

    Optional<ShortUrlEntity> findByShortKey(String shortKey);
}
