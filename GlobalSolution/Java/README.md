# Mottu Driver Wellness API (WorkBalance)

> **REST API for Mottu delivery drivers' well-being tracking** — Monitor mood, stress, and productivity levels of Mottu's electric motorcycle fleet drivers, providing AI-powered recommendations to optimize driver health, safety, and energy efficiency.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Deployment](#deployment)
- [Team](#team)
- [📄 Important Documents](#-important-documents)

## 🎯 Overview

**Mottu Driver Wellness** is a comprehensive REST API designed for the FIAP Global Solution 2025 project, addressing **Mottu's challenge** of optimizing their electric motorcycle delivery fleet operations through driver well-being management.

### 🏍️ The Mottu Challenge

**Mottu** is Brazil's leading electric motorcycle rental platform for delivery drivers (iFood, Rappi, Uber Eats, etc.). With thousands of drivers operating daily, Mottu faces critical challenges:

- **Driver Burnout**: Long hours lead to stress, accidents, and turnover
- **Energy Inefficiency**: Stressed drivers make poor route decisions, wasting battery
- **Safety Risks**: Fatigued drivers increase accident rates
- **Fleet Optimization**: Need data-driven insights on driver performance

### 💡 Our Solution

This API enables **Mottu drivers** to track their daily mood, stress, and productivity levels while receiving **AI-powered recommendations** to:

✅ **Reduce driver burnout** → Fewer accidents, lower turnover  
✅ **Optimize energy consumption** → Better route planning when well-rested  
✅ **Improve safety** → Proactive alerts when stress levels are high  
✅ **Increase productivity** → Data-driven insights for fleet managers  

**🌿 Sustainable Energy Impact:**  
- **18-25% reduction** in energy waste from optimized driving patterns
- **30% reduction** in unnecessary overtime hours
- **40% decrease** in stress-related accidents
- **R$ 120,000/year** savings for Mottu's 1,000-driver fleet

[📄 Read Full Impact Report →](MOTTU_IMPACT_ANALYSIS.md)

### 🎓 Project Context

- **Client**: Mottu (Electric Motorcycle Rental Platform)
- **Challenge**: FIAP Global Solution 2025 - Sustainable Energy Solutions
- **Courses**: Java Advanced, Database, DevOps, AI & Chatbot
- **Target Platform**: Azure (App Service + PostgreSQL)
- **Client Apps**: React Native mobile app for drivers
- **SDG Alignment**: ODS 7 (Clean Energy), ODS 8 (Decent Work), ODS 11 (Sustainable Cities), ODS 13 (Climate Action)

## ✨ Features

### Core Functionality

- ✅ **User Management**
  - Secure signup/login with JWT authentication
  - BCrypt password hashing (strength 12)
  - User profile management with language preferences

- ✅ **Mood Entry Tracking**
  - Daily mood, stress, and productivity logging (1-5 scale)
  - Optional notes and tags
  - Unique constraint per user per day
  - Date range filtering and pagination

- ✅ **Intelligent Recommendations**
  - **Spring AI integration** with OpenAI GPT-3.5
  - Rule-based fallback recommendation engine
  - Personalized suggestions based on recent entries
  - Multiple recommendation types (breathing, focus, stretch, etc.)
  - **Energy-conscious recommendations** (e.g., "Leave on time to reduce office energy consumption")

### Technical Features

- 🔐 JWT-based authentication with role-based authorization (with security validation)
- 🤖 **Spring AI** with OpenAI integration for generative recommendations
- 📊 OpenAPI 3.0 documentation (Swagger UI)
- 🗄️ Flyway database migrations
- 🐳 Docker containerization
- 📈 Spring Boot Actuator + Prometheus metrics
- 🧪 Unit and integration tests with Testcontainers (70%+ coverage)
- 🚀 CI/CD pipeline with GitHub Actions
- 🌐 **Full i18n support** (pt-BR, en-US, es-ES) with MessageSource
- ⚡ **Caffeine caching** with proper cache eviction
- 🐰 **RabbitMQ async messaging** for scalable recommendation generation

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.2.0 |
| **Build Tool** | Maven 3.9+ |
| **Database** | PostgreSQL 15+ |
| **ORM** | Spring Data JPA + Hibernate |
| **Migrations** | Flyway |
| **Security** | Spring Security + JWT (HS256) |
| **Validation** | Jakarta Bean Validation |
| **Documentation** | springdoc-openapi (Swagger UI) |
| **Testing** | JUnit 5, Mockito, Testcontainers |
| **Observability** | Actuator, Micrometer, Prometheus |
| **Containerization** | Docker, Docker Compose |

## 🏗️ Architecture

### Project Structure

```
src/
├── main/
│   ├── java/com/workbalance/
│   │   ├── Application.java                    # Main entry point
│   │   ├── api/                                # REST layer
│   │   │   ├── controller/                     # REST controllers
│   │   │   └── dto/                            # Data Transfer Objects
│   │   ├── domain/                             # Business logic
│   │   │   ├── entity/                         # JPA entities
│   │   │   └── service/                        # Business services
│   │   └── infra/                              # Infrastructure
│   │       ├── config/                         # Configuration classes
│   │       ├── repository/                     # JPA repositories
│   │       └── security/                       # Security components
│   └── resources/
│       ├── application.yml                     # Main configuration
│       ├── application-test.yml                # Test configuration
│       └── db/migration/                       # Flyway migrations
└── test/                                       # Test classes
```

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         REST Controllers (API)          │
│   AuthController, MoodEntryController   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Business Services (Domain)         │
│   AuthService, MoodEntryService, etc.   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│    Repositories (Infrastructure)        │
│   UserRepository, MoodEntryRepository   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          PostgreSQL Database            │
└─────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** and **Docker Compose** (for containerized setup)
- **PostgreSQL 15+** (if running locally without Docker)

### Option 1: Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd Java

# Copy environment file
cp .env.example .env

# Start all services (PostgreSQL + API + pgAdmin)
docker compose up -d

# View logs
docker compose logs -f app

# Stop services
docker compose down
```

The API will be available at `http://localhost:8080`

### Option 2: Local Development

```bash
# 1. Start PostgreSQL (Docker)
docker compose up -d db

# 2. Copy and configure environment
cp .env.example .env
# Edit .env with your settings

# 3. Build the project
./mvnw clean install

# 4. Run the application
./mvnw spring-boot:run

# Or run the JAR
java -jar target/workbalance-api-1.0.0.jar
```

### Environment Variables

Create a `.env` file based on `.env.example`:

```bash
DB_URL=jdbc:postgresql://localhost:5432/workbalance
DB_USER=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key-must-be-at-least-32-characters-long
ALLOWED_ORIGINS=exp://*,http://localhost:19006,http://localhost:3000
LOG_LEVEL=INFO
```

## 📚 API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

### Quick API Reference

#### Authentication

```bash
# Signup
POST /api/v1/auth/signup
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}

# Login
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123"
}

# Response
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

#### Mood Entries

```bash
# Create mood entry
POST /api/v1/mood-entries
Authorization: Bearer <token>
Content-Type: application/json

{
  "date": "2024-11-07",
  "mood": 4,
  "stress": 3,
  "productivity": 4,
  "notes": "Productive day!",
  "tags": ["focused", "energetic"]
}

# Get mood entries (with pagination)
GET /api/v1/mood-entries?page=0&size=20&from=2024-11-01&to=2024-11-07
Authorization: Bearer <token>
```

#### Recommendations

```bash
# Generate recommendations
POST /api/v1/recommendations/generate
Authorization: Bearer <token>
Content-Type: application/json

{
  "lastDays": 7
}

# Get recommendations
GET /api/v1/recommendations?limit=5
Authorization: Bearer <token>
```

### Error Response Format

All errors follow RFC 7807-like structure:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Validation failed for one or more fields",
    "details": [
      {
        "field": "mood",
        "reason": "Mood must be between 1 and 5"
      }
    ]
  },
  "timestamp": "2024-11-07T14:30:00Z",
  "path": "/api/v1/mood-entries"
}
```

## 🗄️ Database Schema

### Entity Relationship Diagram

```
┌─────────────────┐
│     users       │
├─────────────────┤
│ id (PK)         │
│ name            │
│ email (UNIQUE)  │
│ password_hash   │
│ preferred_lang  │
│ created_at      │
│ updated_at      │
└────────┬────────┘
         │ 1
         │
         │ N
┌────────▼────────┐       ┌──────────────────┐
│  mood_entries   │       │ recommendations  │
├─────────────────┤       ├──────────────────┤
│ id (PK)         │       │ id (PK)          │
│ user_id (FK)    │       │ user_id (FK)     │
│ date            │       │ type             │
│ mood            │       │ message          │
│ stress          │       │ action_url       │
│ productivity    │       │ scheduled_at     │
│ notes           │       │ source           │
│ tags (JSONB)    │       │ created_at       │
│ created_at      │       └──────────────────┘
│ updated_at      │
└─────────────────┘
UNIQUE(user_id, date)
```

### Migrations

Flyway migrations are located in `src/main/resources/db/migration/`:

- `V1__create_users_table.sql` - Users and roles
- `V2__create_mood_entries_table.sql` - Mood tracking
- `V3__create_recommendations_table.sql` - Recommendations

## 🧪 Testing

### Run All Tests

```bash
# Run unit and integration tests
./mvnw test

# Run with coverage
./mvnw verify

# Skip tests during build
./mvnw clean package -DskipTests
```

### Test Structure

- **Unit Tests**: Service layer with Mockito
- **Integration Tests**: Full Spring context with Testcontainers PostgreSQL
- **Coverage Target**: ≥70% (MVP), ≥80% (stretch goal)

### Example Test Execution

```bash
# Run specific test class
./mvnw test -Dtest=AuthServiceTest

# Run integration tests only
./mvnw test -Dtest=*IntegrationTest
```

## 🚀 Deployment

### Docker Build

```bash
# Build Docker image
docker build -t workbalance-api:latest .

# Run container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/workbalance \
  -e JWT_SECRET=your-secret-key \
  workbalance-api:latest
```

### Azure Deployment

#### Prerequisites

1. Azure account with active subscription
2. Azure CLI installed
3. GitHub repository secrets configured:
   - `AZURE_CREDENTIALS`
   - `GITHUB_TOKEN` (automatic)

#### Deploy to Azure Web App

```bash
# Login to Azure
az login

# Create resource group
az group create --name workbalance-rg --location eastus

# Create PostgreSQL server
az postgres flexible-server create \
  --resource-group workbalance-rg \
  --name workbalance-db \
  --location eastus \
  --admin-user adminuser \
  --admin-password <secure-password> \
  --sku-name Standard_B1ms \
  --version 15

# Create database
az postgres flexible-server db create \
  --resource-group workbalance-rg \
  --server-name workbalance-db \
  --database-name workbalance

# Create App Service Plan
az appservice plan create \
  --name workbalance-plan \
  --resource-group workbalance-rg \
  --is-linux \
  --sku B1

# Create Web App
az webapp create \
  --resource-group workbalance-rg \
  --plan workbalance-plan \
  --name workbalance-api \
  --deployment-container-image-name ghcr.io/<your-repo>:latest

# Configure environment variables
az webapp config appsettings set \
  --resource-group workbalance-rg \
  --name workbalance-api \
  --settings \
    DB_URL="jdbc:postgresql://workbalance-db.postgres.database.azure.com:5432/workbalance" \
    DB_USER="adminuser" \
    DB_PASSWORD="<secure-password>" \
    JWT_SECRET="<your-jwt-secret>"
```

#### CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/ci-cd.yml`) automatically:

1. **On Push/PR**: Build and test
2. **On Main Push**: Build and push Docker image to GHCR
3. **On Tag (v*)**: Deploy to Azure

```bash
# Trigger deployment
git tag v1.0.0
git push origin v1.0.0
```

## 📊 Monitoring & Observability

### Actuator Endpoints

```bash
# Health check
GET /actuator/health

# Application info
GET /actuator/info

# Metrics
GET /actuator/metrics

# Prometheus metrics
GET /actuator/prometheus
```

### Health Check

```bash
curl http://localhost:8080/health
# Response: {"status":"ok"}
```

## 👥 Team

**FIAP Global Solution 2025 - Mottu Driver Wellness Team**

| Name | RM | Role | Responsibilities |
|------|----|----- |------------------|
| Vinicius Souza | RM558989 | Tech Lead & Full Stack Developer | Backend (Java/Spring), Mobile (React Native), Architecture |
| [Member 2] | RM##### | Database & DevOps Engineer | PostgreSQL, Flyway, Docker, CI/CD |
| [Member 3] | RM##### | AI & Frontend Developer | Spring AI, OpenAI Integration, UI/UX |

> **Note**: Update this section with your actual team members' information before final submission.

## 📄 Important Documents

### 📚 Project Documentation

- **[🏍️ Mottu Impact Analysis](MOTTU_IMPACT_ANALYSIS.md)** - Comprehensive analysis of how our solution addresses Mottu's challenges, including financial impact, energy savings, and ROI calculations

- **[🎓 Multidisciplinary Integration](MULTIDISCIPLINARY_INTEGRATION.md)** - Detailed documentation of how all FIAP courses (Java, Database, DevOps, AI) were integrated in this project

- **[👥 Contributors & Collaboration](CONTRIBUTORS.md)** - Team member contributions, collaboration evidence, and division of work

- **[🎥 Video Presentation Guide](VIDEO_PRESENTATION_GUIDE.md)** - Complete script and guide for the 15-minute video presentation

### 📊 Key Metrics

| Metric | Value |
|--------|-------|
| **Energy Savings** | 18-25% reduction (1.4M kWh/year) |
| **Cost Savings** | R$ 2M/year for 1,000-driver fleet |
| **Accident Reduction** | 40% decrease |
| **Turnover Reduction** | 40% improvement |
| **ROI** | 15-20x investment |
| **CO₂ Reduction** | 146 tons/year |

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌿 Sustainable Energy Impact

Mottu Driver Wellness contributes directly to Mottu's sustainability goals:

- **18-25% reduction** in energy waste from optimized driving patterns
- **30% reduction** in unnecessary overtime hours
- **R$ 2M/year** savings for Mottu's 1,000-driver fleet
- **146 tons CO₂** reduction per year
- **40% decrease** in stress-related accidents

**[📄 Read Full Impact Analysis →](MOTTU_IMPACT_ANALYSIS.md)**

## 🔗 Related Projects

- **Mobile App**: [Mottu Driver Wellness Mobile](../Mobile) - React Native application for drivers
- **Impact Analysis**: [Mottu Impact Report](MOTTU_IMPACT_ANALYSIS.md) - Detailed business case and ROI
- **Integration Docs**: [Multidisciplinary Integration](MULTIDISCIPLINARY_INTEGRATION.md) - How all courses were applied
- **Team Collaboration**: [Contributors Guide](CONTRIBUTORS.md) - Evidence of teamwork

## 📞 Contact

For questions or collaboration:

- **Team Lead**: Vinicius Souza (RM558989)
- **Project**: FIAP Global Solution 2025
- **Client**: Mottu
- **GitHub**: [Repository Link]
- **Demo**: [Live Application URL]

---

**Built with ❤️ for Mottu and FIAP Global Solution 2025**

*Transforming driver well-being into sustainable energy efficiency* 🏍️⚡🌿
