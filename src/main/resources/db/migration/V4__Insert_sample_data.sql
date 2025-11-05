-- V4: Inserção de dados de exemplo para demonstração
-- Inserção de pátios de exemplo
INSERT INTO ST_PATIO (NOME, ENDERECO, COMPLEMENTO, AREA_M2, ID_LOCALIDADE) VALUES 
('Pátio Central São Paulo', 'Av. Paulista, 1000', 'Próximo ao metrô', 5000.00, 1),
('Pátio Zona Norte', 'Rua das Flores, 500', 'Entrada pela Rua A', 3500.50, 2),
('Pátio Zona Sul', 'Av. Ibirapuera, 2000', 'Ao lado do parque', 4200.75, 3),
('Pátio ABC', 'Rua Industrial, 100', 'Distrito Industrial', 6000.00, 4);

-- Inserção de motos de exemplo
INSERT INTO ST_MOTO (MODELO, PLACA, STATUS, ID_PATIO, DATA_ENTRADA) VALUES 
('Honda CG 160', 'ABC1234', 'DISPONIVEL', 1, '2024-01-15'),
('Yamaha Factor 125', 'DEF5678', 'EM_USO', 1, '2024-01-16'),
('Honda Biz 125', 'GHI9012', 'MANUTENCAO', 2, '2024-01-17'),
('Yamaha XTZ 150', 'JKL3456', 'DISPONIVEL', 2, '2024-01-18'),
('Honda CB 600F', 'MNO7890', 'EM_USO', 3, '2024-01-19'),
('Suzuki Burgman 125', 'PQR1234', 'DISPONIVEL', 3, '2024-01-20'),
('Honda PCX 150', 'STU5678', 'DISPONIVEL', 4, '2024-01-21'),
('Yamaha NMAX 160', 'VWX9012', 'MANUTENCAO', 4, '2024-01-22'),
('Honda CB 250F', 'YZA3456', 'DISPONIVEL', 1, '2024-01-23'),
('Kawasaki Ninja 300', 'BCD7890', 'EM_USO', 2, '2024-01-24');
