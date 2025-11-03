-- =====================================================
-- PROCEDURES E FUNCTIONS PARA INTEGRAÇÃO IOT
-- Challenge 2025 - 4º Sprint
-- Tratamento de exceções e lógica de negócio
-- =====================================================

-- FUNCTION: Validar CPF
CREATE OR REPLACE FUNCTION FN_VALIDAR_CPF(p_cpf VARCHAR2) 
RETURN NUMBER
IS
    v_cpf VARCHAR2(11);
    v_soma NUMBER := 0;
    v_resto NUMBER;
    v_digito1 NUMBER;
    v_digito2 NUMBER;
BEGIN
    -- Remove caracteres não numéricos
    v_cpf := REGEXP_REPLACE(p_cpf, '[^0-9]', '');
    
    -- Verifica se tem 11 dígitos
    IF LENGTH(v_cpf) != 11 THEN
        RETURN 0;
    END IF;
    
    -- Verifica sequências inválidas
    IF v_cpf IN ('00000000000', '11111111111', '22222222222', '33333333333',
                 '44444444444', '55555555555', '66666666666', '77777777777',
                 '88888888888', '99999999999') THEN
        RETURN 0;
    END IF;
    
    -- Calcula primeiro dígito verificador
    FOR i IN 1..9 LOOP
        v_soma := v_soma + TO_NUMBER(SUBSTR(v_cpf, i, 1)) * (11 - i);
    END LOOP;
    
    v_resto := MOD(v_soma, 11);
    IF v_resto < 2 THEN
        v_digito1 := 0;
    ELSE
        v_digito1 := 11 - v_resto;
    END IF;
    
    -- Verifica primeiro dígito
    IF v_digito1 != TO_NUMBER(SUBSTR(v_cpf, 10, 1)) THEN
        RETURN 0;
    END IF;
    
    -- Calcula segundo dígito verificador
    v_soma := 0;
    FOR i IN 1..10 LOOP
        v_soma := v_soma + TO_NUMBER(SUBSTR(v_cpf, i, 1)) * (12 - i);
    END LOOP;
    
    v_resto := MOD(v_soma, 11);
    IF v_resto < 2 THEN
        v_digito2 := 0;
    ELSE
        v_digito2 := 11 - v_resto;
    END IF;
    
    -- Verifica segundo dígito
    IF v_digito2 != TO_NUMBER(SUBSTR(v_cpf, 11, 1)) THEN
        RETURN 0;
    END IF;
    
    RETURN 1;
    
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END FN_VALIDAR_CPF;
/

-- FUNCTION: Calcular distância entre coordenadas (Haversine)
CREATE OR REPLACE FUNCTION FN_CALCULAR_DISTANCIA(
    p_lat1 NUMBER,
    p_lon1 NUMBER,
    p_lat2 NUMBER,
    p_lon2 NUMBER
) RETURN NUMBER
IS
    v_r NUMBER := 6371; -- Raio da Terra em km
    v_dlat NUMBER;
    v_dlon NUMBER;
    v_a NUMBER;
    v_c NUMBER;
    v_distancia NUMBER;
BEGIN
    -- Converte para radianos
    v_dlat := (p_lat2 - p_lat1) * 3.14159265359 / 180;
    v_dlon := (p_lon2 - p_lon1) * 3.14159265359 / 180;
    
    v_a := SIN(v_dlat/2) * SIN(v_dlat/2) + 
           COS(p_lat1 * 3.14159265359 / 180) * COS(p_lat2 * 3.14159265359 / 180) * 
           SIN(v_dlon/2) * SIN(v_dlon/2);
    
    v_c := 2 * ATAN2(SQRT(v_a), SQRT(1-v_a));
    v_distancia := v_r * v_c;
    
    RETURN ROUND(v_distancia, 2);
    
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END FN_CALCULAR_DISTANCIA;
/

-- FUNCTION: Obter status da moto
CREATE OR REPLACE FUNCTION FN_OBTER_STATUS_MOTO(p_placa VARCHAR2) 
RETURN VARCHAR2
IS
    v_status VARCHAR2(20);
