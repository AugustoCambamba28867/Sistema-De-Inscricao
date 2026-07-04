package com.gettraining.dao;

import java.sql.*;
import java.util.*;

/**
 * DAO dedicado a Relatórios dinâmicos e estatísticas avançadas.
 * Suporta filtros por: género, curso, top-N, e combinações.
 */
public class RelatorioDAO {

    // ─── Totais gerais ───────────────────────────────────────────────
    public int getTotalEstudantes() throws SQLException {
        return contar("SELECT COUNT(*) FROM formando");
    }

    public int getTotalMasculino() throws SQLException {
        return contar("SELECT COUNT(*) FROM formando WHERE sexo = 'M'");
    }

    public int getTotalFeminino() throws SQLException {
        return contar("SELECT COUNT(*) FROM formando WHERE sexo = 'F'");
    }

    // ─── Top N Cursos por Nº de Inscrições ───────────────────────────
    public List<Map<String, Object>> getTopCursosPorInscricoes(int topN) throws SQLException {
        String sql = """
            SELECT c.nome AS curso, COUNT(i.id) AS total
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            JOIN inscricao i ON f.id = i.formando_id
            GROUP BY c.nome
            ORDER BY total DESC
            LIMIT ?
            """;
        return executarComTopN(sql, topN);
    }

    // ─── Top N Cursos por Género ──────────────────────────────────────
    public List<Map<String, Object>> getTopCursosPorGenero(int topN, String sexo) throws SQLException {
        String sql = """
            SELECT c.nome AS curso, COUNT(f.id) AS total
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            WHERE f.sexo = ?
            GROUP BY c.nome
            ORDER BY total DESC
            LIMIT ?
            """;
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sexo.toUpperCase());
            ps.setInt(2, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("curso", rs.getString("curso"));
                    row.put("total", rs.getInt("total"));
                    lista.add(row);
                }
            }
        }
        return lista;
    }

    // ─── Top N Estudantes por género (globalmente) ────────────────────
    public List<Map<String, Object>> getTopEstudantesPorGenero(int topN, String sexo) throws SQLException {
        String sql = """
            SELECT f.nome AS estudante, c.nome AS curso, f.email
            FROM formando f
            JOIN curso c ON c.id = f.curso_id
            WHERE f.sexo = ?
            ORDER BY f.nome
            LIMIT ?
            """;
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sexo.toUpperCase());
            ps.setInt(2, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("estudante", rs.getString("estudante"));
                    row.put("curso", rs.getString("curso"));
                    row.put("email", rs.getString("email"));
                    lista.add(row);
                }
            }
        }
        return lista;
    }

    // ─── Top N Estudantes por Curso e Género ─────────────────────────
    public List<Map<String, Object>> getEstudantesPorCursoEGenero(int topN, String curso, String sexo) throws SQLException {
        String sqlBase = """
            SELECT f.nome AS estudante, c.nome AS curso, f.email, f.sexo
            FROM formando f
            JOIN curso c ON c.id = f.curso_id
            WHERE 1=1
            """;

        List<Object> params = new ArrayList<>();
        if (curso != null && !curso.isBlank()) {
            sqlBase += " AND LOWER(c.nome) LIKE LOWER(?)";
            params.add("%" + curso + "%");
        }
        if (sexo != null && !sexo.isBlank() && !sexo.equals("TODOS")) {
            sqlBase += " AND f.sexo = ?";
            params.add(sexo.toUpperCase());
        }
        sqlBase += " ORDER BY f.nome LIMIT ?";
        params.add(topN);

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sqlBase)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("estudante", rs.getString("estudante"));
                    row.put("curso",     rs.getString("curso"));
                    row.put("email",     rs.getString("email"));
                    row.put("sexo",      rs.getString("sexo"));
                    lista.add(row);
                }
            }
        }
        return lista;
    }

    // ─── Nº de inscritos por curso (todos) ────────────────────────────
    public List<Map<String, Object>> getInscritosPorCurso() throws SQLException {
        String sql = """
            SELECT c.nome AS curso,
                   COUNT(f.id) AS total,
                   SUM(CASE WHEN f.sexo='M' THEN 1 ELSE 0 END) AS masculino,
                   SUM(CASE WHEN f.sexo='F' THEN 1 ELSE 0 END) AS feminino
            FROM curso c
            JOIN formando f ON c.id = f.curso_id
            GROUP BY c.nome
            ORDER BY total DESC
            """;
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("curso",     rs.getString("curso"));
                row.put("total",     rs.getInt("total"));
                row.put("masculino", rs.getInt("masculino"));
                row.put("feminino",  rs.getInt("feminino"));
                lista.add(row);
            }
        }
        return lista;
    }

    // ─── Lista de todos os cursos (para o dropdown) ───────────────────
    public List<String> getCursosDisponiveis() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT nome FROM curso ORDER BY nome";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rs.getString("nome"));
        }
        return lista;
    }

    // ─── Helper ──────────────────────────────────────────────────────
    private int contar(String sql) throws SQLException {
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<Map<String, Object>> executarComTopN(String sql, int topN) throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, topN);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++)
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    lista.add(row);
                }
            }
        }
        return lista;
    }
}
