package com.gettraining.dao;

import com.gettraining.model.Admin;
import com.gettraining.model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO {

    public void registrar(Admin admin, String action, String details, String ipAddress) throws SQLException {
        Integer adminId = null;
        String username = "SYSTEM";
        if (admin != null) {
            adminId = admin.getId();
            username = admin.getUsername();
        }
        registrar(adminId, username, action, details, ipAddress);
    }

    public void registrar(Integer adminId, String username, String action, String details, String ipAddress) throws SQLException {
        String sql = "INSERT INTO audit_log (admin_id, username, action, details, ip_address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (adminId != null) {
                ps.setInt(1, adminId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, username);
            ps.setString(3, action);
            ps.setString(4, details);
            ps.setString(5, ipAddress);
            ps.executeUpdate();
        }
    }

    public List<AuditLog> listarUltimos(int limite) throws SQLException {
        List<AuditLog> lista = new ArrayList<>();
        String sql = "SELECT id, admin_id, username, action, details, ip_address, created_at FROM audit_log ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog(
                            rs.getInt("id"),
                            rs.getObject("admin_id") != null ? rs.getInt("admin_id") : null,
                            rs.getString("username"),
                            rs.getString("action"),
                            rs.getString("details"),
                            rs.getString("ip_address"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    lista.add(log);
                }
            }
        }
        return lista;
    }
}
