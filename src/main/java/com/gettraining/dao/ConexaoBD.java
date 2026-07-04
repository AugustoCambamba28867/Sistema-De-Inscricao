package com.gettraining.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DAO - Conexão à Base de Dados PostgreSQL
 * Centraliza as configurações de ligação
 */
public class ConexaoBD {

    private static final String URL      = "jdbc:postgresql://localhost:5432/sdpfrequencia"; // PostgreSQL 18
    private static final String USUARIO  = "postgres";
    private static final String SENHA    = "atac";

    // Carrega o driver JDBC do PostgreSQL
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
        return DriverManager.getConnection(URL, USUARIO, SENHA);
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
