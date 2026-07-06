package com.gettraining.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Controlador - Página Inicial
 * Mostra a landing page pública com acesso ao login.
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"/index"})
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/landing.jsp").forward(req, resp);
    }
}
