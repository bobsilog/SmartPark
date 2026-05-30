# SmartPark

demo parking management API

Quick start

Prerequisites

- Java 25 (or compatible)
- Maven or use the included wrapper `mvnw`

Build

Windows PowerShell:

```powershell
.\mvnw.cmd clean package
```

Unix / Bash:

```bash
./mvnw clean package
# or
mvn clean package
```

Run

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Unix / Bash:

```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

Tests

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Unix / Bash:

```bash
./mvnw test
# or
mvn test
```

API (examples)

Base URL: `http://localhost:8080`

- Register parking lot: POST `/api/parking-lots` with JSON `{ "lotId": "LOT-001", "location": "Pasig", "capacity": 120 }`
- Register vehicle: POST `/api/vehicles` with JSON `{ "licensePlate": "ABC-1234", "type": "CAR", "ownerName": "Bruce Apos" }`
- Check in: POST `/api/parking-lots/{lotId}/checkin` with JSON `{ "licensePlate": "ABC-1234" }`
- Check out: POST `/api/parking-lots/{lotId}/checkout` with JSON `{ "licensePlate": "ABC-1234" }`
- Get occupancy: GET `/api/parking-lots/{lotId}/occupancy`
- List vehicles in lot: GET `/api/parking-lots/{lotId}/vehicles`

H2 console

Visit `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:smartparkdb`, user `sa`, no password)

Postman

Import `smartpark.postman_collection.json` and set `baseUrl` to `http://localhost:8080`.
