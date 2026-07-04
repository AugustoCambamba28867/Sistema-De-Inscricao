-- ============================================================
-- GET Training Academy – Script de criação da Base de Dados
-- Base de Dados: sdpfrequencia
-- User: postgres | Senha: atac
-- ============================================================

-- Criar a base de dados (executar ligado ao postgres padrão)
-- CREATE DATABASE sdpfrequencia;

-- Conectar à base de dados sdpfrequencia antes de executar o resto
-- \c sdpfrequencia

-- ---- Tabela: curso ----------------------------------------
CREATE TABLE IF NOT EXISTS curso (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(150) NOT NULL,
    horario VARCHAR(50)  NOT NULL  -- Manhã, Tarde, Fim de Tarde, Sábado
);

-- ---- Tabela: formando -------------------------------------
CREATE TABLE IF NOT EXISTS formando (
    id               SERIAL PRIMARY KEY,
    nome             VARCHAR(150) NOT NULL,
    morada           VARCHAR(200),
    localidade       VARCHAR(100),
    municipio        VARCHAR(100),
    telefone         VARCHAR(20),
    telemovel        VARCHAR(20),
    email            VARCHAR(100) NOT NULL,
    data_nascimento  DATE,
    sexo             CHAR(1),          -- 'M' ou 'F'
    curso_id         INT REFERENCES curso(id) ON DELETE SET NULL
);

-- ---- Tabela: entidade_pagadora ----------------------------
CREATE TABLE IF NOT EXISTS entidade_pagadora (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    morada      VARCHAR(200),
    localidade  VARCHAR(100),
    municipio   VARCHAR(100),
    telefone    VARCHAR(20),
    telemovel   VARCHAR(20),
    fax         VARCHAR(20),
    email       VARCHAR(100),
    nif         VARCHAR(20),
    formando_id INT REFERENCES formando(id) ON DELETE CASCADE
);

-- ---- Tabela: responsavel_rh -------------------------------
CREATE TABLE IF NOT EXISTS responsavel_rh (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    telefone    VARCHAR(20),
    telemovel   VARCHAR(20),
    email       VARCHAR(100),
    formando_id INT REFERENCES formando(id) ON DELETE CASCADE
);

-- ---- Tabela: inscricao ------------------------------------
CREATE TABLE IF NOT EXISTS inscricao (
    id              SERIAL PRIMARY KEY,
    formando_id     INT REFERENCES formando(id) ON DELETE CASCADE,
    data_inscricao  TIMESTAMP DEFAULT NOW()
);

-- ---- Verificação ------------------------------------------
SELECT 'Tabelas criadas com sucesso!' AS status;
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;