BEGIN
    SELECT ST_STATUS_MOTO 
    INTO v_status
    FROM T_MT_LOCALIZACAO_MOTO 
    WHERE CD_PLACA = p_placa;
    
    RETURN v_status;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'NAO_ENCONTRADA';
    WHEN OTHERS THEN
        RETURN 'ERRO';
END FN_OBTER_STATUS_MOTO;
/

-- PROCEDURE: Registrar evento IoT com tratamento de exceções
CREATE OR REPLACE PROCEDURE SP_REGISTRAR_EVENTO_IOT(
    p_id_evento IN VARCHAR2,
    p_id_dispositivo IN VARCHAR2,
    p_tipo_evento IN VARCHAR2,
    p_payload IN CLOB,
    p_criar_alerta IN NUMBER DEFAULT 1,
    p_resultado OUT VARCHAR2
)
IS
    v_id_alerta VARCHAR2(50);
    v_titulo VARCHAR2(200);
    v_descricao CLOB;
    v_severidade VARCHAR2(20);
    v_count NUMBER;
    
    -- Exceções customizadas
    ex_evento_duplicado EXCEPTION;
    ex_dispositivo_inexistente EXCEPTION;
    ex_payload_invalido EXCEPTION;
    
BEGIN
    -- Validações de entrada
    IF p_id_evento IS NULL OR p_id_dispositivo IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'ID do evento e dispositivo são obrigatórios');
    END IF;
    
    -- Verifica se evento já existe (idempotência)
    SELECT COUNT(*) INTO v_count 
    FROM T_MT_EVENTO_IOT 
    WHERE ID_EVENTO = p_id_evento;
    
    IF v_count > 0 THEN
        RAISE ex_evento_duplicado;
    END IF;
    
    -- Verifica se dispositivo existe
    SELECT COUNT(*) INTO v_count 
    FROM T_MT_DISPOSITIVO_IOT 
    WHERE ID_DISPOSITIVO = p_id_dispositivo;
    
    IF v_count = 0 THEN
        RAISE ex_dispositivo_inexistente;
    END IF;
    
    -- Gera ID do alerta se necessário
    IF p_criar_alerta = 1 THEN
        v_id_alerta := 'ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-' || 
                       SUBSTR(RAWTOHEX(SYS_GUID()), 1, 6);
        
        -- Define título e severidade baseado no tipo
        CASE p_tipo_evento
            WHEN 'movimento_detectado' THEN
                v_titulo := 'Movimento Detectado';
                v_descricao := 'Sensor detectou movimento na área monitorada';
                v_severidade := 'MEDIUM';
            WHEN 'bateria_baixa' THEN
                v_titulo := 'Bateria Baixa';
                v_descricao := 'Nível de bateria crítico detectado';
                v_severidade := 'HIGH';
            WHEN 'moto_fora_vaga' THEN
                v_titulo := 'Moto Fora da Vaga';
                v_descricao := 'Motocicleta detectada fora da vaga designada';
                v_severidade := 'HIGH';
            WHEN 'falha_comunicacao' THEN
                v_titulo := 'Falha de Comunicação';
                v_descricao := 'Dispositivo não responde às comunicações';
                v_severidade := 'CRITICAL';
            ELSE
                v_titulo := 'Evento IoT Genérico';
                v_descricao := 'Evento IoT registrado pelo sistema';
                v_severidade := 'LOW';
        END CASE;
        
        -- Insere alerta
        INSERT INTO T_MT_ALERTA (
            ID_ALERTA, TP_ALERTA, NV_SEVERIDADE, TX_TITULO, TX_DESCRICAO,
            ID_DISPOSITIVO, DT_CRIACAO
        ) VALUES (
            v_id_alerta, 'iot', v_severidade, v_titulo, v_descricao,
            p_id_dispositivo, CURRENT_TIMESTAMP
        );
    END IF;
    
    -- Registra evento
    INSERT INTO T_MT_EVENTO_IOT (
        ID_EVENTO, ID_ALERTA, ID_DISPOSITIVO, TP_EVENTO, TX_PAYLOAD, DT_EVENTO
    ) VALUES (
        p_id_evento, v_id_alerta, p_id_dispositivo, p_tipo_evento, p_payload, CURRENT_TIMESTAMP
    );
    
    -- Atualiza status do dispositivo
    UPDATE T_MT_DISPOSITIVO_IOT 
    SET DT_ULTIMA_COM = CURRENT_TIMESTAMP,
        ST_STATUS = 'ONLINE'
    WHERE ID_DISPOSITIVO = p_id_dispositivo;
    
    COMMIT;
    p_resultado := 'SUCCESS:' || NVL(v_id_alerta, 'SEM_ALERTA');
    
