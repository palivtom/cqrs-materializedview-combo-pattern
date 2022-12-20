# Example library
The library is a simple example of a project that uses the command query responsibility segregation pattern.

## Installation

1. Start dockerization of the project
```bash
docker compose up --build -d
```

2. Create debezium connector
```bash
curl -i -X POST -H "Content-Type:application/json" http://localhost:8083/connectors/ -d @config/connectors/register-postgres.json
```

3. Excecute the command to set up the ksql stream
```bash

```