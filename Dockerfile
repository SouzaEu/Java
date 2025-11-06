# Dockerfile para SentinelTrack Java API
# Challenge 2025 - 4º Sprint

# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Instalar dependências necessárias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copiar JAR da aplicação
COPY --from=build /app/build/libs/*.jar app.jar

# Criar usuário não-root
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/mobile/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]
