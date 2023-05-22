#!/usr/bin/env bash

# Start-up script for the web api service
echo "Starting web api service..."

# Create secret properties file from environment variables
echo "Creating secret properties file..."
mkdir config
touch config/secrets.properties
echo "mongodb.url=${mongodb_url}" > config/secrets.properties
echo "Secrets file created."

echo "Starting Spring Container..."
java -jar web.jar
echo "Spring Container has terminated"