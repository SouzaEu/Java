# 🎥 Guia de Apresentação em Vídeo - 15 Minutos

## 📋 Checklist Pré-Gravação

### Preparação Técnica
- [ ] Aplicação rodando online (URL pública)
- [ ] Swagger UI acessível
- [ ] Mobile app funcionando (emulador ou device)
- [ ] Slides preparados (opcional, mas recomendado)
- [ ] Microfone testado
- [ ] Tela limpa (fechar abas desnecessárias)

### Preparação da Equipe
- [ ] Todos os membros confirmados
- [ ] Ordem de apresentação definida
- [ ] Tempo de cada membro calculado (~5min cada)
- [ ] Transições entre membros ensaiadas

---

## ⏱️ Estrutura do Vídeo (15 minutos)

### 🎬 INTRODUÇÃO (1 minuto) - Vinicius

**O que mostrar**:
```
[Tela: Slide com logo Mottu + FIAP]

"Olá! Somos a equipe [Nome do Grupo] da FIAP, e vamos apresentar 
nossa solução para o desafio da Mottu: o Mottu Driver Wellness API.

Equipe:
- Vinicius Souza (RM558989) - Tech Lead
- [Membro 2] (RM#####) - Database & DevOps
- [Membro 3] (RM#####) - AI & Frontend

Vamos demonstrar como nossa solução reduz o burnout de entregadores,
otimiza o consumo de energia e aumenta a segurança da frota."
```

**Tempo**: 1:00

---

### 🏍️ PROBLEMA DA MOTTU (2 minutos) - [Membro 2]

**O que mostrar**:
```
[Tela: Slide com dados da Mottu]

"A Mottu é a maior plataforma de aluguel de motos elétricas do Brasil,
atendendo milhares de entregadores. Mas enfrenta 3 desafios críticos:

1. BURNOUT: 35% de turnover anual = R$ 875 mil em custos
2. ENERGIA: Motoristas estressados desperdiçam 15-20% de bateria
3. SEGURANÇA: Fadiga aumenta acidentes em 3x

Nossa solução ataca esses 3 problemas simultaneamente."
```

**Tempo**: 2:00

---

### 💡 NOSSA SOLUÇÃO (2 minutos) - [Membro 3]

**O que mostrar**:
```
[Tela: Diagrama de arquitetura]

"Desenvolvemos uma API REST que permite aos motoristas registrarem
seu humor, estresse e produtividade diariamente.

A mágica acontece aqui: usamos Spring AI + OpenAI para analisar
padrões e gerar recomendações personalizadas.

Exemplo: Se um motorista está com estresse alto por 3 dias seguidos,
o sistema alerta automaticamente e sugere pausas estratégicas.

Resultado:
- 18-25% redução no consumo de energia
- 40% menos acidentes
- R$ 2 milhões de economia anual"
```

**Tempo**: 2:00

---

### 🖥️ DEMONSTRAÇÃO TÉCNICA (6 minutos)

#### Parte 1: Backend API (3 min) - Vinicius

**O que mostrar**:
```
[Tela: Navegador com Swagger UI]

"Vou demonstrar a API rodando online em [URL].

1. AUTENTICAÇÃO:
   - POST /api/v1/auth/signup
   - Criar usuário: 'motorista@mottu.com'
   - Mostrar JWT token gerado

2. REGISTRO DE HUMOR:
   - POST /api/v1/mood-entries
   - Registrar: mood=2, stress=5, productivity=2
   - Explicar: "Motorista está estressado"

3. RECOMENDAÇÕES AI:
   - POST /api/v1/recommendations/generate
   - Mostrar resposta do OpenAI
   - Ler recomendação: "Faça uma pausa de 20 minutos..."

4. HISTÓRICO:
   - GET /api/v1/mood-entries?from=2025-11-01
   - Mostrar paginação e filtros

Tudo isso com:
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring AI + OpenAI
- PostgreSQL com Flyway"
```

**Tempo**: 3:00

#### Parte 2: Mobile App (2 min) - [Membro 3]

**O que mostrar**:
```
[Tela: Emulador Android/iOS]

"Agora vou mostrar a experiência do motorista no app mobile.

1. LOGIN:
   - Tela de login com email/senha
   - Autenticação via API

2. DASHBOARD:
   - Gráfico de humor dos últimos 7 dias
   - Estatísticas: mood médio, stress médio

3. REGISTRO RÁPIDO:
   - Sliders para mood, stress, productivity
   - Tags: 'cansado', 'trânsito pesado'
   - Enviar em < 30 segundos

4. RECOMENDAÇÕES:
   - Lista de recomendações AI
   - Ícones e prioridades (HIGH, MEDIUM, LOW)
   - Ação: "Fazer pausa agora"

App desenvolvido em React Native com:
- Expo SDK 50
- TypeScript
- Zustand (state management)
- Integração completa com API"
```

**Tempo**: 2:00

#### Parte 3: DevOps & Database (1 min) - [Membro 2]

**O que mostrar**:
```
[Tela: GitHub Actions + Azure Portal]

"Toda nossa infraestrutura é automatizada:

1. CI/CD:
   - GitHub Actions: build, test, deploy
   - Testes automáticos em cada PR
   - Deploy automático no Azure

2. DATABASE:
   - PostgreSQL 15 no Azure
   - 3 tabelas: users, mood_entries, recommendations
   - Flyway migrations para versionamento

3. MONITORAMENTO:
   - Actuator health checks
   - Prometheus metrics
   - Logs estruturados

Tudo containerizado com Docker e pronto para escalar."
```

**Tempo**: 1:00

---

### 🎓 INTEGRAÇÃO MULTIDISCIPLINAR (2 minutos) - Vinicius