EXCEPTION
    WHEN ex_evento_duplicado THEN
        ROLLBACK;
        p_resultado := 'WARNING:EVENTO_JA_EXISTE';
        
    WHEN ex_dispositivo_inexistente THEN
        ROLLBACK;
        p_resultado := 'ERROR:DISPOSITIVO_NAO_ENCONTRADO';
        
    WHEN OTHERS THEN
        ROLLBACK;
        p_resultado := 'ERROR:' || SQLERRM;
        
        -- Log do erro
        INSERT INTO T_MT_AUDITORIA (
            NM_TABELA, ID_REGISTRO, TP_OPERACAO, TX_DADOS_DEPOIS, DT_OPERACAO
        ) VALUES (
            'T_MT_EVENTO_IOT', p_id_evento, 'ERROR', 
            'Erro ao registrar evento: ' || SQLERRM, CURRENT_TIMESTAMP
        );
        COMMIT;
END SP_REGISTRAR_EVENTO_IOT;
/

-- PROCEDURE: Atualizar localização da moto
CREATE OR REPLACE PROCEDURE SP_ATUALIZAR_LOCALIZACAO_MOTO(
    p_placa IN VARCHAR2,
    p_latitude IN NUMBER,
    p_longitude IN NUMBER,
    p_zona IN VARCHAR2,
    p_endereco IN VARCHAR2,
    p_setor IN VARCHAR2,
    p_andar IN NUMBER,
    p_vaga IN VARCHAR2,
    p_descricao IN VARCHAR2,
    p_bateria IN NUMBER,
    p_resultado OUT VARCHAR2
)
IS
    v_count NUMBER;
    v_distancia NUMBER;
    v_lat_anterior NUMBER;
    v_lon_anterior NUMBER;
    
    -- Exceções customizadas
    ex_moto_inexistente EXCEPTION;
    ex_coordenadas_invalidas EXCEPTION;
    ex_bateria_invalida EXCEPTION;
    
