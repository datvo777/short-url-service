-- create schema
CREATE SCHEMA IF NOT EXISTS shortener;

-- create table
CREATE TABLE IF NOT EXISTS shortener.short_urls (
    id BIGINT PRIMARY KEY,
    short_key VARCHAR(255) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ,
    access_count BIGINT NOT NULL DEFAULT 0
);

-- index for fast lookup
CREATE INDEX IF NOT EXISTS idx_short_urls_short_key
ON shortener.short_urls (short_key);
