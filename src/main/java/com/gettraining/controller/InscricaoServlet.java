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

/**
 * Controlador - Ficha de Inscrição
 * GET  → mostra o formulário (formulario.jsp)
 * POST → valida e grava a inscrição na BD
 */
@WebServlet(name = "InscricaoServlet", urlPatterns = {"/inscricao"})
public class InscricaoServlet extends HttpServlet {

    // Padrão de validação de email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    // Padrão de validação de telefone (apenas dígitos, +, -, espaço)
    private static final Pattern TELEFONE_PATTERN =
            Pattern.compile("^[\\d\\s\\+\\-]{7,20}$");

    /** GET – Exibir formulário de inscrição */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(req, resp);
    }

    /** POST – Processar inscrição */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // ===== RECOLHER DADOS DO FORMULÁRIO =====
        String nomeCurso   = sanitizar(req.getParameter("curso"));
        String horario     = sanitizar(req.getParameter("horario"));
        String nome        = sanitizar(req.getParameter("nome"));
        String morada      = sanitizar(req.getParameter("morada"));
        String localidade  = sanitizar(req.getParameter("localidade"));
        String municipio   = sanitizar(req.getParameter("municipio"));
        String telefone    = sanitizar(req.getParameter("telefone"));
        String telemovel   = sanitizar(req.getParameter("telemovel"));
        String email       = sanitizar(req.getParameter("email"));
        String nascimento  = sanitizar(req.getParameter("dataNascimento"));
        String sexo        = sanitizar(req.getParameter("sexo"));

        // Entidade Pagadora
        String epNome      = sanitizar(req.getParameter("epNome"));
        String epMorada    = sanitizar(req.getParameter("epMorada"));
        String epLocal     = sanitizar(req.getParameter("epLocalidade"));
        String epMun       = sanitizar(req.getParameter("epMunicipio"));
        String epTel       = sanitizar(req.getParameter("epTelefone"));
        String epMov       = sanitizar(req.getParameter("epTelemovel"));
        String epFax       = sanitizar(req.getParameter("epFax"));
        String epEmail     = sanitizar(req.getParameter("epEmail"));
        String epNif       = sanitizar(req.getParameter("epNif"));

        // Responsável RH
        String rhNome      = sanitizar(req.getParameter("rhNome"));
        String rhTel       = sanitizar(req.getParameter("rhTelefone"));
        String rhMov       = sanitizar(req.getParameter("rhTelemovel"));
        String rhEmail     = sanitizar(req.getParameter("rhEmail"));

        // ===== VALIDAÇÃO NO SERVIDOR =====
        StringBuilder erros = new StringBuilder();

        if (nomeCurso == null || nomeCurso.isBlank())
            erros.append("• O nome do curso é obrigatório.<br>");

        if (horario == null || horario.isBlank())
            erros.append("• Seleccione um horário preferencial.<br>");

        if (nome == null || nome.isBlank())
            erros.append("• O nome do formando é obrigatório.<br>");
        else if (nome.length() < 3)
            erros.append("• O nome deve ter pelo menos 3 caracteres.<br>");

        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            erros.append("• O email do formando é inválido.<br>");

        if (telefone != null && !telefone.isBlank() && !TELEFONE_PATTERN.matcher(telefone).matches())
            erros.append("• O telefone contém caracteres inválidos.<br>");

        if (telemovel != null && !telemovel.isBlank() && !TELEFONE_PATTERN.matcher(telemovel).matches())
            erros.append("• O telemóvel contém caracteres inválidos.<br>");

        if (sexo == null || (!sexo.equals("M") && !sexo.equals("F")))
            erros.append("• Seleccione o sexo.<br>");

        LocalDate dataNasc = null;
        if (nascimento != null && !nascimento.isBlank()) {
            try {
                dataNasc = LocalDate.parse(nascimento);
                if (dataNasc.isAfter(LocalDate.now()))
                    erros.append("• A data de nascimento não pode ser futura.<br>");
            } catch (DateTimeParseException e) {
                erros.append("• Formato de data inválido (use AAAA-MM-DD).<br>");
            }
        }

        // Validação do NIF da entidade (se preenchido)
        if (epNif != null && !epNif.isBlank() && epNif.length() < 9)
            erros.append("• O NIF da entidade deve ter pelo menos 9 dígitos.<br>");

        if (epEmail != null && !epEmail.isBlank() && !EMAIL_PATTERN.matcher(epEmail).matches())
            erros.append("• O email da entidade pagadora é inválido.<br>");

        if (rhEmail != null && !rhEmail.isBlank() && !EMAIL_PATTERN.matcher(rhEmail).matches())
            erros.append("• O email do Responsável RH é inválido.<br>");

        // Se houver erros, reenviar formulário com mensagens
        if (erros.length() > 0) {
            req.setAttribute("erros", erros.toString());
            // Re-popular formulário com os dados submetidos
            req.setAttribute("valoresAnteriores", req.getParameterMap());
            req.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(req, resp);
            return;
        }

        // ===== CONSTRUIR OBJECTOS DO MODELO =====
        Curso curso = new Curso();
        curso.setNome(nomeCurso);
        curso.setHorario(horario);

        Formando formando = new Formando();
        formando.setNome(nome);
        formando.setMorada(morada);
        formando.setLocalidade(localidade);
        formando.setMunicipio(municipio);
        formando.setTelefone(telefone);
        formando.setTelemovel(telemovel);
        formando.setEmail(email);
        formando.setDataNascimento(dataNasc);
        formando.setSexo(sexo);

        EntidadePagadora entidade = new EntidadePagadora();
        entidade.setNome(epNome);
        entidade.setMorada(epMorada);
        entidade.setLocalidade(epLocal);
        entidade.setMunicipio(epMun);
        entidade.setTelefone(epTel);
        entidade.setTelemovel(epMov);
        entidade.setFax(epFax);
        entidade.setEmail(epEmail);
        entidade.setNif(epNif);

        ResponsavelRH rh = new ResponsavelRH();
        rh.setNome(rhNome);
        rh.setTelefone(rhTel);
        rh.setTelemovel(rhMov);
        rh.setEmail(rhEmail);

        Inscricao inscricao = new Inscricao();
        inscricao.setCurso(curso);
        inscricao.setFormando(formando);
        inscricao.setEntidade(entidade);
        inscricao.setResponsavelRH(rh);

        // ===== GRAVAR NA BASE DE DADOS =====
        try {
            InscricaoDAO dao = new InscricaoDAO();
            int idGerado = dao.gravar(inscricao);
            req.setAttribute("inscricaoId", idGerado);
            req.setAttribute("nomeFormando", nome);
            req.setAttribute("nomeCurso", nomeCurso);
            req.getRequestDispatcher("/WEB-INF/views/sucesso.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("mensagemErro", "Erro ao gravar inscrição: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    /**
     * Sanitiza input para prevenir XSS
     */
    private String sanitizar(String valor) {
        if (valor == null) return "";
        return valor.trim()
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
