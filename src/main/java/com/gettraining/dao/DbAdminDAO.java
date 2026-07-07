package com.gettraining.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DbAdminDAO {

    public String gerarBackup() throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("-- Backup de dados do sistema sdpfrequencia\n");
        sql.append("-- Gerado em ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        sql.append("SET client_encoding = 'UTF8';\n");
        sql.append("SET standard_conforming_strings = on;\n\n");

        appendTableBackup(sql, "curso");
        appendTableBackup(sql, "formando");
        appendTableBackup(sql, "entidade_pagadora");
        appendTableBackup(sql, "responsavel_rh");
        appendTableBackup(sql, "inscricao");
        appendTableBackup(sql, "administrador");
        appendTableBackup(sql, "audit_log");

        return sql.toString();
    }

    private void appendTableBackup(StringBuilder sql, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + " ORDER BY id";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            boolean firstRow = true;
            while (rs.next()) {
                if (firstRow) {
                    sql.append("\n-- Dados da tabela ").append(tableName).append("\n");
                    firstRow = false;
                }
                sql.append("INSERT INTO ").append(tableName).append(" (");
                for (int i = 1; i <= columnCount; i++) {
                    sql.append(meta.getColumnName(i));
                    if (i < columnCount) sql.append(", ");
                }
                sql.append(") VALUES (");
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    sql.append(formatValue(value, meta.getColumnType(i)));
                    if (i < columnCount) sql.append(", ");
                }
                sql.append(");\n");
            }
        }
    }

    private String formatValue(Object value, int sqlType) {
        if (value == null) {
            return "NULL";
        }
        switch (sqlType) {
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.BIT:
            case Types.BOOLEAN:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.REAL:
                return value.toString();
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return "'" + escapeSql(value.toString()) + "'";
            default:
                return "'" + escapeSql(value.toString()) + "'";
        }
    }

    private String escapeSql(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("'", "''").replace("\\", "\\\\");
    }

    public void resetDatabase() throws SQLException {
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);

            // Limpar apenas os dados de aplicação; preserva utilizadores e histórico de auditoria
            stmt.executeUpdate("DELETE FROM entidade_pagadora");
            stmt.executeUpdate("DELETE FROM responsavel_rh");
            stmt.executeUpdate("DELETE FROM inscricao");
            stmt.executeUpdate("DELETE FROM formando");
            stmt.executeUpdate("DELETE FROM curso");

            conn.commit();
        }
    }
}
