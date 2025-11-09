# Dockerfile para SentinelTrack Java API - Challenge 2025
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar arquivos do projeto
COPY . .

# Dar permissão aos scripts
RUN chmod +x ./gradlew ./build.sh ./start.sh

# Instalar curl para health check
RUN apk add --no-cache curl

# Build da aplicação
RUN ./gradlew clean build -x test

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/mobile/health || exit 1

# Comando para executar
CMD ["java", "-Dserver.port=${PORT:-8080}", "-Dspring.profiles.active=prod", "-jar", "build/libs/SentinelTrack-0.0.1-SNAPSHOT.jar"]
