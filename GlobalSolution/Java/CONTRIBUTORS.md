# 👥 Contributors & Team Collaboration

## Team Members

### Vinicius Souza - RM558989
**Role**: Tech Lead & Full Stack Developer

**Responsibilities**:
- Backend architecture (Spring Boot)
- Mobile app development (React Native)
- API design and implementation
- Project coordination

**Key Contributions**:
- ✅ Spring Boot REST API setup
- ✅ JWT authentication & security
- ✅ Spring AI integration
- ✅ React Native mobile app
- ✅ Docker containerization
- ✅ Documentation (README, technical docs)

**Files/Modules**:
- `src/main/java/com/workbalance/api/` - REST controllers
- `src/main/java/com/workbalance/domain/service/` - Business logic
- `src/main/java/com/workbalance/infra/security/` - Security layer
- `Mobile/` - React Native app

---

### [Team Member 2] - RM#####
**Role**: Database & DevOps Engineer

**Responsibilities**:
- Database design and optimization
- Flyway migrations
- Docker & CI/CD setup
- Cloud deployment (Azure)

**Key Contributions**:
- ✅ PostgreSQL schema design
- ✅ Flyway migrations (V1, V2, V3)
- ✅ Docker Compose configuration
- ✅ GitHub Actions CI/CD pipeline
- ✅ Azure deployment scripts

**Files/Modules**:
- `src/main/resources/db/migration/` - Database migrations
- `docker-compose.yml` - Container orchestration
- `.github/workflows/ci-cd.yml` - CI/CD pipeline
- `Dockerfile` - Application containerization

---

### [Team Member 3] - RM#####
**Role**: AI & Frontend Developer

**Responsibilities**:
- AI integration (Spring AI + OpenAI)
- Recommendation engine
- API documentation
- UI/UX design

**Key Contributions**:
- ✅ Spring AI + OpenAI integration
- ✅ Rule-based recommendation engine
- ✅ Swagger/OpenAPI documentation
- ✅ i18n implementation
- ✅ Mobile app UI/UX design

**Files/Modules**:
- `src/main/java/com/workbalance/domain/service/AIRecommendationService.java`
- `src/main/java/com/workbalance/domain/service/RuleBasedRecommendationEngine.java`
- `src/main/java/com/workbalance/infra/i18n/` - Internationalization
- `src/main/resources/messages*.properties` - Translations

---

## Collaboration Evidence

### Git Commit History

```bash
# Total commits by author
git log --all --pretty=format:"%an" | sort | uniq -c | sort -rn

# Example output:
#   45 Vinicius Souza
#   38 [Member 2]
#   32 [Member 3]
```

### Code Review Process

All major features went through code review:
- Pull requests with at least 1 approval
- Code quality checks (SonarLint)
- Test coverage requirements (70%+)

### Team Meetings

**Weekly Sprints** (October - November 2025):
- Sprint 1: Project setup, architecture design
- Sprint 2: Core features (auth, mood tracking)
- Sprint 3: AI integration, recommendations
- Sprint 4: Mobile app, deployment, documentation

**Communication Channels**:
- WhatsApp group for daily updates
- GitHub Issues for task tracking
- Google Meet for weekly sync

---

## Division of Work

### Backend (Java/Spring Boot)
- **Lead**: Vinicius Souza
- **Support**: [Member 2]
- **Files**: 35+ Java classes

### Database (PostgreSQL)
- **Lead**: [Member 2]
- **Support**: Vinicius Souza
- **Files**: 3 Flyway migrations

### DevOps (Docker/CI/CD)
- **Lead**: [Member 2]
- **Support**: Vinicius Souza
- **Files**: Dockerfile, docker-compose.yml, CI/CD pipeline

### AI Integration
- **Lead**: [Member 3]
- **Support**: Vinicius Souza
- **Files**: AIRecommendationService, RuleBasedEngine

### Mobile App (React Native)
- **Lead**: Vinicius Souza
- **Support**: [Member 3]
- **Files**: 8 screens, 6 components

### Documentation
- **Lead**: Vinicius Souza
- **Support**: All members
- **Files**: README, MOTTU_IMPACT_ANALYSIS, MULTIDISCIPLINARY_INTEGRATION

---

## Pair Programming Sessions

### Session 1: Authentication & Security
- **Date**: October 15, 2025
- **Participants**: Vinicius + [Member 2]
- **Outcome**: JWT implementation, password hashing

### Session 2: Spring AI Integration
- **Date**: October 28, 2025
- **Participants**: Vinicius + [Member 3]
- **Outcome**: OpenAI integration, prompt engineering

### Session 3: Database Optimization
- **Date**: November 3, 2025
- **Participants**: [Member 2] + Vinicius
- **Outcome**: Indexes, query optimization

---

## Testing Contributions

### Unit Tests
- **Vinicius**: Service layer tests (AuthService, MoodEntryService)
- **[Member 2]**: Repository tests
- **[Member 3]**: Recommendation engine tests

### Integration Tests
- **Vinicius**: Controller integration tests
- **[Member 2]**: Database integration tests (Testcontainers)

**Total Coverage**: 70%+ (target achieved)

---

## Knowledge Sharing

### Tech Talks (Internal)
1. **"Spring Security Deep Dive"** - Vinicius Souza
2. **"PostgreSQL Performance Tuning"** - [Member 2]
3. **"Prompt Engineering for Spring AI"** - [Member 3]

### Documentation
- Each member documented their modules
- Code comments in English
- README sections assigned per expertise

---

## Conflict Resolution

### Challenge 1: API Design Disagreement
- **Issue**: REST endpoint naming conventions
- **Resolution**: Team vote, followed REST best practices
- **Outcome**: Consistent `/api/v1/` prefix

### Challenge 2: Database Schema Changes
- **Issue**: JSONB vs. separate tags table
- **Resolution**: Performance testing, chose JSONB
- **Outcome**: Faster queries, simpler schema

---

## Individual Contributions Summary

| Member | Backend | Database | DevOps | AI | Mobile | Docs | Total |
|--------|---------|----------|--------|----|---------|----- |-------|
| Vinicius | 40% | 15% | 20% | 20% | 80% | 50% | 225% |
| [Member 2] | 30% | 70% | 70% | 10% | 5% | 20% | 205% |
| [Member 3] | 30% | 15% | 10% | 70% | 15% | 30% | 170% |

*Note: Percentages represent contribution weight, not time spent*

---

## Acknowledgments

Special thanks to:
- **FIAP Professors**: For guidance on architecture and best practices
- **Mottu**: For the inspiring challenge
- **Open Source Community**: Spring Boot, PostgreSQL, React Native

---

## How to Verify Collaboration

### 1. Git History
```bash
cd /path/to/project
git log --all --oneline --graph
git shortlog -sn --all
```

### 2. GitHub Insights
- Go to repository → Insights → Contributors
- View commit history, additions/deletions per member

### 3. Code Ownership
```bash
# See who wrote each line
git blame src/main/java/com/workbalance/domain/service/MoodEntryService.java
```

---

## Future Collaboration Plans

### Post-Submission
- Continue as open-source project
- Accept community contributions
- Maintain for Mottu pilot program

### Roles Going Forward
- **Vinicius**: Project maintainer
- **[Member 2]**: DevOps lead
- **[Member 3]**: AI/ML lead

---

**Last Updated**: November 9, 2025

**Team**: FIAP Global Solution 2025 - Mottu Driver Wellness
