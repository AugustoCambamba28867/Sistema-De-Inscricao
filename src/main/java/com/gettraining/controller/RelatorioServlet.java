package com.gettraining.controller;

import com.gettraining.dao.RelatorioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RelatorioServlet", urlPatterns = {"/relatorios"})
public class RelatorioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RelatorioDAO dao = new RelatorioDAO();

            req.setAttribute("totalEstudantes", dao.getTotalEstudantes());
            req.setAttribute("totalMasculino", dao.getTotalMasculino());
            req.setAttribute("totalFeminino", dao.getTotalFeminino());
            req.setAttribute("inscritosPorCurso", dao.getInscritosPorCurso());
            req.setAttribute("cursosDisponiveis", dao.getCursosDisponiveis());

            String tipo = req.getParameter("tipo");
            String sexo = req.getParameter("sexo");
            String curso = req.getParameter("cursoFiltro");
            String topNp = req.getParameter("topN");
            int topN = 5;
            try {
                topN = Integer.parseInt(topNp);
                if (topN < 1) topN = 5;
            } catch (Exception ignored) {
            }

            req.setAttribute("tipoFiltro", tipo != null ? tipo : "");
            req.setAttribute("sexoFiltro", sexo != null ? sexo : "TODOS");
            req.setAttribute("cursoFiltro2", curso != null ? curso : "");
            req.setAttribute("topNFiltro", topN);

            List<Map<String, Object>> resultado = new ArrayList<>();
            String tipoResultado = "";
            String tituloResultado = "Relatório sem filtros";

            if ("TOP_CURSOS".equals(tipo)) {
                resultado = dao.getTopCursosPorInscricoes(topN);
                tipoResultado = "cursos";
                tituloResultado = "Top " + topN + " Cursos com mais inscrições";
            } else if ("TOP_CURSOS_GENERO".equals(tipo)) {
                String s = (sexo == null || sexo.isBlank() || "TODOS".equals(sexo)) ? "M" : sexo;
                resultado = dao.getTopCursosPorGenero(topN, s);
                tipoResultado = "cursos";
                tituloResultado = "Top " + topN + " Cursos – " + ("M".equals(s) ? "Masculinos" : "Femininas");
            } else if ("TOP_ESTUDANTES_GENERO".equals(tipo)) {
                String s = (sexo == null || sexo.isBlank() || "TODOS".equals(sexo)) ? "M" : sexo;
                resultado = dao.getTopEstudantesPorGenero(topN, s);
                tipoResultado = "estudantes";
                tituloResultado = "Top " + topN + " Estudantes " + ("M".equals(s) ? "Masculinos" : "Femininas");
            } else if ("ESTUDANTES_CURSO_GENERO".equals(tipo)) {
                resultado = dao.getEstudantesPorCursoEGenero(topN, curso, sexo);
                tipoResultado = "estudantes";
                String descSexo = "TODOS".equals(sexo) || sexo == null ? "Todos" : ("M".equals(sexo) ? "Masculinos" : "Femininas");
                String descCurso = (curso == null || curso.isBlank()) ? "Todos os Cursos" : curso;
                tituloResultado = "Top " + topN + " Estudantes (" + descSexo + ") – " + descCurso;
            }

            req.setAttribute("resultado", resultado);
            req.setAttribute("tipoResultado", tipoResultado);
            req.setAttribute("tituloResultado", tituloResultado);

            boolean exportCsv = "csv".equalsIgnoreCase(req.getParameter("export"));
            if (exportCsv) {
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("text/csv;charset=UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename=relatorio.csv");
                try (PrintWriter out = resp.getWriter()) {
                    out.write("\uFEFF");
                    out.println("tipoRelatorio,titulo,topN,sexo,curso");
                    out.println(escapeCsv(tipoResultado) + "," + escapeCsv(tituloResultado) + "," + topN + "," + escapeCsv(sexo != null ? sexo : "TODOS") + "," + escapeCsv(curso != null ? curso : ""));
                    out.println();
                    if ("cursos".equals(tipoResultado)) {
                        out.println("curso,total");
                        for (Map<String, Object> item : resultado) {
                            out.println(escapeCsv(item.get("curso")) + "," + item.get("total"));
                        }
                    } else {
                        out.println("estudante,curso,email,sexo");
                        for (Map<String, Object> item : resultado) {
                            out.println(escapeCsv(item.get("estudante")) + "," + escapeCsv(item.get("curso")) + "," + escapeCsv(item.get("email")) + "," + escapeCsv(item.get("sexo")));
                        }
                    }
                }
                return;
            }

            req.getRequestDispatcher("/WEB-INF/views/relatorios.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro no relatório: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    private String escapeCsv(Object valor) {
        if (valor == null) return "";
        String texto = String.valueOf(valor).replace("\r\n", " ").replace("\n", " ");
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
