package com.gettraining.dao;

import com.gettraining.model.Admin;
import com.gettraining.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin autenticar(String username, String password) throws SQLException {
        String sql = "SELECT id, username, papel, password, must_change_password FROM administrador WHERE username = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String storedHash = rs.getString("password");
                    boolean mustChangePassword = rs.getBoolean("must_change_password");
                    if (PasswordUtil.verifyPassword(password, storedHash)) {
                        if (PasswordUtil.isLegacyHash(storedHash)) {
                            atualizarSenha(id, password, mustChangePassword);
                        }
                        return new Admin(id, rs.getString("username"), rs.getString("papel"), mustChangePassword);
                    }
                }
            }
        }
        return null;
    }

    public void inserir(String username, String password, String papel) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(password);
        String sql = "INSERT INTO administrador (username, password, papel, must_change_password) VALUES (?, ?, ?, false)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, papel);
            ps.executeUpdate();
        }
    }

    public void atualizarSenha(int id, String novaSenha, boolean mustChangePassword) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(novaSenha);
        String sql = "UPDATE administrador SET password = ?, must_change_password = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setBoolean(2, mustChangePassword);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void marcarSenhaComoAlterada(int id) throws SQLException {
        String sql = "UPDATE administrador SET must_change_password = false WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Admin> listarTodos() throws SQLException {
        List<Admin> lista = new ArrayList<>();
        String sql = "SELECT id, username, papel FROM administrador ORDER BY username";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("papel")));
            }
        }
        return lista;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM administrador WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
