-- =====================================================
-- DADOS INICIAIS PARA INTEGRAÇÃO IOT
-- Challenge 2025 - 4º Sprint
-- =====================================================

-- Inserir dispositivos IoT
INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('SENSOR001', 'Sensor de Movimento A1', 'sensor_movimento', 'ONLINE', 'Zona A1 - Entrada Principal', 'A1', -23.5505, -46.6333);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('SENSOR002', 'Sensor de Movimento A2', 'sensor_movimento', 'ONLINE', 'Zona A2 - Área Central', 'A2', -23.5515, -46.6343);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('SENSOR003', 'Sensor de Movimento B1', 'sensor_movimento', 'OFFLINE', 'Zona B1 - Setor Norte', 'B1', -23.5525, -46.6353);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('CAMERA001', 'Câmera Principal', 'camera', 'ONLINE', 'Entrada Principal - Portaria', 'ENTRADA', -23.5500, -46.6330);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('LOCK001', 'Trava Inteligente A1', 'atuador_trava', 'ONLINE', 'Zona A1 - Sistema de Travamento', 'A1', -23.5505, -46.6333);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('ALARM001', 'Sistema de Alarme Central', 'atuador_alarme', 'ONLINE', 'Central de Monitoramento', 'CENTRAL', -23.5510, -46.6340);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('TEMP001', 'Sensor de Temperatura A1', 'sensor_temperatura', 'ONLINE', 'Zona A1 - Monitoramento Ambiental', 'A1', -23.5505, -46.6333);

INSERT INTO T_MT_DISPOSITIVO_IOT (ID_DISPOSITIVO, NM_DISPOSITIVO, TP_DISPOSITIVO, ST_STATUS, DS_LOCALIZACAO, CD_ZONA, NR_LATITUDE, NR_LONGITUDE) VALUES
('BAT001', 'Monitor de Bateria Geral', 'sensor_bateria', 'ONLINE', 'Sistema Central de Energia', 'CENTRAL', -23.5510, -46.6340);

-- Inserir localizações das motos (baseado nas motos existentes)
INSERT INTO T_MT_LOCALIZACAO_MOTO (CD_PLACA, NR_LATITUDE, NR_LONGITUDE, CD_ZONA, DS_ENDERECO, DS_SETOR, NR_ANDAR, CD_VAGA, DS_DESCRICAO_LOC, ST_STATUS_MOTO, NR_BATERIA) 
SELECT 
    CD_PLACA,
    CASE 
        WHEN ROWNUM = 1 THEN -23.5505
        WHEN ROWNUM = 2 THEN -23.5515
        WHEN ROWNUM = 3 THEN -23.5525
        WHEN ROWNUM = 4 THEN -23.5535
        ELSE -23.5545
    END,
    CASE 
        WHEN ROWNUM = 1 THEN -46.6333
        WHEN ROWNUM = 2 THEN -46.6343
        WHEN ROWNUM = 3 THEN -46.6353
        WHEN ROWNUM = 4 THEN -46.6363
        ELSE -46.6373
    END,
    CASE 
        WHEN ROWNUM <= 2 THEN 'A1'
        WHEN ROWNUM <= 4 THEN 'A2'
        ELSE 'B1'
    END,
    'Rua das Palmeiras, 123 - Pátio SentinelTrack',
    CASE 
        WHEN ROWNUM <= 3 THEN 'Setor A'
        ELSE 'Setor B'
    END,
    1,
    'VAGA-' || LPAD(ROWNUM, 3, '0'),
    CASE 
        WHEN ROWNUM = 1 THEN 'Próximo à entrada principal, primeira fileira'
        WHEN ROWNUM = 2 THEN 'Segunda fileira, próximo ao banheiro'
        WHEN ROWNUM = 3 THEN 'Primeira fileira, vaga coberta'
        WHEN ROWNUM = 4 THEN 'Área de manutenção, segundo andar'
        ELSE 'Térreo, próximo ao elevador'
    END,
    'DISPONIVEL',
    CASE 
        WHEN ROWNUM = 1 THEN 95
        WHEN ROWNUM = 2 THEN 78
        WHEN ROWNUM = 3 THEN 100
        WHEN ROWNUM = 4 THEN 45
        ELSE 88
    END
