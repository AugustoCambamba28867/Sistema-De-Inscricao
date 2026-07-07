package com.gettraining.controller;

import com.gettraining.dao.AdminDAO;
import com.gettraining.dao.AuditDAO;
import com.gettraining.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("admin") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        try {
            AdminDAO dao = new AdminDAO();
            Admin admin = dao.autenticar(username, password);
            
            AuditDAO auditDAO = new AuditDAO();
            if (admin != null) {
                auditDAO.registrar(admin, "LOGIN_SUCCESS", "Login bem-sucedido.", req.getRemoteAddr());

                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = req.getSession(true);
                session.setMaxInactiveInterval(20 * 60); // 20 minutos
                session.setAttribute("admin", admin);

                if (req.isSecure()) {
                    Cookie[] cookies = req.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if ("JSESSIONID".equals(cookie.getName())) {
                                cookie.setHttpOnly(true);
                                cookie.setSecure(true);
                                cookie.setPath(req.getContextPath());
                                resp.addCookie(cookie);
                            }
                        }
                    }
                }

                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                auditDAO.registrar(null, username != null ? username : "UNKNOWN", "LOGIN_FAILED", "Tentativa de login inválida.", req.getRemoteAddr());
                req.setAttribute("erro", "Credenciais inválidas.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao autenticar: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
