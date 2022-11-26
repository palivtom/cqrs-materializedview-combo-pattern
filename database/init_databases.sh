#!/bin/bash

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
		psql << EOF
        CREATE DATABASE "$db";
EOF
	done
fi

