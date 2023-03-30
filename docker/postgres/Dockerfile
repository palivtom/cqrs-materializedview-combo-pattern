ARG POSTGRES_VERSION=14-alpine

FROM debezium/postgres:$POSTGRES_VERSION

LABEL maintainer="palivtom@fel.cvut.cz"

COPY init_databases.sh /docker-entrypoint-initdb.d/