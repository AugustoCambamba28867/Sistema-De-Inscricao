package com.gettraining.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {

    public int getTotalInscricoes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricao";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getTotalCursos() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT nome) FROM curso";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getInscricoesHoje() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricao WHERE DATE(data_inscricao) = CURRENT_DATE";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getInscricoesEsteMes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricao WHERE EXTRACT(MONTH FROM data_inscricao) = EXTRACT(MONTH FROM NOW()) AND EXTRACT(YEAR FROM data_inscricao) = EXTRACT(YEAR FROM NOW())";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /** Retorna inscrições agrupadas por curso, ordenadas pela mais popular */
    public Map<String, Integer> getInscricoesPorCurso() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = """
            SELECT c.nome, COUNT(i.id) AS total
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            JOIN inscricao i ON f.id = i.formando_id
            GROUP BY c.nome
            ORDER BY total DESC
            """;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString(1), rs.getInt(2));
        }
        return map;
    }

    /** Retorna os Top N cursos mais procurados */
    public List<Map<String, Object>> getTopCursos(int topN) throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT c.nome, COUNT(i.id) AS total
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            JOIN inscricao i ON f.id = i.formando_id
            GROUP BY c.nome
            ORDER BY total DESC
            LIMIT ?
            """;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("curso", rs.getString("nome"));
                    row.put("total", rs.getInt("total"));
                    lista.add(row);
                }
            }
        }
        return lista;
    }

    /** Retorna inscrições por mês (últimos 6 meses) */
    public Map<String, Integer> getInscricoesPorMes() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = """
            SELECT TO_CHAR(data_inscricao, 'Mon/YY') AS mes, COUNT(*) AS total
            FROM inscricao
            WHERE data_inscricao >= NOW() - INTERVAL '6 months'
            GROUP BY TO_CHAR(data_inscricao, 'Mon/YY'), DATE_TRUNC('month', data_inscricao)
            ORDER BY DATE_TRUNC('month', data_inscricao)
            """;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("mes"), rs.getInt("total"));
        }
        return map;
    }

    /** Retorna inscrições por horário */
    public Map<String, Integer> getInscricoesPorHorario() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = """
            SELECT c.horario, COUNT(i.id) AS total
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            JOIN inscricao i ON f.id = i.formando_id
            GROUP BY c.horario
            ORDER BY total DESC
            """;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("horario"), rs.getInt("total"));
        }
        return map;
    }
}
