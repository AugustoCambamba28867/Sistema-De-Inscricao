package com.gettraining.listener;

import com.gettraining.util.DbConfig;
import com.gettraining.util.PasswordUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class DbInitializerListener implements ServletContextListener {

    private static final String DEFAULT_DB_URL = DbConfig.DB_SUPERUSER_URL;
    private static final String DEFAULT_DB_USER = DbConfig.DB_SUPERUSER_USER;
    private static final String DEFAULT_DB_PASSWORD = DbConfig.DB_SUPERUSER_PASSWORD;
    private static final String APP_DB_URL = DbConfig.DB_URL;
    private static final String APP_DB_USER = DbConfig.DB_USER;
    private static final String APP_DB_PASSWORD = DbConfig.DB_PASSWORD;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[DB Initializer] A iniciar a verificação da Base de Dados...");
        try {
            Class.forName("org.postgresql.Driver");

            // 1. Verificar se a base de dados 'sdpfrequencia' existe
            boolean dbExists = false;
            try (Connection conn = DriverManager.getConnection(DEFAULT_DB_URL, DEFAULT_DB_USER, DEFAULT_DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                String checkDbSql = "SELECT 1 FROM pg_database WHERE datname = 'sdpfrequencia'";
                try (ResultSet rs = stmt.executeQuery(checkDbSql)) {
                    if (rs.next()) {
                        dbExists = true;
                    }
                }

                // 2. Se não existir, criar
                if (!dbExists) {
                    System.out.println("[DB Initializer] A criar a Base de Dados 'sdpfrequencia'...");
                    stmt.executeUpdate("CREATE DATABASE sdpfrequencia");
                    System.out.println("[DB Initializer] Base de dados criada com sucesso.");
                } else {
                    System.out.println("[DB Initializer] A Base de Dados 'sdpfrequencia' já existe.");
                }
            }

            // 3. Conectar à base de dados da aplicação para criar as tabelas
            try (Connection conn = DriverManager.getConnection(APP_DB_URL, APP_DB_USER, APP_DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {

                System.out.println("[DB Initializer] A verificar/criar tabelas do sistema...");

                // Tabela Curso
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS curso (
                        id         SERIAL PRIMARY KEY,
                        nome       VARCHAR(150) NOT NULL,
                        periodo    DATE,
                        hora_inicio VARCHAR(5),
                        hora_fim   VARCHAR(5),
                        duracao    VARCHAR(50)
                    )
                """);

                // Ajustar esquema antigo caso a tabela já exista com coluna 'horario'
                try {
                    stmt.executeUpdate("ALTER TABLE curso ADD COLUMN IF NOT EXISTS periodo DATE");
                    stmt.executeUpdate("ALTER TABLE curso ADD COLUMN IF NOT EXISTS hora_inicio VARCHAR(5)");
                    stmt.executeUpdate("ALTER TABLE curso ADD COLUMN IF NOT EXISTS hora_fim VARCHAR(5)");
                    stmt.executeUpdate("ALTER TABLE curso ADD COLUMN IF NOT EXISTS duracao VARCHAR(50)");
                    stmt.executeUpdate("ALTER TABLE curso DROP COLUMN IF EXISTS horario");
                } catch (Exception e) {
                    // Ignora falhas de migração em versões de PostgreSQL que não suportem IF NOT EXISTS,
                    // mas mantém a estrutura de aplicação consistente quando possível.
                }

                // Tabela Formando
                stmt.executeUpdate("""
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
                        sexo             CHAR(1),
                        curso_id         INT REFERENCES curso(id) ON DELETE SET NULL
                    )
                """);

                // Tabela Entidade Pagadora
                stmt.executeUpdate("""
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
                    )
                """);

                // Tabela Responsável RH
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS responsavel_rh (
                        id          SERIAL PRIMARY KEY,
                        nome        VARCHAR(150) NOT NULL,
                        telefone    VARCHAR(20),
                        telemovel   VARCHAR(20),
                        email       VARCHAR(100),
                        formando_id INT REFERENCES formando(id) ON DELETE CASCADE
                    )
                """);

                // Tabela Inscrição
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS inscricao (
                        id              SERIAL PRIMARY KEY,
                        formando_id     INT REFERENCES formando(id) ON DELETE CASCADE,
                        data_inscricao  TIMESTAMP DEFAULT NOW()
                    )
                """);

                // Tabela Administrador com suporte a níveis de permissão (papel) e alteração obrigatória de senha
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS administrador (
                        id                  SERIAL PRIMARY KEY,
                        username            VARCHAR(50) UNIQUE NOT NULL,
                        password            VARCHAR(255) NOT NULL,
                        papel               VARCHAR(50) DEFAULT 'GESTOR',
                        must_change_password BOOLEAN DEFAULT FALSE
                    )
                """);

                // Tabela de auditoria de ações administrativas
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS audit_log (
                        id         SERIAL PRIMARY KEY,
                        admin_id   INT REFERENCES administrador(id) ON DELETE SET NULL,
                        username   VARCHAR(50),
                        action     VARCHAR(100) NOT NULL,
                        details    TEXT,
                        ip_address VARCHAR(50),
                        created_at TIMESTAMP DEFAULT NOW()
                    )
                """);
                
                try {
                    stmt.executeUpdate("ALTER TABLE administrador ADD COLUMN IF NOT EXISTS papel VARCHAR(50) DEFAULT 'GESTOR'");
                } catch (Exception e) {
                    // Ignora erro se a sintaxe IF NOT EXISTS não for suportada em versões muito antigas do PostgreSQL
                }

                try {
                    stmt.executeUpdate("ALTER TABLE administrador ADD COLUMN IF NOT EXISTS must_change_password BOOLEAN DEFAULT FALSE");
                } catch (Exception e) {
                    // Ignora erro se a sintaxe IF NOT EXISTS não for suportada em versões muito antigas do PostgreSQL
                }
                
                try {
                    stmt.executeUpdate("ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS username VARCHAR(50)");
                } catch (Exception ignored) {
                    // Não faz mal se a coluna já existir ou se a versão não suportar ALTER TABLE ... ADD COLUMN IF NOT EXISTS
                }
                
                if (!DbConfig.ADMIN_PASSWORD.isBlank()) {
                    String defaultAdminHash = PasswordUtil.hashPassword(DbConfig.ADMIN_PASSWORD);
                    try (PreparedStatement ps = conn.prepareStatement("""
                        INSERT INTO administrador (username, password, papel, must_change_password)
                        SELECT ?, ?, 'SUPER_ADMIN', true
                        WHERE NOT EXISTS (SELECT 1 FROM administrador WHERE username = ?)
                    """)) {
                        ps.setString(1, DbConfig.ADMIN_USERNAME);
                        ps.setString(2, defaultAdminHash);
                        ps.setString(3, DbConfig.ADMIN_USERNAME);
                        ps.executeUpdate();
                    }
                } else {
                    System.out.println("[DB Initializer] Nenhum administrador predefinido criado porque DB_ADMIN_PASSWORD não está definido.");
                }
                
                try (PreparedStatement ps = conn.prepareStatement("UPDATE administrador SET papel = 'SUPER_ADMIN' WHERE username = ?")) {
                    ps.setString(1, DbConfig.ADMIN_USERNAME);
                    ps.executeUpdate();
                }

                System.out.println("[DB Initializer] Estrutura de Tabelas verificada com sucesso!");
            }

        } catch (Exception e) {
            System.err.println("[DB Initializer] FALHA AO INICIALIZAR A BASE DE DADOS:");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nada a fazer ao desligar
    }
}
