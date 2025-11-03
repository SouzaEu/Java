-- =====================================================
-- SCRIPT DE EXPORTAÇÃO PARA JSON
-- Challenge 2025 - 4º Sprint - Database
-- Exporta dados do banco relacional para formato JSON
-- =====================================================

-- Configuração para saída em arquivo
SET PAGESIZE 0
SET LINESIZE 4000
SET TRIMSPOOL ON
SET FEEDBACK OFF
SET HEADING OFF

-- Spool para arquivo JSON
SPOOL sentineltrack_export.json

-- Início do JSON
SELECT '{' FROM DUAL;
SELECT '  "export_info": {' FROM DUAL;
SELECT '    "database": "SentinelTrack",' FROM DUAL;
SELECT '    "export_date": "' || TO_CHAR(SYSDATE, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' FROM DUAL;
SELECT '    "version": "1.0",' FROM DUAL;
SELECT '    "description": "Exportação completa do sistema SentinelTrack para integração MongoDB"' FROM DUAL;
SELECT '  },' FROM DUAL;

-- Exportar Endereços
SELECT '  "enderecos": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY NR_CEP) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"cep": "' || NR_CEP || '",' ||
    '"pais": "' || NVL(ID_PAIS, '') || '",' ||
    '"estado": "' || NVL(SG_ESTADO, '') || '",' ||
    '"cidade": "' || NVL(ID_CIDADE, '') || '",' ||
    '"bairro": "' || NVL(ID_BAIRRO, '') || '",' ||
    '"numero": ' || NVL(NR_NUMERO, 0) || ',' ||
    '"logradouro": "' || NVL(REPLACE(DS_LOGRADOURO, '"', '\"'), '') || '",' ||
    '"complemento": "' || NVL(REPLACE(DS_COMPLEMENTO, '"', '\"'), '') || '"' ||
    '}'
FROM T_MT_ENDERECO
ORDER BY NR_CEP;
SELECT '  ],' FROM DUAL;

-- Exportar Motos
SELECT '  "motos": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY CD_PLACA) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"placa": "' || CD_PLACA || '",' ||
    '"cpf_proprietario": "' || NVL(CD_CPF, '') || '",' ||
    '"niv": "' || NVL(CD_NIV, '') || '",' ||
    '"motor": "' || NVL(CD_MOTOR, '') || '",' ||
    '"renavam": ' || NVL(CD_RENAVAM, 0) || ',' ||
    '"fipe": ' || NVL(CD_FIPE, 0) ||
    '}'
FROM T_MT_MOTO
ORDER BY CD_PLACA;
SELECT '  ],' FROM DUAL;

-- Exportar Usuários
SELECT '  "usuarios": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY CD_CPF) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"cpf": "' || CD_CPF || '",' ||
    '"nome": "' || REPLACE(ID_NOME, '"', '\"') || '",' ||
    '"data_nascimento": "' || TO_CHAR(DT_NASCIMENTO, 'YYYY-MM-DD') || '",' ||
    '"cep": "' || NR_CEP || '",' ||
    '"placa_moto": "' || CD_PLACA || '"' ||
    '}'
FROM T_MT_USUARIO
ORDER BY CD_CPF;
SELECT '  ],' FROM DUAL;

-- Exportar Funcionários
SELECT '  "funcionarios": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY ID_FUNCIONARIO) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": ' || ID_FUNCIONARIO || ',' ||
    '"nome": "' || NVL(REPLACE(ID_NOME, '"', '\"'), '') || '",' ||
    '"cpf": "' || NVL(CD_CPF, '') || '",' ||
    '"cep": "' || NR_CEP || '"' ||
    '}'
FROM T_MT_FUNCIONARIO
ORDER BY ID_FUNCIONARIO;
SELECT '  ],' FROM DUAL;

-- Exportar Roles
SELECT '  "roles": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY ID_ROLE) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": ' || ID_ROLE || ',' ||
    '"nome": "' || NM_ROLE || '"' ||
    '}'
FROM T_MT_ROLE
ORDER BY ID_ROLE;
SELECT '  ],' FROM DUAL;

-- Exportar Dispositivos IoT
SELECT '  "dispositivos_iot": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY ID_DISPOSITIVO) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": "' || ID_DISPOSITIVO || '",' ||
    '"nome": "' || REPLACE(NM_DISPOSITIVO, '"', '\"') || '",' ||
    '"tipo": "' || TP_DISPOSITIVO || '",' ||
    '"status": "' || ST_STATUS || '",' ||
    '"localizacao": "' || NVL(REPLACE(DS_LOCALIZACAO, '"', '\"'), '') || '",' ||
    '"zona": "' || NVL(CD_ZONA, '') || '",' ||
    '"latitude": ' || NVL(NR_LATITUDE, 0) || ',' ||
    '"longitude": ' || NVL(NR_LONGITUDE, 0) || ',' ||
    '"data_criacao": "' || TO_CHAR(DT_CRIACAO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"configuracao": ' || NVL(TX_CONFIGURACAO, '{}') ||
    '}'
FROM T_MT_DISPOSITIVO_IOT
ORDER BY ID_DISPOSITIVO;
SELECT '  ],' FROM DUAL;

-- Exportar Localizações das Motos
SELECT '  "localizacoes_motos": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY CD_PLACA) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"placa": "' || CD_PLACA || '",' ||
    '"latitude": ' || NVL(NR_LATITUDE, 0) || ',' ||
    '"longitude": ' || NVL(NR_LONGITUDE, 0) || ',' ||
    '"zona": "' || NVL(CD_ZONA, '') || '",' ||
    '"endereco": "' || NVL(REPLACE(DS_ENDERECO, '"', '\"'), '') || '",' ||
    '"setor": "' || NVL(DS_SETOR, '') || '",' ||
    '"andar": ' || NVL(NR_ANDAR, 1) || ',' ||
    '"vaga": "' || NVL(CD_VAGA, '') || '",' ||
    '"descricao_localizacao": "' || NVL(REPLACE(DS_DESCRICAO_LOC, '"', '\"'), '') || '",' ||
    '"status": "' || ST_STATUS_MOTO || '",' ||
    '"bateria": ' || NVL(NR_BATERIA, 100) || ',' ||
    '"ultima_atualizacao": "' || TO_CHAR(DT_ULTIMA_ATU, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"cpf_em_uso": "' || NVL(CD_CPF_EM_USO, '') || '"' ||
    '}'
FROM T_MT_LOCALIZACAO_MOTO
ORDER BY CD_PLACA;
SELECT '  ],' FROM DUAL;

-- Exportar Alertas
SELECT '  "alertas": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY DT_CRIACAO DESC) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": "' || ID_ALERTA || '",' ||
    '"tipo": "' || TP_ALERTA || '",' ||
    '"severidade": "' || NV_SEVERIDADE || '",' ||
    '"titulo": "' || REPLACE(TX_TITULO, '"', '\"') || '",' ||
    '"descricao": "' || NVL(REPLACE(TX_DESCRICAO, '"', '\"'), '') || '",' ||
    '"placa_moto": "' || NVL(CD_PLACA_MOTO, '') || '",' ||
    '"dispositivo_id": "' || NVL(ID_DISPOSITIVO, '') || '",' ||
    '"zona": "' || NVL(CD_ZONA, '') || '",' ||
    '"ativo": "' || ST_ATIVO || '",' ||
    '"data_criacao": "' || TO_CHAR(DT_CRIACAO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"data_resolucao": "' || NVL(TO_CHAR(DT_RESOLUCAO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"'), '') || '",' ||
    '"resolvido_por": ' || NVL(ID_RESOLVIDO_POR, 0) ||
    '}'
FROM T_MT_ALERTA
ORDER BY DT_CRIACAO DESC;
SELECT '  ],' FROM DUAL;

-- Exportar Histórico de Uso
SELECT '  "historico_uso": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY DT_INICIO_USO DESC) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": ' || ID_HISTORICO || ',' ||
    '"placa": "' || CD_PLACA || '",' ||
    '"cpf_usuario": "' || NVL(CD_CPF_USUARIO, '') || '",' ||
    '"inicio_uso": "' || TO_CHAR(DT_INICIO_USO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"fim_uso": "' || NVL(TO_CHAR(DT_FIM_USO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"'), '') || '",' ||
    '"localizacao_inicial": "' || NVL(REPLACE(DS_LOC_INICIAL, '"', '\"'), '') || '",' ||
    '"localizacao_final": "' || NVL(REPLACE(DS_LOC_FINAL, '"', '\"'), '') || '",' ||
    '"distancia_km": ' || NVL(NR_DISTANCIA_KM, 0) || ',' ||
    '"tempo_uso_min": ' || NVL(NR_TEMPO_USO_MIN, 0) || ',' ||
    '"status": "' || ST_USO || '",' ||
    '"observacoes": "' || NVL(REPLACE(TX_OBSERVACOES, '"', '\"'), '') || '"' ||
    '}'
FROM T_MT_HISTORICO_USO
ORDER BY DT_INICIO_USO DESC;
SELECT '  ],' FROM DUAL;

-- Exportar Dados dos Sensores (últimas 100 leituras)
SELECT '  "sensor_dados": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY DT_LEITURA DESC) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id": ' || ID_LEITURA || ',' ||
    '"dispositivo_id": "' || ID_DISPOSITIVO || '",' ||
    '"tipo_sensor": "' || TP_SENSOR || '",' ||
    '"valor_leitura": ' || NVL(VL_LEITURA, 0) || ',' ||
    '"dados_json": ' || NVL(TX_DADOS_JSON, '{}') || ',' ||
    '"data_leitura": "' || TO_CHAR(DT_LEITURA, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"processado": "' || ST_PROCESSADO || '"' ||
    '}'
FROM (
    SELECT * FROM T_MT_SENSOR_DADOS 
    ORDER BY DT_LEITURA DESC 
    FETCH FIRST 100 ROWS ONLY
)
ORDER BY DT_LEITURA DESC;
SELECT '  ],' FROM DUAL;

-- Exportar Eventos IoT
SELECT '  "eventos_iot": [' FROM DUAL;
SELECT 
    CASE 
        WHEN ROW_NUMBER() OVER (ORDER BY DT_EVENTO DESC) = 1 THEN '    {'
        ELSE '    ,{'
    END ||
    '"id_evento": "' || ID_EVENTO || '",' ||
    '"id_alerta": "' || ID_ALERTA || '",' ||
    '"dispositivo_id": "' || NVL(ID_DISPOSITIVO, '') || '",' ||
    '"tipo_evento": "' || NVL(TP_EVENTO, '') || '",' ||
    '"payload": ' || NVL(TX_PAYLOAD, '{}') || ',' ||
    '"data_evento": "' || TO_CHAR(DT_EVENTO, 'YYYY-MM-DD"T"HH24:MI:SS"Z"') || '",' ||
    '"processado": "' || ST_PROCESSADO || '"' ||
    '}'
FROM T_MT_EVENTO_IOT
ORDER BY DT_EVENTO DESC;
SELECT '  ],' FROM DUAL;

-- Estatísticas do sistema
SELECT '  "estatisticas": {' FROM DUAL;
SELECT '    "total_motos": ' || COUNT(*) || ',' FROM T_MT_MOTO;
SELECT '    "total_usuarios": ' || COUNT(*) || ',' FROM T_MT_USUARIO;
SELECT '    "total_funcionarios": ' || COUNT(*) || ',' FROM T_MT_FUNCIONARIO;
SELECT '    "total_dispositivos_iot": ' || COUNT(*) || ',' FROM T_MT_DISPOSITIVO_IOT;
SELECT '    "total_alertas_ativos": ' || COUNT(*) || ',' FROM T_MT_ALERTA WHERE ST_ATIVO = 'S';
SELECT '    "motos_disponiveis": ' || COUNT(*) || ',' FROM T_MT_LOCALIZACAO_MOTO WHERE ST_STATUS_MOTO = 'DISPONIVEL';
SELECT '    "motos_em_uso": ' || COUNT(*) || ',' FROM T_MT_LOCALIZACAO_MOTO WHERE ST_STATUS_MOTO = 'EM_USO';
SELECT '    "dispositivos_online": ' || COUNT(*) || ',' FROM T_MT_DISPOSITIVO_IOT WHERE ST_STATUS = 'ONLINE';
SELECT '    "usos_hoje": ' || COUNT(*) FROM T_MT_HISTORICO_USO WHERE DATE(DT_INICIO_USO) = DATE(SYSDATE);
SELECT '  },' FROM DUAL;

-- Metadados das tabelas
SELECT '  "metadados": {' FROM DUAL;
SELECT '    "tabelas": [' FROM DUAL;
SELECT '      {"nome": "T_MT_ENDERECO", "descricao": "Endereços do sistema"},' FROM DUAL;
SELECT '      {"nome": "T_MT_MOTO", "descricao": "Cadastro de motocicletas"},' FROM DUAL;
SELECT '      {"nome": "T_MT_USUARIO", "descricao": "Usuários do sistema"},' FROM DUAL;
SELECT '      {"nome": "T_MT_FUNCIONARIO", "descricao": "Funcionários da empresa"},' FROM DUAL;
SELECT '      {"nome": "T_MT_ROLE", "descricao": "Perfis de acesso"},' FROM DUAL;
SELECT '      {"nome": "T_MT_DISPOSITIVO_IOT", "descricao": "Dispositivos IoT cadastrados"},' FROM DUAL;
SELECT '      {"nome": "T_MT_LOCALIZACAO_MOTO", "descricao": "Localização em tempo real das motos"},' FROM DUAL;
SELECT '      {"nome": "T_MT_ALERTA", "descricao": "Sistema de alertas integrado"},' FROM DUAL;
SELECT '      {"nome": "T_MT_HISTORICO_USO", "descricao": "Histórico de utilização das motos"},' FROM DUAL;
SELECT '      {"nome": "T_MT_SENSOR_DADOS", "descricao": "Dados coletados pelos sensores"},' FROM DUAL;
SELECT '      {"nome": "T_MT_EVENTO_IOT", "descricao": "Eventos IoT com idempotência"}' FROM DUAL;
SELECT '    ]' FROM DUAL;
SELECT '  }' FROM DUAL;

-- Fim do JSON
SELECT '}' FROM DUAL;

SPOOL OFF

-- Restaurar configurações
SET PAGESIZE 24
SET LINESIZE 80
SET FEEDBACK ON
SET HEADING ON

-- Mensagem de confirmação
SELECT 'Exportação concluída! Arquivo: sentineltrack_export.json' as RESULTADO FROM DUAL;
SELECT 'Total de registros exportados:' as INFO FROM DUAL;
SELECT '- Endereços: ' || COUNT(*) FROM T_MT_ENDERECO;
SELECT '- Motos: ' || COUNT(*) FROM T_MT_MOTO;
SELECT '- Usuários: ' || COUNT(*) FROM T_MT_USUARIO;
SELECT '- Funcionários: ' || COUNT(*) FROM T_MT_FUNCIONARIO;
SELECT '- Dispositivos IoT: ' || COUNT(*) FROM T_MT_DISPOSITIVO_IOT;
SELECT '- Localizações: ' || COUNT(*) FROM T_MT_LOCALIZACAO_MOTO;
SELECT '- Alertas: ' || COUNT(*) FROM T_MT_ALERTA;
SELECT '- Histórico de Uso: ' || COUNT(*) FROM T_MT_HISTORICO_USO;
SELECT '- Dados de Sensores: ' || COUNT(*) FROM T_MT_SENSOR_DADOS;
SELECT '- Eventos IoT: ' || COUNT(*) FROM T_MT_EVENTO_IOT;
