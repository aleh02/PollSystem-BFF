# Poll BFF Service

Thin Spring Boot BFF that proxies the core Poll System API and exposes one composite endpoint.

## Scope

- Proxy all core endpoints under `/rest/api/v0` via `/bff/api/v0/**`.
- Composite endpoint: `GET /bff/api/v0/polls/{id}/view`
  - Calls `/polls-details/{id}` and `/polls/{id}/vote`
  - Combines responses into a single view DTO
- Pass-through `Authorization` header (JWT).

## Configuration

Set the upstream base URL in `application.yaml`:

```yaml
bff:
  upstream:
    base-url: http://localhost:8080/rest/api/v0
```

## Endpoints

### Proxy (generic)

All requests under `/bff/api/v0/**` are forwarded to the core service:

- method, path, query string, headers, and body are passed through
- upstream status codes and response bodies are returned as-is

### Composite

`GET /bff/api/v0/polls/{id}/view`

Response: `PollViewResponse` containing:

- `pollDetailsResponse`
- `voteOptionId`, `voteId`, `votedAt`

## Implementation Notes

- HTTP client: Spring `RestClient`
- Controllers: Spring MVC (`@RestController`)
- DTOs for upstream responses are separate from BFF response DTOs.

## Tests

Unit tests verify:

- composite behavior and upstream status propagation
- proxy passthrough (method, headers, body) and error propagation

Run tests:

```bash
./mvnw test
```

## Build and Run

Build and compile:

```bash
./mvnw clean package
```

Run locally:

```bash
./mvnw spring-boot:run
```

Or run the built jar:

```bash
java -jar target/poll-bff-service-0.0.1-SNAPSHOT.jar
```
