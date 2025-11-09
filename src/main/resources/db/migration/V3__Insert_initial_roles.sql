-- V3: Inserção dos perfis iniciais do sistema
INSERT INTO ST_ROLE (NOME, DESCRICAO) VALUES 
('ADMIN', 'Administrador do sistema - acesso total'),
('GERENTE', 'Gerente de pátio - gerencia motos e pátios'),
('OPERADOR', 'Operador - visualiza e opera motos'),
('VIEWER', 'Visualizador - apenas consulta dados');

-- Inserção de usuários padrão para teste
-- Senha: admin123 (deve ser criptografada em produção)
INSERT INTO ST_USUARIO (USERNAME, PASSWORD, EMAIL, NOME_COMPLETO, ATIVO) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P2.nRs.b7J2TJO', 'admin@mottu.com', 'Administrador Sistema', TRUE),
('gerente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P2.nRs.b7J2TJO', 'gerente@mottu.com', 'Gerente Pátio', TRUE),
('operador', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P2.nRs.b7J2TJO', 'operador@mottu.com', 'Operador Sistema', TRUE);

-- Associação usuário-perfil
INSERT INTO ST_USUARIO_ROLE (ID_USUARIO, ID_ROLE) VALUES 
(1, 1), -- admin -> ADMIN
(2, 2), -- gerente -> GERENTE  
(3, 3); -- operador -> OPERADOR
