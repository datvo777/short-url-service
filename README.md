# Short URL Service

A lightweight Java service to create and resolve short URLs. Designed for clarity, testability and easy deployment.

## Features

- Create short URLs from long URLs
- Redirect short codes to target URLs
- Optional analytics (click counts, createdAt)
- Configurable persistence (JDBC / Redis / in-memory)
- REST API, ready for containerization

## Requirements

- Java 11+
- Maven or Gradle
- PostgreSQL / MySQL (optional) or in-memory for dev
- Redis (optional, for caching or rate-limiting)
- Docker (optional)

## Quick start

1. Clone and build
```
git clone <repo>
cd short-url-service
./mvnw clean package    # or: ./gradlew build
```

2. Configure (example environment variables)
```
SERVER_PORT=8080
BASE_URL=http://localhost:8080
DATABASE_URL=jdbc:postgresql://db:5432/shorturl
DATABASE_USER=user
DATABASE_PASSWORD=pass
REDIS_URL=redis://localhost:6379
```

3. Run
```
java -jar target/short-url-service.jar
```

Or with Docker:
```
docker build -t short-url-service .
docker run -e BASE_URL=http://localhost:8080 -p 8080:8080 short-url-service
```

## API

- Create short URL
  - POST /api/shorten
  - Body: `{ "url": "https://example.com/long/path", "expiryDays": 30 }`
  - Response: `{ "shortUrl": "http://.../abc123", "code": "abc123" }`

- Redirect
  - GET /{code}
  - Response: 302 redirect to original URL

- Get metadata
  - GET /api/info/{code}
  - Response: `{ "url": "...", "createdAt": "...", "clicks": 123 }`

- Delete (optional admin)
  - DELETE /api/admin/{code}

Example curl:
```
curl -X POST -H "Content-Type: application/json" \
  -d '{"url":"https://example.com"}' \
  http://localhost:8080/api/shorten
```

## Data model (example)

- urls
  - id (PK)
  - code (unique)
  - target_url
  - created_at
  - expiry_at
  - clicks

Indexes on code and expiry_at recommended.

## Configuration tips

- Use BASE_URL for generating absolute short links.
- Enable Redis for hot-cache of popular codes.
- Apply rate limiting per IP to avoid abuse.
- Consider hashing + collision resolution for generating codes.

## Testing

Run unit/integration tests:
```
./mvnw test   # or: ./gradlew test
```

## Contributing

- Fork repo, create feature branch, open PR with tests and documentation.
- Keep commits small and focused.

## License

MIT License — see LICENSE file.
