-- Script de inicialização do banco de dados
-- Criação de usuário administrador padrão

INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil, data_criacao, ativo)
VALUES (
    'Administrador do Sistema',
    '00000000000',
    'admin@sistema.com',
    'Administrador',
    'admin',
    '$2a$10$9vZj8qGQQr4.1QJ4qF4Qx.yQ3.1QJ4qF4QxqF4QxqF4QxqF4QxqF4Q',
    'ADMINISTRADOR',
    NOW(),
    true
);

-- Usuário gerente de exemplo
INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil, data_criacao, ativo)
VALUES (
    'João Silva',
    '12345678901',
    'joao@empresa.com',
    'Gerente de Projetos',
    'joao.silva',
    '$2a$10$9vZj8qGQQr4.1QJ4qF4Qx.yQ3.1QJ4qF4QxqF4QxqF4QxqF4QxqF4Q',
    'GERENTE',
    NOW(),
    true
);

-- Usuário colaborador de exemplo
INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil, data_criacao, ativo)
VALUES (
    'Maria Santos',
    '98765432100',
    'maria@empresa.com',
    'Desenvolvedora',
    'maria.santos',
    '$2a$10$9vZj8qGQQr4.1QJ4qF4Qx.yQ3.1QJ4qF4QxqF4QxqF4QxqF4QxqF4Q',
    'COLABORADOR',
    NOW(),
    true
);
