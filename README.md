# Example library
The library is a simple example of a project that uses the command query responsibility segregation pattern.

## Installation
0. Make gradle executable
```bash
chmod +x ./order-service/gradlew && \
chmod +x ./order-updater-service/gradlew
```

1. Build both services
```bash
./order-service/gradlew bootJar -p ./order-service && \
./order-updater-service/gradlew bootJar -p ./order-updater-service
```

2. Start dockerization of the project
```bash
docker compose up --build -d
```

3. Create debezium connector
```bash
curl -i -X POST -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @config/connectors/register-postgres.json
```

4. Application is available on http://localhost:8080

5. Swagger open api specification can be found at `./order-service/src/main/resources/openapi/order-service.yml`