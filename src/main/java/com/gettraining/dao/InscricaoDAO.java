package com.gettraining.dao;

import com.gettraining.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Operações sobre a tabela 'inscricao'
 * Coordena a persistência de todos os dados relacionados
 */
public class InscricaoDAO {

    /**
     * Grava uma Inscrição completa na BD (transação única)
     * @param inscricao Objecto completo com todos os dados
     * @return ID da inscrição gravada
     * @throws SQLException em caso de erro na BD
     */
    public int gravar(Inscricao inscricao) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexaoBD.getConexao();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Gravar Curso
            CursoDAO cursoDAO = new CursoDAO();
            int cursoId = cursoDAO.inserir(conn, inscricao.getCurso());
            inscricao.getFormando().setCursoId(cursoId);

            // 2. Gravar Formando
            FormandoDAO formandoDAO = new FormandoDAO();
            int formandoId = formandoDAO.inserir(conn, inscricao.getFormando());

            // 3. Gravar Entidade Pagadora
            if (inscricao.getEntidade() != null && inscricao.getEntidade().getNome() != null
                    && !inscricao.getEntidade().getNome().isBlank()) {
                inscricao.getEntidade().setFormandoId(formandoId);
                EntidadeDAO entidadeDAO = new EntidadeDAO();
                entidadeDAO.inserir(conn, inscricao.getEntidade());
            }

            // 4. Gravar Responsável RH
            if (inscricao.getResponsavelRH() != null && inscricao.getResponsavelRH().getNome() != null
                    && !inscricao.getResponsavelRH().getNome().isBlank()) {
                inscricao.getResponsavelRH().setFormandoId(formandoId);
                RHDAO rhDAO = new RHDAO();
                rhDAO.inserir(conn, inscricao.getResponsavelRH());
            }

            // 5. Gravar registo da Inscrição
            String sql = "INSERT INTO inscricao (formando_id) VALUES (?) RETURNING id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, formandoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        conn.commit(); // Confirmar transação
                        return rs.getInt("id");
                    }
                }
            }

            conn.rollback();
            throw new SQLException("Falha ao criar registo de inscrição.");

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Lista todas as inscrições (com dados do formando e curso)
     * @return Lista de Inscrições
     * @throws SQLException em caso de erro na BD
     */
    public List<Inscricao> listarTodas() throws SQLException {
        String sql = """
            SELECT i.id AS ins_id, i.data_inscricao,
                   f.id AS f_id, f.nome AS f_nome, f.email,
                   c.nome AS c_nome, c.horario
            FROM inscricao i
            JOIN formando f ON i.formando_id = f.id
            JOIN curso c ON f.curso_id = c.id
            ORDER BY i.data_inscricao DESC
            """;
        List<Inscricao> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Inscricao ins = new Inscricao();
                ins.setId(rs.getInt("ins_id"));
                ins.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());

                Formando f = new Formando();
                f.setId(rs.getInt("f_id"));
                f.setNome(rs.getString("f_nome"));
                f.setEmail(rs.getString("email"));
                ins.setFormando(f);

                Curso c = new Curso();
                c.setNome(rs.getString("c_nome"));
                c.setHorario(rs.getString("horario"));
                ins.setCurso(c);

                lista.add(ins);
            }
        }
        return lista;
    }

    /**
     * Busca uma Inscrição completa pelo ID
     * @param id ID da inscrição
     * @return Inscrição completa com todos os dados associados
     * @throws SQLException em caso de erro na BD
     */
    public Inscricao buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT i.id AS ins_id, i.data_inscricao, i.formando_id
            FROM inscricao i WHERE i.id = ?
            """;
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int formandoId = rs.getInt("formando_id");
                    Inscricao ins = new Inscricao();
                    ins.setId(rs.getInt("ins_id"));
                    ins.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());

                    FormandoDAO fdao = new FormandoDAO();
                    Formando f = fdao.buscarPorId(formandoId);
                    ins.setFormando(f);

                    if (f != null) {
                        CursoDAO cdao = new CursoDAO();
                        ins.setCurso(cdao.buscarPorId(f.getCursoId()));

                        EntidadeDAO edao = new EntidadeDAO();
                        ins.setEntidade(edao.buscarPorFormandoId(formandoId));

                        RHDAO rhdao = new RHDAO();
                        ins.setResponsavelRH(rhdao.buscarPorFormandoId(formandoId));
                    }
                    return ins;
                }
            }
        }
        return null;
    }

    /**
     * Elimina uma inscrição e todos os dados associados
     * @param id ID da inscrição
     * @throws SQLException em caso de erro na BD
     */
    public void eliminar(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexaoBD.getConexao();
            conn.setAutoCommit(false);

            // Obter formando_id antes de eliminar
            int formandoId = -1;
            String getFormando = "SELECT formando_id FROM inscricao WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getFormando)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) formandoId = rs.getInt("formando_id");
                }
            }
            if (formandoId == -1) throw new SQLException("Inscrição não encontrada: " + id);

            // Eliminar em cascata
            executar(conn, "DELETE FROM inscricao WHERE id = ?", id);
            executar(conn, "DELETE FROM responsavel_rh WHERE formando_id = ?", formandoId);
            executar(conn, "DELETE FROM entidade_pagadora WHERE formando_id = ?", formandoId);

            // Obter curso_id antes de eliminar formando
            int cursoId = -1;
            String getCurso = "SELECT curso_id FROM formando WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getCurso)) {
                ps.setInt(1, formandoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) cursoId = rs.getInt("curso_id");
                }
            }
            executar(conn, "DELETE FROM formando WHERE id = ?", formandoId);
            if (cursoId > 0) executar(conn, "DELETE FROM curso WHERE id = ?", cursoId);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }

    private void executar(Connection conn, String sql, int param) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            ps.executeUpdate();
        }
    }
}
