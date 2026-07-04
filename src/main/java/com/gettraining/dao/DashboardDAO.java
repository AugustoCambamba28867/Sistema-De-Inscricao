package com.gettraining.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO {

    public int getTotalInscricoes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricao";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public Map<String, Integer> getInscricoesPorCurso() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT c.nome, COUNT(i.id) FROM curso c " +
                     "JOIN formando f ON c.id = f.curso_id " +
                     "JOIN inscricao i ON f.id = i.formando_id " +
                     "GROUP BY c.nome";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        }
        return map;
    }
}
