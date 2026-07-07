package com.gettraining.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    private static final String CSRF_TOKEN_ATTRIBUTE = "csrfToken";
    private static final String CSRF_PARAMETER_NAME = "csrfToken";

    @Override
    public void init(FilterConfig filterConfig) {
        // nada a inicializar
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(CSRF_TOKEN_ATTRIBUTE);
        if (token == null || token.isBlank()) {
            token = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_ATTRIBUTE, token);
        }

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String requestToken = req.getParameter(CSRF_PARAMETER_NAME);
            if (requestToken == null || !requestToken.equals(token)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nada a destruir
    }
}