BEGIN
    -- Validações
    IF p_placa IS NULL THEN
        RAISE_APPLICATION_ERROR(-20002, 'Placa da moto é obrigatória');
    END IF;
    
    IF p_latitude IS NULL OR p_longitude IS NULL THEN
        RAISE ex_coordenadas_invalidas;
    END IF;
    
    IF p_bateria IS NOT NULL AND (p_bateria < 0 OR p_bateria > 100) THEN
        RAISE ex_bateria_invalida;
    END IF;
    
    -- Verifica se moto existe
    SELECT COUNT(*) INTO v_count 
    FROM T_MT_MOTO 
    WHERE CD_PLACA = p_placa;
    
    IF v_count = 0 THEN
        RAISE ex_moto_inexistente;
    END IF;
    
    -- Obtém localização anterior para calcular distância
    BEGIN
        SELECT NR_LATITUDE, NR_LONGITUDE 
        INTO v_lat_anterior, v_lon_anterior
        FROM T_MT_LOCALIZACAO_MOTO 
        WHERE CD_PLACA = p_placa;
        
        -- Calcula distância se havia localização anterior
        IF v_lat_anterior IS NOT NULL AND v_lon_anterior IS NOT NULL THEN
            v_distancia := FN_CALCULAR_DISTANCIA(v_lat_anterior, v_lon_anterior, p_latitude, p_longitude);
        END IF;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_distancia := 0;
    END;
    
    -- Atualiza ou insere localização
    MERGE INTO T_MT_LOCALIZACAO_MOTO loc
    USING (SELECT p_placa as placa FROM DUAL) src
    ON (loc.CD_PLACA = src.placa)
    WHEN MATCHED THEN
        UPDATE SET 
            NR_LATITUDE = p_latitude,
            NR_LONGITUDE = p_longitude,
            CD_ZONA = NVL(p_zona, CD_ZONA),
            DS_ENDERECO = NVL(p_endereco, DS_ENDERECO),
            DS_SETOR = NVL(p_setor, DS_SETOR),
            NR_ANDAR = NVL(p_andar, NR_ANDAR),
            CD_VAGA = NVL(p_vaga, CD_VAGA),
            DS_DESCRICAO_LOC = NVL(p_descricao, DS_DESCRICAO_LOC),
            NR_BATERIA = NVL(p_bateria, NR_BATERIA),
            DT_ULTIMA_ATU = CURRENT_TIMESTAMP
    WHEN NOT MATCHED THEN
        INSERT (CD_PLACA, NR_LATITUDE, NR_LONGITUDE, CD_ZONA, DS_ENDERECO,
                DS_SETOR, NR_ANDAR, CD_VAGA, DS_DESCRICAO_LOC, NR_BATERIA, DT_ULTIMA_ATU)
        VALUES (p_placa, p_latitude, p_longitude, p_zona, p_endereco,
                p_setor, p_andar, p_vaga, p_descricao, NVL(p_bateria, 100), CURRENT_TIMESTAMP);
    
    -- Cria alerta se bateria baixa
    IF p_bateria IS NOT NULL AND p_bateria < 20 THEN
        INSERT INTO T_MT_ALERTA (
            ID_ALERTA, TP_ALERTA, NV_SEVERIDADE, TX_TITULO, TX_DESCRICAO,
            CD_PLACA_MOTO, DT_CRIACAO
        ) VALUES (
            'ALR-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-' || SUBSTR(RAWTOHEX(SYS_GUID()), 1, 6),
            'bateria', 'HIGH', 'Bateria Baixa', 
            'Moto ' || p_placa || ' com bateria em ' || p_bateria || '%',
            p_placa, CURRENT_TIMESTAMP
        );
    END IF;
    
    COMMIT;
    p_resultado := 'SUCCESS:DISTANCIA_' || NVL(TO_CHAR(v_distancia), '0') || 'KM';
    
EXCEPTION
    WHEN ex_moto_inexistente THEN
        ROLLBACK;
        p_resultado := 'ERROR:MOTO_NAO_ENCONTRADA';
        
    WHEN ex_coordenadas_invalidas THEN
        ROLLBACK;
        p_resultado := 'ERROR:COORDENADAS_INVALIDAS';
        
    WHEN ex_bateria_invalida THEN
        ROLLBACK;
        p_resultado := 'ERROR:BATERIA_INVALIDA';
        
    WHEN OTHERS THEN
        ROLLBACK;
        p_resultado := 'ERROR:' || SQLERRM;
END SP_ATUALIZAR_LOCALIZACAO_MOTO;
/

-- PROCEDURE: Iniciar uso da moto
CREATE OR REPLACE PROCEDURE SP_INICIAR_USO_MOTO(
    p_placa IN VARCHAR2,
    p_cpf_usuario IN VARCHAR2,
    p_localizacao_inicial IN VARCHAR2,
    p_resultado OUT VARCHAR2
)
IS
    v_count NUMBER;
    v_status_atual VARCHAR2(20);
    v_id_historico NUMBER;
    
    -- Exceções customizadas
    ex_moto_indisponivel EXCEPTION;
    ex_usuario_inexistente EXCEPTION;
    ex_moto_em_uso EXCEPTION;
    
