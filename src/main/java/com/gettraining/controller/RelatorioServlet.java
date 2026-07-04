package com.gettraining.controller;

import com.gettraining.dao.RelatorioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RelatorioServlet", urlPatterns = {"/relatorios"})
public class RelatorioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RelatorioDAO dao = new RelatorioDAO();

            // KPIs gerais
            req.setAttribute("totalEstudantes", dao.getTotalEstudantes());
            req.setAttribute("totalMasculino",  dao.getTotalMasculino());
            req.setAttribute("totalFeminino",    dao.getTotalFeminino());

            // Inscritos por curso com divisão de género (tabela resumo)
            req.setAttribute("inscritosPorCurso", dao.getInscritosPorCurso());

            // Lista de cursos para o dropdown de filtro
            req.setAttribute("cursosDisponiveis", dao.getCursosDisponiveis());

            // ─── Parâmetros do filtro dinâmico ───
            String tipo   = req.getParameter("tipo");
            String sexo   = req.getParameter("sexo");
            String curso  = req.getParameter("cursoFiltro");
            String topNp  = req.getParameter("topN");
            int topN = 5; // default
            try { topN = Integer.parseInt(topNp); if (topN < 1) topN = 5; }
            catch (Exception ignored) {}

            req.setAttribute("tipoFiltro",   tipo   != null ? tipo   : "");
            req.setAttribute("sexoFiltro",   sexo   != null ? sexo   : "TODOS");
            req.setAttribute("cursoFiltro2", curso  != null ? curso  : "");
            req.setAttribute("topNFiltro",   topN);

            // ─── Executar a query correta conforme o tipo ───
            if ("TOP_CURSOS".equals(tipo)) {
                req.setAttribute("resultado", dao.getTopCursosPorInscricoes(topN));
                req.setAttribute("tipoResultado", "cursos");
                req.setAttribute("tituloResultado", "Top " + topN + " Cursos com mais inscrições");

            } else if ("TOP_CURSOS_GENERO".equals(tipo)) {
                String s = (sexo == null || sexo.isBlank() || "TODOS".equals(sexo)) ? "M" : sexo;
                req.setAttribute("resultado", dao.getTopCursosPorGenero(topN, s));
                req.setAttribute("tipoResultado", "cursos");
                req.setAttribute("tituloResultado", "Top " + topN + " Cursos – " + ("M".equals(s) ? "Masculinos" : "Femininas"));

            } else if ("TOP_ESTUDANTES_GENERO".equals(tipo)) {
                String s = (sexo == null || sexo.isBlank() || "TODOS".equals(sexo)) ? "M" : sexo;
                req.setAttribute("resultado", dao.getTopEstudantesPorGenero(topN, s));
                req.setAttribute("tipoResultado", "estudantes");
                req.setAttribute("tituloResultado", "Top " + topN + " Estudantes " + ("M".equals(s) ? "Masculinos" : "Femininas"));

            } else if ("ESTUDANTES_CURSO_GENERO".equals(tipo)) {
                req.setAttribute("resultado", dao.getEstudantesPorCursoEGenero(topN, curso, sexo));
                req.setAttribute("tipoResultado", "estudantes");
                String descSexo = "TODOS".equals(sexo) || sexo == null ? "Todos" : ("M".equals(sexo) ? "Masculinos" : "Femininas");
                String descCurso = (curso == null || curso.isBlank()) ? "Todos os Cursos" : curso;
                req.setAttribute("tituloResultado", "Top " + topN + " Estudantes (" + descSexo + ") – " + descCurso);
            }

            req.getRequestDispatcher("/WEB-INF/views/relatorios.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro no relatório: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
