# Entity Audit Library

Reusable Java library for microservices to record entity changes in the **same database** as the service. The audit API is **non-blocking**: the service continues without waiting for persistence.

## Features

- **Configurable actions and attributes** via `audit-service-configurations.yml` (e.g. `orderCreate`, `orderUpdate`; `quantity`, `deliverySlot`).
- **Non-blocking** persistence using `@Async` and a dedicated thread pool.
- **Same database**: audit events are stored in the same MongoDB as the microservice (collection `audit_events`).
- **Spring Boot auto-configuration**: add the dependency and (for MongoDB) enable the audit repository package.

## Setup in a microservice

### 1. Maven dependency

```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>entity-audit-library</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Install the library first: `mvn -f entity-audit-library clean install`.

### 2. Enable audit repositories (MongoDB)

In your `@SpringBootApplication` class, ensure the audit repository package is scanned:

```java
@EnableMongoRepositories(basePackages = {"com.yourcompany.yourapp.repository", "com.example.audit.repository"})
```

### 3. Configuration

Create or override `audit-service-configurations.yml` in `src/main/resources` (or add to `application.yml`). You can also import the default:

```yaml
spring.config.import: optional:classpath:audit-service-configurations.yml
```

Example content:

```yaml
audit:
  entities:
    order:
      actions:
        - orderCreate
        - orderUpdate
      attributes:
        - quantity
        - deliverySlot
        - items
```

### 4. Recording audit events

Inject `AuditService` and use `AuditEventBuilder` to build and save events:

```java
@Autowired
private AuditService auditService;

// After creating an entity
AuditEvent event = AuditEventBuilder.create()
    .auditKey1(storeId)                    // e.g. "Store-1"
    .entity("order", order.getOrderNumber()) // entity type and id
    .action("orderCreate")
    .changedAttribute("N/A")
    .currentValue("N/A")
    .newValue(jsonMapper.toJson(order))    // full snapshot for create
    .sourceSystem(order.getSourceSystem())
    .sourceParty(order.getSourceParty())
    .build();
auditService.saveAuditEventAsync(event);   // non-blocking
```

For updates, send one event per changed attribute (or one event with the full diff). The call returns immediately; persistence runs asynchronously.

## Event model

| Field             | Description |
|-------------------|-------------|
| auditKey1         | Origin/scope (e.g. store id, tenant) |
| auditKey2         | `audit:entityType:entityId` (e.g. `audit:order:ORD-001`) |
| action            | e.g. `orderCreate`, `orderUpdate` |
| entityId          | Entity identifier |
| changedAttribute  | Attribute that changed, or `N/A` for create |
| currentValue      | Value before (JSON string), or `N/A` |
| newValue          | Value after (JSON string) |
| sourceSystem      | System that initiated the change |
| sourceParty       | Party responsible |
| createdAt         | Set automatically if null |

## Testing

In integration tests, you can mock `AuditService` with `@MockBean` so the audit library does not need to connect to MongoDB for tests.