BEGIN
    -- Validações
    IF p_placa IS NULL OR p_cpf_usuario IS NULL THEN
        RAISE_APPLICATION_ERROR(-20003, 'Placa e CPF são obrigatórios');
    END IF;
    
    -- Verifica se usuário existe e CPF é válido
    IF FN_VALIDAR_CPF(p_cpf_usuario) = 0 THEN
        RAISE_APPLICATION_ERROR(-20004, 'CPF inválido');
    END IF;
    
    SELECT COUNT(*) INTO v_count 
    FROM T_MT_USUARIO 
    WHERE CD_CPF = p_cpf_usuario;
    
    IF v_count = 0 THEN
        RAISE ex_usuario_inexistente;
    END IF;
    
    -- Verifica status da moto
    v_status_atual := FN_OBTER_STATUS_MOTO(p_placa);
    
    IF v_status_atual NOT IN ('DISPONIVEL', 'NAO_ENCONTRADA') THEN
        RAISE ex_moto_indisponivel;
    END IF;
    
    -- Verifica se usuário já tem moto em uso
    SELECT COUNT(*) INTO v_count 
    FROM T_MT_HISTORICO_USO 
    WHERE CD_CPF_USUARIO = p_cpf_usuario 
    AND ST_USO = 'EM_ANDAMENTO';
    
    IF v_count > 0 THEN
        RAISE ex_moto_em_uso;
    END IF;
    
    -- Inicia uso
    INSERT INTO T_MT_HISTORICO_USO (
        CD_PLACA, CD_CPF_USUARIO, DT_INICIO_USO, DS_LOC_INICIAL, ST_USO
    ) VALUES (
        p_placa, p_cpf_usuario, CURRENT_TIMESTAMP, p_localizacao_inicial, 'EM_ANDAMENTO'
    ) RETURNING ID_HISTORICO INTO v_id_historico;
    
    -- Atualiza status da moto
    UPDATE T_MT_LOCALIZACAO_MOTO 
    SET ST_STATUS_MOTO = 'EM_USO',
        CD_CPF_EM_USO = p_cpf_usuario
    WHERE CD_PLACA = p_placa;
    
    -- Se não existe registro de localização, cria um básico
    IF SQL%ROWCOUNT = 0 THEN
        INSERT INTO T_MT_LOCALIZACAO_MOTO (
            CD_PLACA, ST_STATUS_MOTO, CD_CPF_EM_USO, DT_ULTIMA_ATU
        ) VALUES (
            p_placa, 'EM_USO', p_cpf_usuario, CURRENT_TIMESTAMP
        );
    END IF;
    
    COMMIT;
    p_resultado := 'SUCCESS:HISTORICO_' || v_id_historico;
    
EXCEPTION
    WHEN ex_usuario_inexistente THEN
        ROLLBACK;
        p_resultado := 'ERROR:USUARIO_NAO_ENCONTRADO';
        
    WHEN ex_moto_indisponivel THEN
        ROLLBACK;
        p_resultado := 'ERROR:MOTO_INDISPONIVEL_STATUS_' || v_status_atual;
        
    WHEN ex_moto_em_uso THEN
        ROLLBACK;
        p_resultado := 'ERROR:USUARIO_JA_TEM_MOTO_EM_USO';
        
    WHEN OTHERS THEN
        ROLLBACK;
        p_resultado := 'ERROR:' || SQLERRM;
END SP_INICIAR_USO_MOTO;
/

-- PROCEDURE: Finalizar uso da moto
CREATE OR REPLACE PROCEDURE SP_FINALIZAR_USO_MOTO(
    p_placa IN VARCHAR2,
    p_cpf_usuario IN VARCHAR2,
    p_localizacao_final IN VARCHAR2,
    p_observacoes IN VARCHAR2,
    p_resultado OUT VARCHAR2
)
IS
    v_id_historico NUMBER;
    v_dt_inicio TIMESTAMP;
    v_tempo_uso NUMBER;
    v_count NUMBER;
    
    -- Exceções customizadas
    ex_uso_nao_encontrado EXCEPTION;
    ex_usuario_diferente EXCEPTION;
    
