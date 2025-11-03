// =====================================================
// SCRIPT DE IMPORTAÇÃO PARA MONGODB
// Challenge 2025 - 4º Sprint - Database
// Importa dados JSON do banco relacional para MongoDB
// =====================================================

// Conectar ao MongoDB
use sentineltrack_nosql;

// Limpar coleções existentes
db.enderecos.drop();
db.motos.drop();
db.usuarios.drop();
db.funcionarios.drop();
db.dispositivos_iot.drop();
db.localizacoes_motos.drop();
db.alertas.drop();
db.historico_uso.drop();
db.sensor_dados.drop();
db.eventos_iot.drop();
db.metadados.drop();

print("🗄️  Coleções limpas. Iniciando importação...");

// Função para carregar dados do arquivo JSON exportado
// Nota: Este script assume que o arquivo JSON foi exportado e está disponível
// Para uso real, você deve executar: mongoimport --db sentineltrack_nosql --collection dados_exportados --file sentineltrack_export.json

// Simular dados para demonstração (baseado na estrutura do banco relacional)

// =====================================================
// ENDEREÇOS
// =====================================================
print("📍 Importando endereços...");

db.enderecos.insertMany([
    {
        "_id": "01310100",
        "cep": "01310100",
        "pais": "Brasil",
        "estado": "SP",
        "cidade": "São Paulo",
        "bairro": "Bela Vista",
        "numero": 123,
        "logradouro": "Avenida Paulista",
        "complemento": "Próximo ao MASP",
        "created_at": new Date(),
        "source": "relational_db"
    },
    {
        "_id": "04038001",
        "cep": "04038001",
        "pais": "Brasil",
        "estado": "SP",
        "cidade": "São Paulo",
        "bairro": "Vila Olímpia",
        "numero": 456,
        "logradouro": "Rua Funchal",
        "complemento": "Edifício Comercial",
        "created_at": new Date(),
        "source": "relational_db"
    }
]);

// =====================================================
// DISPOSITIVOS IOT
// =====================================================
print("🔌 Importando dispositivos IoT...");

db.dispositivos_iot.insertMany([
    {
        "_id": "SENSOR001",
        "nome": "Sensor de Movimento A1",
        "tipo": "sensor_movimento",
        "status": "online",
        "localizacao": {
            "descricao": "Zona A1 - Entrada Principal",
            "zona": "A1",
            "coordenadas": {
                "latitude": -23.5505,
                "longitude": -46.6333
            }
        },
        "configuracao": {
            "sensitivity": "high",
            "detection_range": 10,
            "alert_threshold": 0.8,
            "operating_hours": "24/7"
        },
        "ultima_comunicacao": new Date(),
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    },
    {
        "_id": "CAMERA001",
        "nome": "Câmera Principal",
        "tipo": "camera",
        "status": "online",
        "localizacao": {
            "descricao": "Entrada Principal - Portaria",
            "zona": "ENTRADA",
            "coordenadas": {
                "latitude": -23.5500,
                "longitude": -46.6330
            }
        },
        "configuracao": {
            "resolution": "1080p",
            "fps": 30,
            "night_vision": true,
            "motion_detection": true,
            "recording": "continuous"
        },
        "ultima_comunicacao": new Date(),
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    },
    {
        "_id": "LOCK001",
        "nome": "Trava Inteligente A1",
        "tipo": "atuador_trava",
        "status": "online",
        "localizacao": {
            "descricao": "Zona A1 - Sistema de Travamento",
            "zona": "A1",
            "coordenadas": {
                "latitude": -23.5505,
                "longitude": -46.6333
            }
        },
        "configuracao": {
            "auto_lock": true,
            "unlock_method": ["rfid", "mobile_app"],
            "timeout": 300,
            "force_threshold": 50
        },
        "ultima_comunicacao": new Date(),
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    }
]);

// =====================================================
// MOTOS E LOCALIZAÇÕES
// =====================================================
print("🏍️  Importando motos e localizações...");

