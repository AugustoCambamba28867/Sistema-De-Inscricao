package com.gettraining.controller;

import com.gettraining.dao.AdminDAO;
import com.gettraining.dao.AuditDAO;
import com.gettraining.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GestaoUtilizadoresServlet", urlPatterns = {"/utilizadores"})
public class GestaoUtilizadoresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin loggedAdmin = (Admin) req.getSession().getAttribute("admin");
        if (loggedAdmin == null || !"SUPER_ADMIN".equals(loggedAdmin.getPapel())) {
            req.setAttribute("mensagemErro", "Acesso Negado. Apenas o Super Admin pode gerir utilizadores.");
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
            return;
        }

        try {
            AdminDAO dao = new AdminDAO();
            List<Admin> utilizadores = dao.listarTodos();
            req.setAttribute("utilizadores", utilizadores);
            req.getRequestDispatcher("/WEB-INF/views/utilizadores.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao carregar utilizadores: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin loggedAdmin = (Admin) req.getSession().getAttribute("admin");
        if (loggedAdmin == null || !"SUPER_ADMIN".equals(loggedAdmin.getPapel())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado.");
            return;
        }

        String action = req.getParameter("action");
        try {
            AdminDAO dao = new AdminDAO();
            AuditDAO auditDAO = new AuditDAO();
            if ("adicionar".equals(action)) {
                String username = req.getParameter("username");
                String password = req.getParameter("password");
                String papel = req.getParameter("papel");
                dao.inserir(username, password, papel);
                auditDAO.registrar(loggedAdmin, "USER_CREATE", "Criado utilizador '" + username + "' com papel='" + papel + "'.", req.getRemoteAddr());
                resp.sendRedirect(req.getContextPath() + "/utilizadores?sucesso=adicionado");
            } else if ("eliminar".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                if (id == loggedAdmin.getId()) {
                    throw new Exception("Não podes eliminar o teu próprio utilizador!");
                }
                dao.eliminar(id);
                auditDAO.registrar(loggedAdmin, "USER_DELETE", "Eliminado utilizador id='" + id + "'.", req.getRemoteAddr());
                resp.sendRedirect(req.getContextPath() + "/utilizadores?sucesso=eliminado");
            } else {
                resp.sendRedirect(req.getContextPath() + "/utilizadores");
            }
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao gerir utilizador: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
