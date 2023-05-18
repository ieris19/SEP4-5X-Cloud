#!/usr/bin/env bash

# Starting web api service container
echo "Starting web api service container..."

docker build -t web-api-service:latest .
docker run -dp 8080:8080 --env-file ./../config/secrets.env --name web-api-service web-api-service:latest