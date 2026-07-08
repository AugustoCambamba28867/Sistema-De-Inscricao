package com.gettraining.controller;

import com.gettraining.dao.InscricaoDAO;
import com.gettraining.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@WebServlet(name = "EditarServlet", urlPatterns = {"/editar"})
public class EditarServlet extends HttpServlet {

    // Padrão de validação de hora (HH:MM format)
    private static final Pattern HORARIO_PATTERN =
            Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

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

            // Validar horários e período
            String periodo = req.getParameter("periodo");
            String horaInicio = req.getParameter("horaInicio");
            String horaFim = req.getParameter("horaFim");
            String duracao = req.getParameter("duracao");
            LocalDate periodoData = null;
            
            if (periodo != null && !periodo.isBlank()) {
                try {
                    periodoData = LocalDate.parse(periodo);
                    if (!isPeriodoPermitido(periodoData)) {
                        req.setAttribute("mensagemErro", "O período deve ser do ano atual ou, no máximo, janeiro/fevereiro do ano seguinte.");
                        req.setAttribute("inscricao", inscricao);
                        req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                        return;
                    }
                    if (periodoData.isBefore(LocalDate.now())) {
                        req.setAttribute("mensagemErro", "O período da formação não pode ser anterior a hoje.");
                        req.setAttribute("inscricao", inscricao);
                        req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    req.setAttribute("mensagemErro", "O período da formação deve estar no formato AAAA-MM-DD.");
                    req.setAttribute("inscricao", inscricao);
                    req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                    return;
                }
            }
            
            if (horaInicio == null || horaInicio.isBlank() || !HORARIO_PATTERN.matcher(horaInicio).matches()) {
                req.setAttribute("mensagemErro", "Hora de início inválida. Use HH:MM.");
                req.setAttribute("inscricao", inscricao);
                req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                return;
            }
            
            if (horaFim == null || horaFim.isBlank() || !HORARIO_PATTERN.matcher(horaFim).matches()) {
                req.setAttribute("mensagemErro", "Hora de fim inválida. Use HH:MM.");
                req.setAttribute("inscricao", inscricao);
                req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                return;
            }
            
            if (duracao == null || duracao.isBlank()) {
                req.setAttribute("mensagemErro", "Duração da formação é obrigatória.");
                req.setAttribute("inscricao", inscricao);
                req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
                return;
            }

            // Atualiza dados
            inscricao.getCurso().setNome(req.getParameter("curso"));
            inscricao.getCurso().setPeriodo(periodoData);
            inscricao.getCurso().setHoraInicio(horaInicio);
            inscricao.getCurso().setHoraFim(horaFim);
            inscricao.getCurso().setDuracao(duracao);
            
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

    private boolean isPeriodoPermitido(LocalDate periodoData) {
        int anoAtual = LocalDate.now().getYear();
        int anoPeriodo = periodoData.getYear();
        if (anoPeriodo < anoAtual) {
            return false;
        }
        if (anoPeriodo == anoAtual) {
            return true;
        }
        return anoPeriodo == anoAtual + 1 && periodoData.getMonthValue() <= 2;
    }
}
