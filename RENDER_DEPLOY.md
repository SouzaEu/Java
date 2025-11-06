# 🚀 Deploy na Render - SentinelTrack API

## 📋 Passo a Passo Completo

### **1. Acesse a Render**
- URL: https://render.com
- Faça login ou crie conta (pode usar GitHub)

### **2. Criar Web Service**
1. Clique em **"New +"** → **"Web Service"**
2. Conecte com GitHub
3. Selecione o repositório: **"Java-main"** ou **"SentinelTrack"**

### **3. Configurações do Deploy**

#### **Configurações Básicas:**
- **Name:** `sentineltrack-api`
- **Region:** `Oregon (US West)` (mais rápido)
- **Branch:** `main`
- **Runtime:** `Java 17`

#### **Comandos de Build e Start:**
- **Build Command:** `./build.sh`
- **Start Command:** `./start.sh`

#### **Variáveis de Ambiente (Environment Variables):**
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:h2:mem:testdb
DATABASE_USERNAME=sa
DATABASE_PASSWORD=
ADMIN_PASSWORD=admin123
CORS_ORIGINS=*
```

### **4. Deploy Automático**
1. Clique **"Create Web Service"**
2. Render vai:
   - Fazer clone do repositório
   - Executar `./build.sh` (build do Gradle)
   - Executar `./start.sh` (iniciar aplicação)
   - Gerar URL pública

### **5. URL Final**
Após deploy, você terá:
- **URL:** `https://sentineltrack-api.onrender.com`
- **Health Check:** `https://sentineltrack-api.onrender.com/api/mobile/health`
- **Swagger:** `https://sentineltrack-api.onrender.com/swagger-ui.html`

## 🔧 Troubleshooting

### **Se Build Falhar:**
1. Verifique logs na Render
2. Teste localmente: `./gradlew clean build`
3. Verifique se `build.sh` tem permissão de execução

### **Se App não Iniciar:**
1. Verifique logs de startup
2. Confirme se porta está configurada: `server.port=${PORT:8080}`
3. Teste localmente: `./start.sh`

### **Se APIs não Responderem:**
1. Teste health check: `/api/mobile/health`
2. Verifique CORS configuration
3. Confirme se perfil `prod` está ativo

## ✅ Checklist Final

- [ ] Repositório no GitHub atualizado
- [ ] Scripts `build.sh` e `start.sh` executáveis
- [ ] Configurações de produção prontas
- [ ] Deploy na Render realizado
- [ ] URL funcionando
- [ ] Health check respondendo
- [ ] APIs REST testadas
- [ ] Swagger UI acessível

## 🎯 URLs Importantes

- **Render Dashboard:** https://dashboard.render.com
- **API Produção:** https://sentineltrack-api.onrender.com
- **Health Check:** https://sentineltrack-api.onrender.com/api/mobile/health
- **Swagger UI:** https://sentineltrack-api.onrender.com/swagger-ui.html

---

**🏆 Objetivo:** Cumprir requisito de deploy (40 pontos) do Challenge 2025