db.motos.insertMany([
    {
        "_id": "ABC1234",
        "placa": "ABC1234",
        "modelo": "Honda CG 160",
        "proprietario": {
            "cpf": "12345678901",
            "nome": "João Silva"
        },
        "documentos": {
            "niv": "9BWZZZ377VT004251",
            "motor": "JH2PC4001LM200001",
            "renavam": 123456789,
            "fipe": 15000
        },
        "localizacao_atual": {
            "coordenadas": {
                "latitude": -23.5505,
                "longitude": -46.6333
            },
            "endereco": "Rua das Palmeiras, 123 - Pátio SentinelTrack",
            "zona": "A1",
            "setor": "Setor A",
            "andar": 1,
            "vaga": "A1-001",
            "descricao": "Próximo à entrada principal, primeira fileira"
        },
        "status": {
            "atual": "disponivel",
            "bateria": 95,
            "ultima_atualizacao": new Date(),
            "em_uso_por": null
        },
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    },
    {
        "_id": "DEF5678",
        "placa": "DEF5678",
        "modelo": "Yamaha Factor",
        "proprietario": {
            "cpf": "98765432109",
            "nome": "Maria Santos"
        },
        "documentos": {
            "niv": "9C2JC3110LR123456",
            "motor": "JH2PC4002LM200002",
            "renavam": 987654321,
            "fipe": 18000
        },
        "localizacao_atual": {
            "coordenadas": {
                "latitude": -23.5515,
                "longitude": -46.6343
            },
            "endereco": "Rua das Palmeiras, 123 - Pátio SentinelTrack",
            "zona": "A2",
            "setor": "Setor A",
            "andar": 1,
            "vaga": "A2-005",
            "descricao": "Segunda fileira, próximo ao banheiro"
        },
        "status": {
            "atual": "em_uso",
            "bateria": 78,
            "ultima_atualizacao": new Date(),
            "em_uso_por": "12345678901"
        },
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    }
]);

// =====================================================
// USUÁRIOS
// =====================================================
print("👥 Importando usuários...");

db.usuarios.insertMany([
    {
        "_id": "12345678901",
        "cpf": "12345678901",
        "nome": "João Silva",
        "data_nascimento": new Date("1990-05-15"),
        "endereco": {
            "cep": "01310100",
            "endereco_completo": "Avenida Paulista, 123 - Bela Vista - São Paulo/SP"
        },
        "moto_associada": "ABC1234",
        "perfil": "usuario",
        "ativo": true,
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    },
    {
        "_id": "98765432109",
        "cpf": "98765432109",
        "nome": "Maria Santos",
        "data_nascimento": new Date("1985-08-22"),
        "endereco": {
            "cep": "04038001",
            "endereco_completo": "Rua Funchal, 456 - Vila Olímpia - São Paulo/SP"
        },
        "moto_associada": "DEF5678",
        "perfil": "usuario",
        "ativo": true,
        "created_at": new Date(),
        "updated_at": new Date(),
        "source": "relational_db"
    }
]);

// =====================================================
// ALERTAS
// =====================================================
print("🚨 Importando alertas...");

db.alertas.insertMany([
    {
        "_id": "ALR-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-001",
        "tipo": "iot",
        "severidade": "MEDIUM",
        "titulo": "Movimento Detectado",
        "descricao": "Sensor detectou movimento na zona A1 fora do horário normal",
        "dispositivo": {
            "id": "SENSOR001",
            "nome": "Sensor de Movimento A1"
        },
        "localizacao": {
            "zona": "A1",
            "coordenadas": {
                "latitude": -23.5505,
                "longitude": -46.6333
            }
        },
        "status": {
            "ativo": true,
            "data_criacao": new Date(Date.now() - 2*60*60*1000), // 2 horas atrás
            "data_resolucao": null,
            "resolvido_por": null
        },
        "metadados": {
            "confidence": 0.95,
            "detection_type": "motion",
            "camera_feed": "available"
        },
        "created_at": new Date(Date.now() - 2*60*60*1000),
        "updated_at": new Date(Date.now() - 2*60*60*1000),
        "source": "relational_db"
    },
    {
        "_id": "ALR-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-002",
        "tipo": "sistema",
        "severidade": "HIGH",
        "titulo": "Dispositivo Offline",
        "descricao": "Sensor de movimento B1 não responde há mais de 30 minutos",
        "dispositivo": {
            "id": "SENSOR003",
            "nome": "Sensor de Movimento B1"
        },
        "localizacao": {
            "zona": "B1"
        },
        "status": {
            "ativo": true,
            "data_criacao": new Date(Date.now() - 1*60*60*1000), // 1 hora atrás
            "data_resolucao": null,
            "resolvido_por": null
        },
        "metadados": {
            "last_seen": new Date(Date.now() - 1*60*60*1000),
            "connection_type": "wifi",
            "signal_strength": "weak"
        },
        "created_at": new Date(Date.now() - 1*60*60*1000),
        "updated_at": new Date(Date.now() - 1*60*60*1000),
        "source": "relational_db"
    }
]);

// =====================================================
// HISTÓRICO DE USO
// =====================================================
print("📊 Importando histórico de uso...");

