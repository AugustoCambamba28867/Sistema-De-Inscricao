package com.gettraining.filter;

import com.gettraining.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/listagem", "/detalhe", "/deletar", "/dashboard", "/editar", "/utilizadores", "/relatorios", "/inscricao", "/dbadmin", "/change-password"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        boolean loggedIn = session != null && session.getAttribute("admin") != null;
        
        if (loggedIn) {
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin != null && admin.isMustChangePassword() && !"/change-password".equals(req.getServletPath())) {
                res.sendRedirect(req.getContextPath() + "/change-password?required=true");
                return;
            }
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