FROM T_MT_MOTO
WHERE ROWNUM <= 10;

-- Inserir alguns alertas de exemplo
INSERT INTO T_MT_ALERTA (ID_ALERTA, TP_ALERTA, NV_SEVERIDADE, TX_TITULO, TX_DESCRICAO, ID_DISPOSITIVO, CD_ZONA, DT_CRIACAO) VALUES
('ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-001', 'iot', 'MEDIUM', 'Movimento Detectado', 'Sensor detectou movimento na zona A1 fora do horário normal', 'SENSOR001', 'A1', CURRENT_TIMESTAMP - INTERVAL '2' HOUR);

INSERT INTO T_MT_ALERTA (ID_ALERTA, TP_ALERTA, NV_SEVERIDADE, TX_TITULO, TX_DESCRICAO, ID_DISPOSITIVO, CD_ZONA, DT_CRIACAO) VALUES
('ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-002', 'sistema', 'HIGH', 'Dispositivo Offline', 'Sensor de movimento B1 não responde há mais de 30 minutos', 'SENSOR003', 'B1', CURRENT_TIMESTAMP - INTERVAL '1' HOUR);

INSERT INTO T_MT_ALERTA (ID_ALERTA, TP_ALERTA, NV_SEVERIDADE, TX_TITULO, TX_DESCRICAO, CD_ZONA, DT_CRIACAO) VALUES
('ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-003', 'manutencao', 'LOW', 'Manutenção Preventiva', 'Manutenção preventiva agendada para zona A2', 'A2', CURRENT_TIMESTAMP + INTERVAL '1' DAY);

-- Inserir dados de sensores (simulando leituras)
INSERT INTO T_MT_SENSOR_DADOS (ID_DISPOSITIVO, TP_SENSOR, VL_LEITURA, TX_DADOS_JSON, DT_LEITURA) VALUES
('SENSOR001', 'movimento', 1, '{"detected": true, "confidence": 0.95, "timestamp": "' || TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD"T"HH24:MI:SS') || '"}', CURRENT_TIMESTAMP - INTERVAL '30' MINUTE);

INSERT INTO T_MT_SENSOR_DADOS (ID_DISPOSITIVO, TP_SENSOR, VL_LEITURA, TX_DADOS_JSON, DT_LEITURA) VALUES
('TEMP001', 'temperatura', 23.5, '{"temperature": 23.5, "humidity": 65, "unit": "celsius"}', CURRENT_TIMESTAMP - INTERVAL '15' MINUTE);

INSERT INTO T_MT_SENSOR_DADOS (ID_DISPOSITIVO, TP_SENSOR, VL_LEITURA, TX_DADOS_JSON, DT_LEITURA) VALUES
('BAT001', 'bateria', 85, '{"battery_level": 85, "voltage": 12.6, "status": "good"}', CURRENT_TIMESTAMP - INTERVAL '5' MINUTE);

INSERT INTO T_MT_SENSOR_DADOS (ID_DISPOSITIVO, TP_SENSOR, VL_LEITURA, TX_DADOS_JSON, DT_LEITURA) VALUES
('SENSOR002', 'movimento', 0, '{"detected": false, "last_motion": "' || TO_CHAR(CURRENT_TIMESTAMP - INTERVAL '2' HOUR, 'YYYY-MM-DD"T"HH24:MI:SS') || '"}', CURRENT_TIMESTAMP - INTERVAL '10' MINUTE);

-- Inserir histórico de uso de exemplo
DECLARE
    v_placa VARCHAR2(10);
    v_cpf VARCHAR2(20);
    v_resultado VARCHAR2(500);
BEGIN
    -- Busca primeira moto e usuário para exemplo
    SELECT CD_PLACA INTO v_placa FROM T_MT_MOTO WHERE ROWNUM = 1;
    SELECT CD_CPF INTO v_cpf FROM T_MT_USUARIO WHERE ROWNUM = 1;
    
    -- Simula uso finalizado ontem
    INSERT INTO T_MT_HISTORICO_USO (
        CD_PLACA, CD_CPF_USUARIO, DT_INICIO_USO, DT_FIM_USO, 
        DS_LOC_INICIAL, DS_LOC_FINAL, NR_TEMPO_USO_MIN, NR_DISTANCIA_KM, ST_USO
    ) VALUES (
        v_placa, v_cpf, 
        CURRENT_TIMESTAMP - INTERVAL '1' DAY - INTERVAL '2' HOUR,
        CURRENT_TIMESTAMP - INTERVAL '1' DAY - INTERVAL '30' MINUTE,
        'Pátio SentinelTrack - Vaga A1-001',
        'Centro da Cidade - Rua Augusta, 123',
        90, 12.5, 'FINALIZADO'
    );
    
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignora erros se não houver dados
END;
/

-- Inserir eventos IoT de exemplo
INSERT INTO T_MT_EVENTO_IOT (ID_EVENTO, ID_ALERTA, ID_DISPOSITIVO, TP_EVENTO, TX_PAYLOAD, DT_EVENTO) VALUES
('EVT-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-001', 'ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-001', 'SENSOR001', 'movimento_detectado', 
'{"deviceId": "SENSOR001", "type": "motion_detected", "confidence": 0.95, "location": {"zone": "A1", "coordinates": {"lat": -23.5505, "lng": -46.6333}}}', 
CURRENT_TIMESTAMP - INTERVAL '2' HOUR);

INSERT INTO T_MT_EVENTO_IOT (ID_EVENTO, ID_ALERTA, ID_DISPOSITIVO, TP_EVENTO, TX_PAYLOAD, DT_EVENTO) VALUES
('EVT-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-002', 'ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-002', 'SENSOR003', 'falha_comunicacao', 
'{"deviceId": "SENSOR003", "type": "communication_failure", "last_seen": "' || TO_CHAR(CURRENT_TIMESTAMP - INTERVAL '1' HOUR, 'YYYY-MM-DD"T"HH24:MI:SS') || '", "status": "offline"}', 
CURRENT_TIMESTAMP - INTERVAL '1' HOUR);

-- Inserir configurações dos dispositivos
UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"sensitivity": "high", "detection_range": 10, "alert_threshold": 0.8, "operating_hours": "24/7"}'
WHERE TP_DISPOSITIVO = 'sensor_movimento';

UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"resolution": "1080p", "fps": 30, "night_vision": true, "motion_detection": true, "recording": "continuous"}'
WHERE TP_DISPOSITIVO = 'camera';

UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"auto_lock": true, "unlock_method": ["rfid", "mobile_app"], "timeout": 300, "force_threshold": 50}'
WHERE TP_DISPOSITIVO = 'atuador_trava';

UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"volume": 85, "alert_types": ["intrusion", "fire", "emergency"], "notification_channels": ["sms", "email", "push"]}'
WHERE TP_DISPOSITIVO = 'atuador_alarme';

UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"min_temp": 15, "max_temp": 35, "alert_threshold": 5, "sample_rate": 60}'
WHERE TP_DISPOSITIVO = 'sensor_temperatura';

UPDATE T_MT_DISPOSITIVO_IOT 
SET TX_CONFIGURACAO = '{"low_battery_threshold": 20, "critical_threshold": 10, "monitoring_interval": 300}'
WHERE TP_DISPOSITIVO = 'sensor_bateria';

-- Commit das alterações
COMMIT;

-- Comentários sobre os dados inseridos
COMMENT ON TABLE T_MT_DISPOSITIVO_IOT IS 'Dispositivos IoT cadastrados: 8 dispositivos incluindo sensores, câmeras e atuadores';
COMMENT ON TABLE T_MT_LOCALIZACAO_MOTO IS 'Localizações das motos com coordenadas GPS e informações detalhadas de vaga';
COMMENT ON TABLE T_MT_ALERTA IS 'Sistema de alertas com 3 tipos: IoT, Sistema e Manutenção';
COMMENT ON TABLE T_MT_SENSOR_DADOS IS 'Dados coletados pelos sensores com formato JSON flexível';
COMMENT ON TABLE T_MT_HISTORICO_USO IS 'Histórico de utilização das motos com métricas de tempo e distância';
COMMENT ON TABLE T_MT_EVENTO_IOT IS 'Eventos IoT com controle de idempotência para integração externa';
