# Swift Agent

A Java Spring Boot application acting as a secure gateway to the Swift Cloud, supporting **GPI for Corporates** and **Messaging** APIs.

## Features

*   **Secure Authentication**: Uses Mutual TLS (mTLS) with Swift Channel Certificates.
*   **Multi-Client Support**: Acts as a gateway for internal clients, validating them against an Oracle Database.
*   **GPI for Corporates**: Check payment status (UETR).
*   **Messaging API**: Send FIN (MT) and ISO 20022 (MX) messages, and retrieve distributions/reports.
*   **Auto-Detection**: Automatically detects message types (FIN vs XML) in payloads.

## Prerequisites

*   Java 17 or higher
*   Maven 3.x
*   Oracle Database (with `TB_SWIFT_CLIENT` table)
*   Valid Swift Channel Certificate (PKCS12 format)

## Configuration

Update `src/main/resources/application.properties`:

```properties
swift.api.base-url=https://sandbox.swift.com
server.ssl.key-store-path=classpath:keystore.p12
server.ssl.key-store-password=your_password
spring.datasource.url=jdbc:oracle:thin:@your_host:1521:your_sid
spring.datasource.username=your_user
spring.datasource.password=your_password
```

## Running the Application

```bash
mvn spring-boot:run
```

## API Endpoints

### Internal API (Gateway)
*   **Check Payment Status**: `GET /api/gpi/status/{uetr}`
*   **Send Message**: `POST /api/messaging/send`

**Headers**: `X-Client-ID: <authorized_client_id>`
