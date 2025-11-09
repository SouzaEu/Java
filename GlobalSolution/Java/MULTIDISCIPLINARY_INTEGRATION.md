# 🎓 Integração Multidisciplinar - FIAP Global Solution 2025

## Visão Geral

Este documento demonstra como **todas as disciplinas** do semestre foram aplicadas de forma integrada no projeto **Mottu Driver Wellness API**.

---

## 📚 Disciplinas Aplicadas

### 1️⃣ JAVA ADVANCED

#### Conceitos Aplicados:

**Spring Boot 3.2.0 - Arquitetura em Camadas**
- ✅ **Controller Layer**: REST endpoints com `@RestController`
- ✅ **Service Layer**: Lógica de negócio com `@Service`
- ✅ **Repository Layer**: Acesso a dados com Spring Data JPA
- ✅ **DTO Pattern**: Separação Request/Response

**Spring Security + JWT**
- ✅ Autenticação stateless com tokens JWT (HS256)
- ✅ BCrypt password hashing (strength 12)
- ✅ Role-based authorization (USER, ADMIN)
- ✅ Custom `UserDetailsService`

**Spring Data JPA**
- ✅ Entities com relacionamentos (`@OneToMany`, `@ManyToOne`)
- ✅ Custom queries com `@Query`
- ✅ Pagination com `Pageable`
- ✅ Audit fields (`@CreatedDate`, `@LastModifiedDate`)

**Spring AI (Inovação)**
- ✅ Integração com OpenAI GPT-3.5
- ✅ Prompt engineering para recomendações
- ✅ Fallback para rule-based engine

**Caching & Performance**
- ✅ Caffeine cache com `@Cacheable`
- ✅ Cache eviction com `@CacheEvict`
- ✅ TTL configurável

**Messaging (RabbitMQ)**
- ✅ Async recommendation generation
- ✅ Message queues para escalabilidade
- ✅ Dead letter queues

**Validation & Error Handling**
- ✅ Bean Validation (`@Valid`, `@NotNull`, etc.)
- ✅ Custom validators (`@NotOlderThan`)
- ✅ Global exception handler (`@ControllerAdvice`)
- ✅ RFC 7807-like error responses

**Internationalization (i18n)**
- ✅ `MessageSource` com 3 idiomas (pt-BR, en-US, es-ES)
- ✅ `Accept-Language` header support
- ✅ Mensagens de erro localizadas

**OpenAPI 3.0 Documentation**
- ✅ Swagger UI interativo
- ✅ Annotations (`@Operation`, `@Parameter`)
- ✅ Security schemes documentados

**Observability**
- ✅ Spring Boot Actuator
- ✅ Prometheus metrics
- ✅ Health checks customizados
- ✅ Logging estruturado (SLF4J)

#### Evidências:
- 📁 `src/main/java/com/workbalance/` - 59 arquivos Java
- 📁 `src/test/` - 6 classes de teste (JUnit 5 + Mockito)
- 📄 `pom.xml` - Dependências Maven

---

### 2️⃣ DATABASE APPLICATION & DATA SCIENCE

#### Conceitos Aplicados:

**PostgreSQL 15**
- ✅ Database design com normalização (3NF)
- ✅ Primary keys (UUID)
- ✅ Foreign keys com `ON DELETE CASCADE`
- ✅ Unique constraints (`user_id + date`)
- ✅ Indexes para performance

**Advanced Features**
- ✅ JSONB para tags (NoSQL híbrido)
- ✅ Date range queries
- ✅ Aggregate functions para analytics
- ✅ UUID extension (`uuid-ossp`)

**Flyway Migrations**
- ✅ Version control para schema
- ✅ 3 migrations versionadas
- ✅ Rollback strategy

**Data Modeling**
- ✅ Time-series data (mood entries)
- ✅ User management
- ✅ Recommendations tracking

