package com.gettraining.dao;

import com.gettraining.model.Admin;
import com.gettraining.util.HashUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin autenticar(String username, String password) throws SQLException {
        String hashedPassword = HashUtil.hashSHA256(password);
        String sql = "SELECT id, username, papel FROM administrador WHERE username = ? AND password = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("papel"));
                }
            }
        }
        return null;
    }

    public void inserir(String username, String password, String papel) throws SQLException {
        String hashedPassword = HashUtil.hashSHA256(password);
        String sql = "INSERT INTO administrador (username, password, papel) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, papel);
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
