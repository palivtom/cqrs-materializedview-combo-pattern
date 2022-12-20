ARG CONFLUENTINC_VERSION=7.3.0

FROM confluentinc/cp-kafka-connect:$CONFLUENTINC_VERSION

LABEL maintainer="palivtom@fel.cvut.cz"

RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:1.9.7