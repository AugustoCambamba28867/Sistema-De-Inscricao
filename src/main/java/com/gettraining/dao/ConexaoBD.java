package com.gettraining.dao;

import com.gettraining.util.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DAO - Conexão à Base de Dados PostgreSQL
 * Centraliza as configurações de ligação
 */
public class ConexaoBD {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL não encontrado!", e);
        }
    }

    /**
     * Obtém uma ligação à base de dados
     * @return Connection activa
     * @throws SQLException se a ligação falhar
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(DbConfig.DB_URL, DbConfig.DB_USER, DbConfig.DB_PASSWORD);
    }

    /**
     * Fecha a ligação de forma segura
     * @param conn Connection a fechar
     */
    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