#### Evidências:
- 📁 `src/main/resources/db/migration/`
  - `V1__create_users_table.sql`
  - `V2__create_mood_entries_table.sql`
  - `V3__create_recommendations_table.sql`
- 📊 Entity Relationship Diagram (README.md)

---

### 3️⃣ DEVOPS TOOLS & CLOUD COMPUTING

#### Conceitos Aplicados:

**Containerization**
- ✅ Dockerfile multi-stage build
- ✅ Docker Compose (app + db + pgAdmin)
- ✅ Health checks
- ✅ Volume management

**CI/CD Pipeline**
- ✅ GitHub Actions workflow
- ✅ Automated testing on PR
- ✅ Docker image build & push (GHCR)
- ✅ Azure deployment on tag

**Cloud Deployment (Azure)**
- ✅ App Service configuration
- ✅ PostgreSQL Flexible Server
- ✅ Environment variables management
- ✅ Monitoring & logging

**Infrastructure as Code**
- ✅ `docker-compose.yml`
- ✅ `.github/workflows/ci-cd.yml`
- ✅ Azure CLI scripts (README)

#### Evidências:
- 📄 `Dockerfile`
- 📄 `docker-compose.yml`
- 📄 `.github/workflows/ci-cd.yml`
- 📄 `.env.example`

---

### 4️⃣ ARTIFICIAL INTELLIGENCE & CHATBOT

#### Conceitos Aplicados:

**Spring AI Integration**
- ✅ OpenAI GPT-3.5 Turbo
- ✅ Chat completion API
- ✅ Context-aware prompts
- ✅ Temperature & max tokens tuning

**Prompt Engineering**
```java
String prompt = String.format(
    "You are a wellness coach for Mottu delivery drivers. " +
    "Analyze this driver's mood data from the last %d days and " +
    "provide 3 actionable recommendations to improve well-being " +
    "and energy efficiency. Mood avg: %.1f, Stress avg: %.1f",
    days, avgMood, avgStress
);
```

**Rule-Based Fallback**
- ✅ 7 recommendation types
- ✅ Threshold-based triggers
- ✅ Deterministic logic

**Natural Language Processing**
- ✅ Sentiment analysis (mood tracking)
- ✅ Text generation (recommendations)
- ✅ Multi-language support

#### Evidências:
- 📄 `AIRecommendationService.java`
- 📄 `RuleBasedRecommendationEngine.java`
- 📄 `RecommendationService.java`

---

## 🔗 Integração Entre Disciplinas

### Fluxo Completo: Driver Mood Check-in

```
1. [MOBILE APP] Driver logs mood via React Native
   ↓
2. [JAVA] REST API receives POST /api/v1/mood-entries
   ↓
3. [DATABASE] PostgreSQL stores entry with Flyway schema
   ↓
4. [JAVA] Service triggers recommendation generation
   ↓
5. [AI] Spring AI analyzes patterns with OpenAI
   ↓
6. [DATABASE] Recommendations saved to PostgreSQL
   ↓
7. [JAVA] Cache updated (Caffeine)
   ↓
8. [DEVOPS] Metrics exported to Prometheus
   ↓
9. [MOBILE APP] Driver receives AI-powered recommendations
```

### Exemplo Real:

**Input** (Mobile App):
```json
POST /api/v1/mood-entries
{
  "date": "2025-11-09",
  "mood": 2,
  "stress": 5,
  "productivity": 2,
  "notes": "Muito cansado hoje"
}
```

**Processing** (Java + Database):
1. Validation (Bean Validation)
2. Authorization (JWT)
3. Business logic (Service)
4. Database insert (JPA)
5. AI analysis (Spring AI)

**Output** (AI-Generated):
```json
{
  "recommendations": [
    {
      "type": "BREAK",
      "message": "Seus níveis de estresse estão altos. Faça uma pausa de 20 minutos.",
      "priority": "HIGH"
    },
    {
      "type": "BREATHING",
      "message": "Exercícios de respiração podem reduzir estresse em 30%.",
      "priority": "MEDIUM"
    }
  ]
}
```

