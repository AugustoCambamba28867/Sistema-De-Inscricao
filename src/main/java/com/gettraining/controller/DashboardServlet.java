package com.gettraining.controller;

import com.gettraining.dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DashboardDAO dao = new DashboardDAO();
            req.setAttribute("totalInscricoes",   dao.getTotalInscricoes());
            req.setAttribute("totalCursos",        dao.getTotalCursos());
            req.setAttribute("inscricoesHoje",     dao.getInscricoesHoje());
            req.setAttribute("inscricoesEsteMes",  dao.getInscricoesEsteMes());
            req.setAttribute("inscricoesPorCurso", dao.getInscricoesPorCurso());
            req.setAttribute("inscricoesPorMes",   dao.getInscricoesPorMes());
            req.setAttribute("inscricoesPorHorario", dao.getInscricoesPorHorario());
            req.setAttribute("topCursos",          dao.getTopCursos(5));
            
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao carregar dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
