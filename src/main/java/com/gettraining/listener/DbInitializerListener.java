package com.gettraining.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class DbInitializerListener implements ServletContextListener {

    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String APP_DB_URL = "jdbc:postgresql://localhost:5432/sdpfrequencia";
    private static final String USER = "postgres";
    private static final String PASS = "atac";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[DB Initializer] A iniciar a verificação da Base de Dados...");
        try {
            Class.forName("org.postgresql.Driver");

            // 1. Verificar se a base de dados 'sdpfrequencia' existe
            boolean dbExists = false;
            try (Connection conn = DriverManager.getConnection(DEFAULT_DB_URL, USER, PASS);
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
            try (Connection conn = DriverManager.getConnection(APP_DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement()) {

                System.out.println("[DB Initializer] A verificar/criar tabelas do sistema...");

                // Tabela Curso
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS curso (
                        id      SERIAL PRIMARY KEY,
                        nome    VARCHAR(150) NOT NULL,
                        horario VARCHAR(50)  NOT NULL
                    )
                """);

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

                // Tabela Administrador com suporte a níveis de permissão (papel)
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS administrador (
                        id       SERIAL PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        papel    VARCHAR(50) DEFAULT 'GESTOR'
                    )
                """);
                
                try {
                    stmt.executeUpdate("ALTER TABLE administrador ADD COLUMN IF NOT EXISTS papel VARCHAR(50) DEFAULT 'GESTOR'");
                } catch (Exception e) {
                    // Ignora erro se a sintaxe IF NOT EXISTS não for suportada em versões muito antigas do PostgreSQL
                }
                
                // Inserir Admin default (username: admin, password: admin -> hash SHA-256)
                // hash de 'admin' = 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
                stmt.executeUpdate("""
                    INSERT INTO administrador (username, password, papel)
                    SELECT 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'SUPER_ADMIN'
                    WHERE NOT EXISTS (SELECT 1 FROM administrador WHERE username = 'admin')
                """);
                
                stmt.executeUpdate("UPDATE administrador SET papel = 'SUPER_ADMIN' WHERE username = 'admin'");

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