**O que mostrar**:
```
[Tela: Slide com diagrama de integração]

"Este projeto integra TODAS as disciplinas do semestre:

1. JAVA ADVANCED:
   - Spring Boot, Security, Data JPA, AI
   - 59 classes Java, arquitetura em camadas
   - Testes: JUnit, Mockito, Testcontainers

2. DATABASE:
   - PostgreSQL com JSONB, indexes
   - Flyway migrations versionadas
   - Queries otimizadas para analytics

3. DEVOPS:
   - Docker + Docker Compose
   - GitHub Actions CI/CD
   - Azure deployment (App Service + DB)

4. AI & CHATBOT:
   - Spring AI + OpenAI GPT-3.5
   - Prompt engineering
   - Rule-based fallback

Tudo documentado em MULTIDISCIPLINARY_INTEGRATION.md"
```

**Tempo**: 2:00

---

### 🌍 IMPACTO & SUSTENTABILIDADE (1 minuto) - [Membro 2]

**O que mostrar**:
```
[Tela: Slide com métricas de impacto]

"Nossa solução contribui diretamente para os ODS da ONU:

ODS 7 (Energia Limpa):
- 1,4 milhão kWh economizados/ano
- 146 toneladas CO₂ reduzidas

ODS 8 (Trabalho Decente):
- 40% redução em turnover
- Melhor qualidade de vida para motoristas

ODS 11 (Cidades Sustentáveis):
- Menos acidentes = trânsito mais seguro
- Otimização de rotas urbanas

ROI: 15-20x o investimento
Payback: 2-3 meses

Detalhes em MOTTU_IMPACT_ANALYSIS.md"
```

**Tempo**: 1:00

---

### 🎯 CONCLUSÃO (1 minuto) - Todos

**O que mostrar**:
```
[Tela: Slide final com logo + contatos]

Vinicius:
"Desenvolvemos uma solução completa, production-ready, que resolve
um problema real da Mottu."

[Membro 2]:
"Com tecnologias modernas, arquitetura escalável e CI/CD automatizado."

[Membro 3]:
"Integrando IA para recomendações personalizadas e impacto mensurável."

Todos juntos:
"Obrigado! Estamos prontos para perguntas."

[Mostrar QR code ou link do GitHub]
```

**Tempo**: 1:00

---

## 🎬 Dicas de Gravação

### Para o Apresentador
1. **Fale devagar e claramente** - Professores assistirão múltiplas vezes
2. **Mostre, não apenas fale** - Demonstrações valem mais que slides
3. **Use transições suaves** - "Agora vou passar para [Nome]..."
4. **Mantenha energia** - Entusiasmo é contagiante

### Para a Edição
1. **Cortes limpos** - Remova pausas longas
2. **Zoom quando necessário** - Facilite leitura de código/telas
3. **Legendas (opcional)** - Ajudam na compreensão
4. **Música de fundo (baixa)** - Apenas na intro/conclusão

### Checklist Técnico
- [ ] Resolução mínima: 1080p
- [ ] Áudio claro (sem ruídos)
- [ ] Cursor visível quando navegar
- [ ] URLs funcionando
- [ ] Sem informações sensíveis (senhas, tokens)

---

## 📊 Distribuição de Tempo

| Seção | Tempo | Apresentador |
|-------|-------|--------------|
| Introdução | 1:00 | Vinicius |
| Problema Mottu | 2:00 | [Membro 2] |
| Nossa Solução | 2:00 | [Membro 3] |
| Demo Backend | 3:00 | Vinicius |
| Demo Mobile | 2:00 | [Membro 3] |
| Demo DevOps | 1:00 | [Membro 2] |
| Integração | 2:00 | Vinicius |
| Impacto | 1:00 | [Membro 2] |
| Conclusão | 1:00 | Todos |
| **TOTAL** | **15:00** | |

---

## 🚨 Erros a Evitar

❌ **NÃO FAZER**:
- Ler slides palavra por palavra
- Demonstrar features que não funcionam
- Falar muito rápido
- Esquecer de mencionar a Mottu
- Ultrapassar 15 minutos
- Apresentar sozinho (todos devem aparecer)

✅ **FAZER**:
- Demonstrar aplicação funcionando
- Conectar com o desafio da Mottu
- Mostrar código real (brevemente)
- Destacar inovações (Spring AI)
- Mencionar todas as disciplinas
- Ser objetivo e confiante

---

## 📝 Script de Emergência

**Se algo der errado durante a gravação**:

```
"Tivemos um problema técnico aqui, mas vou mostrar através de 
[screenshots/gravação prévia/código]. Em produção, isso funciona 
perfeitamente, como podem ver em [URL]."
```

**Se ultrapassar o tempo**:
- Corte a seção de DevOps para 30s
- Reduza demos para apenas 1 exemplo cada
- Foque no essencial: Mottu + funcionamento + impacto

---

## 🎯 Objetivos da Apresentação

Ao final do vídeo, os professores devem entender:

1. ✅ **Problema**: Desafio específico da Mottu
2. ✅ **Solução**: Como nossa API resolve o problema
3. ✅ **Tecnologia**: Stack completo e integração
4. ✅ **Funcionamento**: Demonstração prática
5. ✅ **Impacto**: Métricas de sustentabilidade
6. ✅ **Equipe**: Todos contribuíram

---

## 📦 Entrega Final

**Junto com o vídeo, entregar**:
- [ ] Link do vídeo (YouTube/Google Drive)
- [ ] Link do repositório GitHub
- [ ] Link da aplicação online
- [ ] README.md completo
- [ ] Documentos: MOTTU_IMPACT_ANALYSIS.md, MULTIDISCIPLINARY_INTEGRATION.md

---

**Boa sorte! 🚀**

**Lembre-se**: Vocês construíram algo incrível. Mostrem com orgulho!