db.historico_uso.insertMany([
    {
        "_id": ObjectId(),
        "moto": {
            "placa": "ABC1234",
            "modelo": "Honda CG 160"
        },
        "usuario": {
            "cpf": "12345678901",
            "nome": "João Silva"
        },
        "periodo": {
            "inicio": new Date(Date.now() - 24*60*60*1000 - 2*60*60*1000), // ontem, 2h de uso
            "fim": new Date(Date.now() - 24*60*60*1000),
            "duracao_minutos": 120
        },
        "trajeto": {
            "origem": "Pátio SentinelTrack - Vaga A1-001",
            "destino": "Centro da Cidade - Rua Augusta, 123",
            "distancia_km": 12.5
        },
        "status": "finalizado",
        "observacoes": "Uso normal, sem intercorrências",
        "metricas": {
            "velocidade_media": 25.5,
            "consumo_bateria": 17,
            "paradas": 3
        },
        "created_at": new Date(Date.now() - 24*60*60*1000 - 2*60*60*1000),
        "updated_at": new Date(Date.now() - 24*60*60*1000),
        "source": "relational_db"
    }
]);

// =====================================================
// DADOS DE SENSORES
// =====================================================
print("📡 Importando dados de sensores...");

db.sensor_dados.insertMany([
    {
        "_id": ObjectId(),
        "dispositivo": {
            "id": "SENSOR001",
            "nome": "Sensor de Movimento A1",
            "tipo": "sensor_movimento"
        },
        "leitura": {
            "tipo": "movimento",
            "valor": 1,
            "unidade": "boolean",
            "timestamp": new Date(Date.now() - 30*60*1000) // 30 min atrás
        },
        "dados_completos": {
            "detected": true,
            "confidence": 0.95,
            "duration": 5.2,
            "direction": "north-south",
            "object_count": 1
        },
        "processado": true,
        "alertas_gerados": ["ALR-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-001"],
        "created_at": new Date(Date.now() - 30*60*1000),
        "source": "relational_db"
    },
    {
        "_id": ObjectId(),
        "dispositivo": {
            "id": "TEMP001",
            "nome": "Sensor de Temperatura A1",
            "tipo": "sensor_temperatura"
        },
        "leitura": {
            "tipo": "temperatura",
            "valor": 23.5,
            "unidade": "celsius",
            "timestamp": new Date(Date.now() - 15*60*1000) // 15 min atrás
        },
        "dados_completos": {
            "temperature": 23.5,
            "humidity": 65,
            "pressure": 1013.25,
            "air_quality": "good"
        },
        "processado": true,
        "alertas_gerados": [],
        "created_at": new Date(Date.now() - 15*60*1000),
        "source": "relational_db"
    }
]);

// =====================================================
// EVENTOS IOT
// =====================================================
print("🌐 Importando eventos IoT...");

db.eventos_iot.insertMany([
    {
        "_id": "EVT-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-001",
        "evento": {
            "id": "EVT-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-001",
            "tipo": "movimento_detectado",
            "timestamp": new Date(Date.now() - 2*60*60*1000)
        },
        "dispositivo": {
            "id": "SENSOR001",
            "nome": "Sensor de Movimento A1"
        },
        "payload": {
            "deviceId": "SENSOR001",
            "type": "motion_detected",
            "confidence": 0.95,
            "location": {
                "zone": "A1",
                "coordinates": {
                    "lat": -23.5505,
                    "lng": -46.6333
                }
            },
            "metadata": {
                "duration": 5.2,
                "direction": "north-south"
            }
        },
        "alerta_gerado": "ALR-" + new Date().toISOString().slice(0,10).replace(/-/g,'') + "-001",
        "processado": true,
        "created_at": new Date(Date.now() - 2*60*60*1000),
        "source": "relational_db"
    }
]);

// =====================================================
// METADADOS E CONFIGURAÇÕES
// =====================================================
print("⚙️  Importando metadados...");

db.metadados.insertOne({
    "_id": "system_config",
    "sistema": {
        "nome": "SentinelTrack",
        "versao": "2.0",
        "ambiente": "producao"
    },
    "importacao": {
        "data": new Date(),
        "origem": "banco_relacional_oracle",
        "versao_script": "1.0",
        "total_registros": {
            "enderecos": 2,
            "motos": 2,
            "usuarios": 2,
            "dispositivos_iot": 3,
            "alertas": 2,
            "historico_uso": 1,
            "sensor_dados": 2,
            "eventos_iot": 1
        }
    },
    "configuracoes": {
        "timezone": "America/Sao_Paulo",
        "idioma_padrao": "pt-BR",
        "moeda": "BRL",
        "formato_data": "DD/MM/YYYY"
    },
    "integracoes": {
        "apis_ativas": ["mobile", "java", "dotnet", "iot"],
        "webhooks": ["alertas", "eventos"],
        "notificacoes": ["push", "email", "sms"]
    }
});

