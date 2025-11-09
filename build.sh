#!/bin/bash

# Build script for Render deployment
# SentinelTrack Java API - Challenge 2025

echo "🚀 Starting SentinelTrack build process..."

# Make gradlew executable
chmod +x ./gradlew

# Clean and build the project
echo "📦 Building application..."
./gradlew clean build -x test --no-daemon

echo "✅ Build completed successfully!"
echo "📍 JAR location: build/libs/"
ls -la build/libs/

echo "🎯 Ready for deployment!"