BEGIN
    -- Validações
    IF p_placa IS NULL OR p_cpf_usuario IS NULL THEN
        RAISE_APPLICATION_ERROR(-20005, 'Placa e CPF são obrigatórios');
    END IF;
    
    -- Busca uso em andamento
    BEGIN
        SELECT ID_HISTORICO, DT_INICIO_USO 
        INTO v_id_historico, v_dt_inicio
        FROM T_MT_HISTORICO_USO 
        WHERE CD_PLACA = p_placa 
        AND ST_USO = 'EM_ANDAMENTO'
        AND CD_CPF_USUARIO = p_cpf_usuario;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- Verifica se existe uso por outro usuário
            SELECT COUNT(*) INTO v_count 
            FROM T_MT_HISTORICO_USO 
            WHERE CD_PLACA = p_placa 
            AND ST_USO = 'EM_ANDAMENTO';
            
            IF v_count > 0 THEN
                RAISE ex_usuario_diferente;
            ELSE
                RAISE ex_uso_nao_encontrado;
            END IF;
    END;
    
    -- Calcula tempo de uso em minutos
    v_tempo_uso := ROUND((CURRENT_TIMESTAMP - v_dt_inicio) * 24 * 60);
    
    -- Finaliza uso
    UPDATE T_MT_HISTORICO_USO 
    SET DT_FIM_USO = CURRENT_TIMESTAMP,
        DS_LOC_FINAL = p_localizacao_final,
        NR_TEMPO_USO_MIN = v_tempo_uso,
        ST_USO = 'FINALIZADO',
        TX_OBSERVACOES = p_observacoes
    WHERE ID_HISTORICO = v_id_historico;
    
    -- Libera moto
    UPDATE T_MT_LOCALIZACAO_MOTO 
    SET ST_STATUS_MOTO = 'DISPONIVEL',
        CD_CPF_EM_USO = NULL
    WHERE CD_PLACA = p_placa;
    
    COMMIT;
    p_resultado := 'SUCCESS:TEMPO_USO_' || v_tempo_uso || '_MIN';
    
EXCEPTION
    WHEN ex_uso_nao_encontrado THEN
        ROLLBACK;
        p_resultado := 'ERROR:USO_NAO_ENCONTRADO';
        
    WHEN ex_usuario_diferente THEN
        ROLLBACK;
        p_resultado := 'ERROR:MOTO_EM_USO_POR_OUTRO_USUARIO';
        
    WHEN OTHERS THEN
        ROLLBACK;
        p_resultado := 'ERROR:' || SQLERRM;
END SP_FINALIZAR_USO_MOTO;
/

-- PROCEDURE: Gerar relatório de uso
CREATE OR REPLACE PROCEDURE SP_GERAR_RELATORIO_USO(
    p_data_inicio IN DATE,
    p_data_fim IN DATE,
    p_placa IN VARCHAR2 DEFAULT NULL,
    p_cpf_usuario IN VARCHAR2 DEFAULT NULL,
    p_cursor OUT SYS_REFCURSOR
)
IS
    v_sql VARCHAR2(4000);
BEGIN
    v_sql := 'SELECT 
                h.CD_PLACA,
                m.CD_NIV as modelo,
                h.CD_CPF_USUARIO,
                u.ID_NOME as nome_usuario,
                h.DT_INICIO_USO,
                h.DT_FIM_USO,
                h.NR_TEMPO_USO_MIN,
                h.NR_DISTANCIA_KM,
                h.DS_LOC_INICIAL,
                h.DS_LOC_FINAL,
                h.ST_USO,
                h.TX_OBSERVACOES
              FROM T_MT_HISTORICO_USO h
              JOIN T_MT_MOTO m ON h.CD_PLACA = m.CD_PLACA
              LEFT JOIN T_MT_USUARIO u ON h.CD_CPF_USUARIO = u.CD_CPF
              WHERE h.DT_INICIO_USO BETWEEN :p_data_inicio AND :p_data_fim';
    
    IF p_placa IS NOT NULL THEN
        v_sql := v_sql || ' AND h.CD_PLACA = :p_placa';
    END IF;
    
    IF p_cpf_usuario IS NOT NULL THEN
        v_sql := v_sql || ' AND h.CD_CPF_USUARIO = :p_cpf_usuario';
    END IF;
    
    v_sql := v_sql || ' ORDER BY h.DT_INICIO_USO DESC';
    
    IF p_placa IS NOT NULL AND p_cpf_usuario IS NOT NULL THEN
        OPEN p_cursor FOR v_sql USING p_data_inicio, p_data_fim, p_placa, p_cpf_usuario;
    ELSIF p_placa IS NOT NULL THEN
        OPEN p_cursor FOR v_sql USING p_data_inicio, p_data_fim, p_placa;
    ELSIF p_cpf_usuario IS NOT NULL THEN
        OPEN p_cursor FOR v_sql USING p_data_inicio, p_data_fim, p_cpf_usuario;
    ELSE
        OPEN p_cursor FOR v_sql USING p_data_inicio, p_data_fim;
    END IF;
    
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20006, 'Erro ao gerar relatório: ' || SQLERRM);
END SP_GERAR_RELATORIO_USO;
/
