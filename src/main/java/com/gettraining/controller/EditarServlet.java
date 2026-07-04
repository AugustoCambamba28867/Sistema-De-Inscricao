package com.gettraining.controller;

import com.gettraining.dao.InscricaoDAO;
import com.gettraining.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "EditarServlet", urlPatterns = {"/editar"})
public class EditarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        com.gettraining.model.Admin admin = (com.gettraining.model.Admin) req.getSession().getAttribute("admin");
        if (admin != null && "LEITOR".equals(admin.getPapel())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado.");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            try {
                int id = Integer.parseInt(idParam);
                InscricaoDAO dao = new InscricaoDAO();
                Inscricao inscricao = dao.buscarPorId(id);
                if (inscricao != null) {
                    req.setAttribute("inscricao", inscricao);
                    req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                    return;
                }
            } catch (Exception e) {
                req.setAttribute("mensagemErro", "Erro ao buscar inscrição para edição.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/listagem");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        com.gettraining.model.Admin admin = (com.gettraining.model.Admin) req.getSession().getAttribute("admin");
        if (admin != null && "LEITOR".equals(admin.getPapel())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado.");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        
        try {
            int id = Integer.parseInt(req.getParameter("inscricaoId"));
            InscricaoDAO dao = new InscricaoDAO();
            Inscricao inscricao = dao.buscarPorId(id);
            
            if (inscricao == null) {
                resp.sendRedirect(req.getContextPath() + "/listagem");
                return;
            }

            // Atualiza dados
            inscricao.getCurso().setNome(req.getParameter("curso"));
            inscricao.getCurso().setHorario(req.getParameter("horario"));
            
            Formando f = inscricao.getFormando();
            f.setNome(req.getParameter("nome"));
            f.setMorada(req.getParameter("morada"));
            f.setLocalidade(req.getParameter("localidade"));
            f.setMunicipio(req.getParameter("municipio"));
            f.setTelefone(req.getParameter("telefone"));
            f.setTelemovel(req.getParameter("telemovel"));
            f.setEmail(req.getParameter("email"));
            
            String dtNasc = req.getParameter("dataNascimento");
            if (dtNasc != null && !dtNasc.isBlank()) f.setDataNascimento(LocalDate.parse(dtNasc));
            f.setSexo(req.getParameter("sexo"));
            
            EntidadePagadora e = inscricao.getEntidade();
            if (e != null) {
                e.setNome(req.getParameter("epNome"));
                e.setMorada(req.getParameter("epMorada"));
                e.setLocalidade(req.getParameter("epLocalidade"));
                e.setMunicipio(req.getParameter("epMunicipio"));
                e.setTelefone(req.getParameter("epTelefone"));
                e.setTelemovel(req.getParameter("epTelemovel"));
                e.setFax(req.getParameter("epFax"));
                e.setEmail(req.getParameter("epEmail"));
                e.setNif(req.getParameter("epNif"));
            }
            
            ResponsavelRH rh = inscricao.getResponsavelRH();
            if (rh != null) {
                rh.setNome(req.getParameter("rhNome"));
                rh.setTelefone(req.getParameter("rhTelefone"));
                rh.setTelemovel(req.getParameter("rhTelemovel"));
                rh.setEmail(req.getParameter("rhEmail"));
            }

            dao.atualizar(inscricao);
            
            resp.sendRedirect(req.getContextPath() + "/listagem?sucesso=true");

        } catch (Exception ex) {
            req.setAttribute("mensagemErro", "Erro ao editar inscrição: " + ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }
}
