package com.gettraining.dao;

import com.gettraining.model.EntidadePagadora;
import java.sql.*;

/**
 * DAO - Operações sobre a tabela 'entidade_pagadora'
 */
public class EntidadeDAO {

    /**
     * Insere uma Entidade Pagadora na base de dados
     * @param e EntidadePagadora a inserir
     * @return ID gerado
     * @throws SQLException em caso de erro na BD
     */
    public int inserir(EntidadePagadora e) throws SQLException {
        try (Connection conn = ConexaoBD.getConexao()) {
            return inserir(conn, e);
        }
    }

    public int inserir(Connection conn, EntidadePagadora e) throws SQLException {
        String sql = """
            INSERT INTO entidade_pagadora
              (nome, morada, localidade, municipio, telefone, telemovel, fax, email, nif, formando_id)
            VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getMorada());
            ps.setString(3, e.getLocalidade());
            ps.setString(4, e.getMunicipio());
            ps.setString(5, e.getTelefone());
            ps.setString(6, e.getTelemovel());
            ps.setString(7, e.getFax());
            ps.setString(8, e.getEmail());
            ps.setString(9, e.getNif());
            ps.setInt(10, e.getFormandoId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Falha ao inserir entidade pagadora.");
    }

    /**
     * Busca EntidadePagadora pelo ID do Formando
     */
    public EntidadePagadora buscarPorFormandoId(int formandoId) throws SQLException {
        String sql = "SELECT * FROM entidade_pagadora WHERE formando_id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, formandoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private EntidadePagadora mapear(ResultSet rs) throws SQLException {
        EntidadePagadora e = new EntidadePagadora();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        e.setMorada(rs.getString("morada"));
        e.setLocalidade(rs.getString("localidade"));
        e.setMunicipio(rs.getString("municipio"));
        e.setTelefone(rs.getString("telefone"));
        e.setTelemovel(rs.getString("telemovel"));
        e.setFax(rs.getString("fax"));
        e.setEmail(rs.getString("email"));
        e.setNif(rs.getString("nif"));
        e.setFormandoId(rs.getInt("formando_id"));
        return e;
    }
}
