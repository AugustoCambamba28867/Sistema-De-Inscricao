package com.gettraining.controller;

import com.gettraining.dao.InscricaoDAO;
import com.gettraining.model.Inscricao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Controlador - Listagem de Inscrições
 * GET /listagem         → lista todas as inscrições
 * GET /listagem?id=X   → detalhe de uma inscrição específica
 * POST /listagem?action=eliminar&id=X → eliminar inscrição
 */
@WebServlet(name = "ListagemServlet", urlPatterns = {"/listagem"})
public class ListagemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String idParam     = req.getParameter("id");
        String cursoFiltro = req.getParameter("curso");
        String topNParam   = req.getParameter("topN");
        String ordenarPor  = req.getParameter("ordenar");

        // Detalhe de inscrição específica
        if (idParam != null && !idParam.isBlank()) {
            int id;
            try {
                id = Integer.parseInt(idParam);
                if (id <= 0) throw new NumberFormatException("ID inválido");
            } catch (NumberFormatException e) {
                req.setAttribute("mensagemErro", "ID de inscrição inválido: " + idParam);
                req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
                return;
            }

            try {
                InscricaoDAO dao = new InscricaoDAO();
                Inscricao ins = dao.buscarPorId(id);
                if (ins == null) {
                    req.setAttribute("mensagemErro", "Inscrição #" + id + " não encontrada.");
                    req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
                    return;
                }
                req.setAttribute("inscricao", ins);
                req.getRequestDispatcher("/WEB-INF/views/detalhe.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("mensagemErro", "Erro ao carregar inscrição: " + e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
            }
            return;
        }

        // Listagem com filtros
        try {
            InscricaoDAO dao = new InscricaoDAO();
            List<Inscricao> lista = dao.listarTodas();

            // Coleta todos os cursos únicos para o dropdown de filtro
            java.util.Set<String> cursos = new java.util.LinkedHashSet<>();
            for (Inscricao ins : lista) {
                if (ins.getCurso() != null && ins.getCurso().getNome() != null)
                    cursos.add(ins.getCurso().getNome());
            }
            req.setAttribute("cursos", cursos);

            // Filtrar por nome de curso
            if (cursoFiltro != null && !cursoFiltro.isBlank()) {
                lista = lista.stream()
                    .filter(i -> cursoFiltro.equalsIgnoreCase(i.getCurso().getNome()))
                    .collect(java.util.stream.Collectors.toList());
            }

            // Ordenar: por data_asc ou por nome
            if ("nome".equals(ordenarPor)) {
                lista.sort((a, b) -> a.getFormando().getNome().compareToIgnoreCase(b.getFormando().getNome()));
            } else if ("curso".equals(ordenarPor)) {
                lista.sort((a, b) -> a.getCurso().getNome().compareToIgnoreCase(b.getCurso().getNome()));
            }
            // default: já vem DESC por data

            // Limitar top N
            if (topNParam != null && !topNParam.isBlank()) {
                try {
                    int topN = Integer.parseInt(topNParam);
                    if (topN > 0 && topN < lista.size()) {
                        lista = lista.subList(0, topN);
                    }
                } catch (NumberFormatException ignored) {}
            }

            req.setAttribute("inscricoes", lista);
            req.setAttribute("cursoFiltro", cursoFiltro);
            req.setAttribute("topNParam", topNParam);
            req.setAttribute("ordenarPor", ordenarPor);
            req.getRequestDispatcher("/WEB-INF/views/listagem.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao carregar inscrições: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        com.gettraining.model.Admin admin = (com.gettraining.model.Admin) req.getSession().getAttribute("admin");
        if (admin == null || !"SUPER_ADMIN".equals(admin.getPapel())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado.");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if ("eliminar".equals(action) && idParam != null) {
            int id;
            try {
                id = Integer.parseInt(idParam);
                if (id <= 0) throw new NumberFormatException("ID inválido");
            } catch (NumberFormatException e) {
                req.setAttribute("mensagemErro", "ID de inscrição inválido para eliminar.");
                req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
                return;
            }
            try {
                new InscricaoDAO().eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/listagem?eliminado=" + id);
            } catch (Exception e) {
                req.setAttribute("mensagemErro", "Erro ao eliminar inscrição: " + e.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/listagem");
        }
    }
}
