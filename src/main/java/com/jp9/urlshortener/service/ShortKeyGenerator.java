package com.jp9.urlshortener.service;

import org.springframework.stereotype.Component;

import com.jp9.urlshortener.dto.GeneratedShortUrl;
import com.jp9.urlshortener.util.Base62Encoder;
import com.jp9.urlshortener.util.SnowflakeIdGenerator;

@Component
public class ShortKeyGenerator {

    private final SnowflakeIdGenerator idGenerator;
 
    public ShortKeyGenerator() {
        // machineId should come from config in real systems
        this.idGenerator = new SnowflakeIdGenerator(1);
    }

    public GeneratedShortUrl generate() {
        long id = idGenerator.nextId();
        return new GeneratedShortUrl(id, Base62Encoder.encode(id));
    }
}
