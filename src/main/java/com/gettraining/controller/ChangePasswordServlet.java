package com.gettraining.controller;

import com.gettraining.dao.AdminDAO;
import com.gettraining.dao.AuditDAO;
import com.gettraining.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin admin = (Admin) req.getSession().getAttribute("admin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("required", admin.isMustChangePassword());
        req.getRequestDispatcher("/WEB-INF/views/changePassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin admin = (Admin) req.getSession().getAttribute("admin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("continue".equals(action)) {
            try {
                AdminDAO dao = new AdminDAO();
                dao.marcarSenhaComoAlterada(admin.getId());
                admin.setMustChangePassword(false);
                req.getSession().setAttribute("admin", admin);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            } catch (Exception e) {
                req.setAttribute("mensagemErro", "Erro ao continuar com a senha atual: " + e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
                return;
            }
        }

        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            req.setAttribute("erro", "Por favor preencha ambos os campos de senha.");
            req.getRequestDispatcher("/WEB-INF/views/changePassword.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("erro", "As senhas não coincidem.");
            req.getRequestDispatcher("/WEB-INF/views/changePassword.jsp").forward(req, resp);
            return;
        }

        try {
            AdminDAO dao = new AdminDAO();
            dao.atualizarSenha(admin.getId(), newPassword, false);
            AuditDAO auditDAO = new AuditDAO();
            auditDAO.registrar(admin, "PASSWORD_CHANGED", "Senha atualizada pelo utilizador.", req.getRemoteAddr());

            admin.setMustChangePassword(false);
            req.getSession().setAttribute("admin", admin);
            req.setAttribute("sucesso", "Senha atualizada com sucesso.");
            req.getRequestDispatcher("/WEB-INF/views/changePassword.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao atualizar senha: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
