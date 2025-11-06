#!/bin/bash

# Start script for Render deployment
# SentinelTrack Java API - Challenge 2025

echo "🚀 Starting SentinelTrack API..."

# Set production profile
export SPRING_PROFILES_ACTIVE=prod

# Find the JAR file
JAR_FILE=$(find build/libs -name "*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ JAR file not found in build/libs/"
    exit 1
fi

echo "📦 Found JAR: $JAR_FILE"
echo "🌍 Environment: Production"
echo "🔧 Profile: $SPRING_PROFILES_ACTIVE"
echo "🚪 Port: ${PORT:-8080}"

# Start the application
java -Dserver.port=${PORT:-8080} \
     -Dspring.profiles.active=prod \
     -Djava.security.egd=file:/dev/./urandom \
     -jar "$JAR_FILE"
