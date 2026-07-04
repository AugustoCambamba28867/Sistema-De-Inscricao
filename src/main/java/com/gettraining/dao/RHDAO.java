package com.gettraining.dao;

import com.gettraining.model.ResponsavelRH;
import java.sql.*;

/**
 * DAO - Operações sobre a tabela 'responsavel_rh'
 */
public class RHDAO {

    /**
     * Insere um Responsável RH na base de dados
     * @param rh ResponsavelRH a inserir
     * @return ID gerado
     * @throws SQLException em caso de erro na BD
     */
    public int inserir(ResponsavelRH rh) throws SQLException {
        try (Connection conn = ConexaoBD.getConexao()) {
            return inserir(conn, rh);
        }
    }

    public int inserir(Connection conn, ResponsavelRH rh) throws SQLException {
        String sql = """
            INSERT INTO responsavel_rh (nome, telefone, telemovel, email, formando_id)
            VALUES (?,?,?,?,?) RETURNING id
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rh.getNome());
            ps.setString(2, rh.getTelefone());
            ps.setString(3, rh.getTelemovel());
            ps.setString(4, rh.getEmail());
            ps.setInt(5, rh.getFormandoId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Falha ao inserir responsável RH.");
    }

    /**
     * Busca ResponsavelRH pelo ID do Formando
     */
    public ResponsavelRH buscarPorFormandoId(int formandoId) throws SQLException {
        String sql = "SELECT * FROM responsavel_rh WHERE formando_id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, formandoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private ResponsavelRH mapear(ResultSet rs) throws SQLException {
        ResponsavelRH rh = new ResponsavelRH();
        rh.setId(rs.getInt("id"));
        rh.setNome(rs.getString("nome"));
        rh.setTelefone(rs.getString("telefone"));
        rh.setTelemovel(rs.getString("telemovel"));
        rh.setEmail(rs.getString("email"));
        rh.setFormandoId(rs.getInt("formando_id"));
        return rh;
    }
}
