# Money Service Kafka

A multi-module Spring Boot microservices project demonstrating event-driven architecture using Apache Kafka for asynchronous communication between services.

## Project Structure

```
money-service-kafka/
├── base-domain/          # Shared domain models and DTOs
├── order-service/        # Service for handling orders
├── email-service/        # Service for sending emails
├── stock-service/        # Service for managing stock
├── settings.gradle       # Root Gradle configuration
└── README.md
```

### Modules

#### base-domain
Contains shared data transfer objects (DTOs) and domain models used across all services:
- `Order` - Order entity
- `OrderEvent` - Event model for Kafka messages

#### order-service
REST API service for placing orders. Publishes `OrderEvent` messages to Kafka for downstream services to consume.
- **Port:** 8081
- **Endpoint:** `POST /api/v1/orders`

#### email-service
Consumes order events from Kafka and sends email notifications. (Consumer service)
- **Port:** 8082

#### stock-service
Consumes order events from Kafka and updates stock levels. (Consumer service)
- **Port:** 8083

## Prerequisites

- **Java:** OpenJDK 25 or higher
- **Apache Kafka:** Running locally on `localhost:9092`
- **Build Tool:** Gradle (wrapper included)

## Getting Started

### 1. Start Kafka

Ensure Apache Kafka is running on `localhost:9092`. The application will automatically create the required topics.

### 2. Build the Project

From the root directory:
```bash
./gradlew clean build
```

### 3. Run Individual Services

#### Order Service
```bash
cd order-service
./gradlew bootRun
```
Service runs on `http://localhost:8081`

#### Email Service
```bash
cd email-service
./gradlew bootRun
```
Service runs on `http://localhost:8082`

#### Stock Service
```bash
cd stock-service
./gradlew bootRun
```
Service runs on `http://localhost:8083`

## API Usage

### Place an Order
```bash
curl -X POST http://localhost:8081/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Product Name",
    "qty": 5,
    "price": 29.99
  }'
```

Response:
```
Order placed successfully ...
```

## Kafka Configuration

### Producer (order-service)
- **Bootstrap Servers:** `localhost:9092`
- **Key Serializer:** `StringSerializer`
- **Value Serializer:** `JsonSerializer`
- **Topic:** `order_topic`
- **Partitions:** 3
- **Replicas:** 1

### Application Properties

Each service has its own `application.properties`:
```properties
spring.application.name=order-service
server.port=8081
spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.topic.name=order_topic
```

## Dependencies

Key dependencies used across the project:
- **Spring Boot 4.0.6**
- **Spring Kafka 4.0.5**
- **Apache Kafka 4.1.2**
- **Lombok** - For reducing boilerplate code
- **Jackson 2.18.1** - JSON serialization (see note below)

### Important: Jackson Version Compatibility

This project uses **Jackson 2.18.1** explicitly due to compatibility requirements with Spring Kafka 4.0.5. Spring Boot 4.0.6 includes Jackson 3, but the Kafka `JsonSerializer` references `com.fasterxml.jackson.databind` classes that are only available in Jackson 2. The explicit dependency ensures proper Kafka message serialization.

## Building & Testing

### Run Tests
```bash
./gradlew test
```

### Run All Tests
```bash
./gradlew clean build
```

### Check Dependencies
```bash
./gradlew dependencies --configuration runtimeClasspath
```

## Troubleshooting

### Gradle Cache Issues
If you encounter resolution issues, refresh the Gradle cache:
```bash
./gradlew --refresh-dependencies clean build
```

### IDE Integration (VS Code)

**Refresh Java Language Server:**
1. Open Command Palette: `Ctrl+Shift+P`
2. Run `Java: Clean Java Language Server Workspace`
3. Reload the window when prompted

**Refresh Gradle Projects:**
1. Open Command Palette: `Ctrl+Shift+P`
2. Run `Gradle: Refresh All Projects`

### Kafka Connection Issues
- Verify Kafka is running on `localhost:9092`
- Check firewall settings
- Review application logs for connection errors

## Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP POST /api/v1/orders
       ▼
┌─────────────────────┐
│   Order Service     │
│ (Kafka Producer)    │
└──────┬──────────────┘
       │ OrderEvent
       ▼ (Kafka Topic: order_topic)
    ┌──────────────────────────────┐
    │                              │
    ▼                              ▼
┌─────────────┐          ┌─────────────────┐
│Email Service│          │  Stock Service  │
│(Consumer)   │          │   (Consumer)    │
└─────────────┘          └─────────────────┘
```

## Development Notes

- Ensure all three services are running for the complete event flow
- Messages published to Kafka are serialized as JSON
- Each service subscribes to `order_topic` (except order-service which is the producer)
- The project uses multi-module Gradle for dependency management

## License

This project is provided as-is for educational and development purposes.
