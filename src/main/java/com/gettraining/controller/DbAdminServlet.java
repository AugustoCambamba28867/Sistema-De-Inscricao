package com.gettraining.controller;

import com.gettraining.dao.AuditDAO;
import com.gettraining.dao.DbAdminDAO;
import com.gettraining.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "DbAdminServlet", urlPatterns = {"/dbadmin"})
public class DbAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin loggedAdmin = (Admin) req.getSession().getAttribute("admin");
        if (loggedAdmin == null || !"SUPER_ADMIN".equals(loggedAdmin.getPapel())) {
            req.setAttribute("mensagemErro", "Acesso Negado. Apenas o Super Admin pode aceder a esta secção.");
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
            return;
        }

        try {
            AuditDAO auditDAO = new AuditDAO();
            List<com.gettraining.model.AuditLog> logs = auditDAO.listarUltimos(50);
            req.setAttribute("auditLogs", logs);
            req.getRequestDispatcher("/WEB-INF/views/dbadmin.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao carregar administração da base de dados: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Admin loggedAdmin = (Admin) req.getSession().getAttribute("admin");
        if (loggedAdmin == null || !"SUPER_ADMIN".equals(loggedAdmin.getPapel())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado.");
            return;
        }

        String action = req.getParameter("action");
        String ipAddress = req.getRemoteAddr();

        try {
            if ("backup".equals(action)) {
                DbAdminDAO dao = new DbAdminDAO();
                String backupSql = dao.gerarBackup();

                AuditDAO auditDAO = new AuditDAO();
                auditDAO.registrar(loggedAdmin, "DB_BACKUP", "Backup gerado no painel de administração.", ipAddress);

                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
                resp.setContentType("application/sql;charset=UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename=sdpfrequencia_backup_" + now + ".sql");
                resp.getWriter().write(backupSql);
                return;
            } else if ("reset".equals(action)) {
                String confirmation = req.getParameter("confirmacao");
                if (confirmation == null || !"RESETAR".equalsIgnoreCase(confirmation.trim())) {
                    req.setAttribute("mensagemErro", "A confirmação de reset está incorreta. Digite RESETAR para prosseguir.");
                    doGet(req, resp);
                    return;
                }

                DbAdminDAO dao = new DbAdminDAO();
                dao.resetDatabase();

                AuditDAO auditDAO = new AuditDAO();
                auditDAO.registrar(loggedAdmin, "DB_RESET", "Base de dados reiniciada. Dados de aplicação foram limpos; administradores e auditoria preservados.", ipAddress);

                resp.sendRedirect(req.getContextPath() + "/dbadmin?reset=ok");
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/dbadmin");
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao executar operação de base de dados: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