**Monitoring** (DevOps):
- Prometheus metrics: `mood_entries_created_total`
- Azure logs: Request/response times
- Health check: `/actuator/health`

---

## 📊 Evidências Visuais

### Arquitetura Completa

```
┌─────────────────────────────────────────────────────┐
│           MOBILE APP (React Native)                 │
│         - Driver interface                          │
│         - Mood tracking UI                          │
└───────────────────┬─────────────────────────────────┘
                    │ HTTPS/REST
┌───────────────────▼─────────────────────────────────┐
│              JAVA BACKEND (Spring Boot)             │
│  ┌──────────────────────────────────────────────┐   │
│  │  Controllers (REST API)                      │   │
│  │  - AuthController                            │   │
│  │  - MoodEntryController                       │   │
│  │  - RecommendationController                  │   │
│  └─────────────────┬────────────────────────────┘   │
│                    │                                 │
│  ┌─────────────────▼────────────────────────────┐   │
│  │  Services (Business Logic)                   │   │
│  │  - AuthService (JWT)                         │   │
│  │  - MoodEntryService (CRUD)                   │   │
│  │  - AIRecommendationService (Spring AI)      │   │
│  └─────────────────┬────────────────────────────┘   │
│                    │                                 │
│  ┌─────────────────▼────────────────────────────┐   │
│  │  Repositories (Data Access)                  │   │
│  │  - UserRepository                            │   │
│  │  - MoodEntryRepository                       │   │
│  │  - RecommendationRepository                  │   │
│  └─────────────────┬────────────────────────────┘   │
└────────────────────┼─────────────────────────────────┘
                     │ JDBC
┌────────────────────▼─────────────────────────────────┐
│         DATABASE (PostgreSQL 15)                     │
│  - users table                                       │
│  - mood_entries table                                │
│  - recommendations table                             │
│  - Flyway migrations                                 │
└──────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│         EXTERNAL SERVICES                            │
│  - OpenAI API (GPT-3.5)                              │
│  - RabbitMQ (Messaging)                              │
│  - Prometheus (Metrics)                              │
└──────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│         DEVOPS (Azure Cloud)                         │
│  - App Service (Java container)                      │
│  - PostgreSQL Flexible Server                        │
│  - GitHub Actions (CI/CD)                            │
│  - Docker Registry (GHCR)                            │
└──────────────────────────────────────────────────────┘
```

---

## 🎯 Resultados da Integração

### Métricas de Qualidade:

| Aspecto | Métrica | Evidência |
|---------|---------|-----------|
| **Code Coverage** | 70%+ | JUnit + Mockito tests |
| **API Endpoints** | 15 | Swagger UI |
| **Database Tables** | 3 | Flyway migrations |
| **Docker Images** | 1 | Multi-stage build |
| **CI/CD Stages** | 3 | GitHub Actions |
| **Languages** | 3 | i18n support |
| **Response Time** | <200ms | Caching enabled |

### Tecnologias Integradas:

- ✅ Java 21
- ✅ Spring Boot 3.2.0
- ✅ PostgreSQL 15
- ✅ Docker & Docker Compose
- ✅ GitHub Actions
- ✅ Azure Cloud
- ✅ OpenAI GPT-3.5
- ✅ React Native (Mobile)
- ✅ RabbitMQ
- ✅ Prometheus
- ✅ Flyway
- ✅ JWT
- ✅ Swagger/OpenAPI

---

## 📝 Conclusão

Este projeto demonstra **integração completa** entre todas as disciplinas:

1. **Java Advanced**: Backend robusto com Spring Boot
2. **Database**: Modelagem e migrations com PostgreSQL
3. **DevOps**: Containerização e CI/CD
4. **AI**: Recomendações inteligentes com Spring AI

**Resultado**: Solução production-ready que resolve um problema real da Mottu.

---

**FIAP Global Solution 2025 - Mottu Driver Wellness Team**