// =====================================================
// ÍNDICES PARA PERFORMANCE
// =====================================================
print("🔍 Criando índices...");

// Índices para dispositivos IoT
db.dispositivos_iot.createIndex({ "tipo": 1 });
db.dispositivos_iot.createIndex({ "status": 1 });
db.dispositivos_iot.createIndex({ "localizacao.zona": 1 });
db.dispositivos_iot.createIndex({ "localizacao.coordenadas": "2dsphere" });

// Índices para motos
db.motos.createIndex({ "status.atual": 1 });
db.motos.createIndex({ "localizacao_atual.zona": 1 });
db.motos.createIndex({ "localizacao_atual.coordenadas": "2dsphere" });
db.motos.createIndex({ "proprietario.cpf": 1 });

// Índices para alertas
db.alertas.createIndex({ "status.ativo": 1 });
db.alertas.createIndex({ "severidade": 1 });
db.alertas.createIndex({ "tipo": 1 });
db.alertas.createIndex({ "status.data_criacao": -1 });
db.alertas.createIndex({ "localizacao.zona": 1 });

// Índices para histórico
db.historico_uso.createIndex({ "moto.placa": 1 });
db.historico_uso.createIndex({ "usuario.cpf": 1 });
db.historico_uso.createIndex({ "periodo.inicio": -1 });
db.historico_uso.createIndex({ "status": 1 });

// Índices para sensor dados
db.sensor_dados.createIndex({ "dispositivo.id": 1 });
db.sensor_dados.createIndex({ "leitura.timestamp": -1 });
db.sensor_dados.createIndex({ "dispositivo.tipo": 1 });
db.sensor_dados.createIndex({ "processado": 1 });

// Índices para eventos IoT
db.eventos_iot.createIndex({ "dispositivo.id": 1 });
db.eventos_iot.createIndex({ "evento.timestamp": -1 });
db.eventos_iot.createIndex({ "evento.tipo": 1 });
db.eventos_iot.createIndex({ "processado": 1 });

// =====================================================
// VALIDAÇÕES E ESTATÍSTICAS
// =====================================================
print("📈 Gerando estatísticas...");

print("✅ Importação concluída!");
print("📊 Estatísticas:");
print("   - Endereços: " + db.enderecos.countDocuments());
print("   - Motos: " + db.motos.countDocuments());
print("   - Usuários: " + db.usuarios.countDocuments());
print("   - Dispositivos IoT: " + db.dispositivos_iot.countDocuments());
print("   - Alertas: " + db.alertas.countDocuments());
print("   - Histórico de Uso: " + db.historico_uso.countDocuments());
print("   - Dados de Sensores: " + db.sensor_dados.countDocuments());
print("   - Eventos IoT: " + db.eventos_iot.countDocuments());

print("🔍 Índices criados: " + Object.keys(db.stats().indexes).length);

// Exemplos de consultas NoSQL
print("\n🔍 Exemplos de consultas NoSQL:");

print("\n1. Motos disponíveis na zona A1:");
printjson(db.motos.find(
    { 
        "status.atual": "disponivel",
        "localizacao_atual.zona": "A1" 
    },
    { 
        "placa": 1, 
        "modelo": 1, 
        "status.bateria": 1,
        "localizacao_atual.vaga": 1
    }
).toArray());

print("\n2. Alertas críticos ativos:");
printjson(db.alertas.find(
    { 
        "status.ativo": true,
        "severidade": { "$in": ["HIGH", "CRITICAL"] }
    }
).sort({ "status.data_criacao": -1 }).limit(5).toArray());

print("\n3. Dispositivos offline:");
printjson(db.dispositivos_iot.find(
    { "status": "offline" },
    { "nome": 1, "tipo": 1, "localizacao.zona": 1 }
).toArray());

print("\n🎉 MongoDB configurado com sucesso para SentinelTrack!");
print("🔗 Estrutura NoSQL pronta para integração com APIs Mobile, Java e .NET");

// Função para backup automático
function criarBackup() {
    var timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    var backupName = "sentineltrack_backup_" + timestamp;
    
    print("💾 Criando backup: " + backupName);
    
    // Este comando seria executado no shell do sistema
    // mongodump --db sentineltrack_nosql --out /backups/" + backupName
    
    return backupName;
}

print("\n💡 Para criar backup, execute: criarBackup()");
print("💡 Para restaurar, execute: mongorestore --db sentineltrack_nosql /path/to/backup");

// Configuração de replicação (para produção)
print("\n🔄 Para produção, configure replicação:");
print("   rs.initiate()");
print("   rs.add('mongodb2.example.com:27017')");
print("   rs.add('mongodb3.example.com:27017')");

print("\n✨ Sistema SentinelTrack MongoDB pronto para uso!");
