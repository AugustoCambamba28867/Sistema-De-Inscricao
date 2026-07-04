package com.gettraining.dao;

import com.gettraining.model.Admin;
import com.gettraining.util.HashUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public Admin autenticar(String username, String password) throws SQLException {
        String hashedPassword = HashUtil.hashSHA256(password);
        String sql = "SELECT id, username FROM administrador WHERE username = ? AND password = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("id"), rs.getString("username"));
                }
            }
        }
        return null;
    }
}
