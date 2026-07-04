package com.gettraining.dao;

import com.gettraining.model.Formando;
import java.sql.*;
import java.time.LocalDate;

/**
 * DAO - Operações sobre a tabela 'formando'
 */
public class FormandoDAO {

    /**
     * Insere um novo formando na base de dados
     * @param f Formando a inserir
     * @return ID gerado
     * @throws SQLException em caso de erro na BD
     */
    public int inserir(Formando f) throws SQLException {
        try (Connection conn = ConexaoBD.getConexao()) {
            return inserir(conn, f);
        }
    }

    public int inserir(Connection conn, Formando f) throws SQLException {
        String sql = """
            INSERT INTO formando
              (nome, morada, localidade, municipio, telefone, telemovel,
               email, data_nascimento, sexo, curso_id)
            VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getMorada());
            ps.setString(3, f.getLocalidade());
            ps.setString(4, f.getMunicipio());
            ps.setString(5, f.getTelefone());
            ps.setString(6, f.getTelemovel());
            ps.setString(7, f.getEmail());
            if (f.getDataNascimento() != null) {
                ps.setDate(8, Date.valueOf(f.getDataNascimento()));
            } else {
                ps.setNull(8, Types.DATE);
            }
            ps.setString(9, f.getSexo());
            ps.setInt(10, f.getCursoId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Falha ao inserir formando.");
    }

    /**
     * Busca um Formando pelo seu ID
     */
    public Formando buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM formando WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Formando mapear(ResultSet rs) throws SQLException {
        Formando f = new Formando();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setMorada(rs.getString("morada"));
        f.setLocalidade(rs.getString("localidade"));
        f.setMunicipio(rs.getString("municipio"));
        f.setTelefone(rs.getString("telefone"));
        f.setTelemovel(rs.getString("telemovel"));
        f.setEmail(rs.getString("email"));
        Date dn = rs.getDate("data_nascimento");
        if (dn != null) f.setDataNascimento(dn.toLocalDate());
        f.setSexo(rs.getString("sexo"));
        f.setCursoId(rs.getInt("curso_id"));
        return f;
    }
}
