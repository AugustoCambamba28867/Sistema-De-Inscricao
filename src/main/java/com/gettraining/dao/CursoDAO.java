package com.gettraining.dao;

import com.gettraining.model.Curso;
import java.sql.*;

/**
 * DAO - Operações sobre a tabela 'curso'
 */
public class CursoDAO {

    /**
     * Insere um novo curso na base de dados
     * @param curso Objeto Curso a inserir
     * @return id gerado pelo PostgreSQL (SERIAL)
     * @throws SQLException em caso de erro na BD
     */
    public int inserir(Curso curso) throws SQLException {
        try (Connection conn = ConexaoBD.getConexao()) {
            return inserir(conn, curso);
        }
    }

    public int inserir(Connection conn, Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nome, horario) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getHorario());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Falha ao inserir curso, nenhum ID retornado.");
    }

    /**
     * Busca um Curso pelo seu ID
     * @param id identificador do curso
     * @return Curso encontrado ou null
     * @throws SQLException em caso de erro na BD
     */
    public Curso buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM curso WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    /**
     * Mapeia um ResultSet para um objeto Curso
     */
    private Curso mapear(ResultSet rs) throws SQLException {
        Curso c = new Curso();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setHorario(rs.getString("horario"));
        return c;
    }
}
