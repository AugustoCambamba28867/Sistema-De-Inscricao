package sdpfrequencia.negocio;

import java.io.IOException;
import java.util.List;
import sdpfrequencia.dao.FichaInscricaoDAO;
import sdpfrequencia.modelo.FichaInscricao;

public class FichaInscricaoService {

    private FichaInscricaoDAO dao;

    public FichaInscricaoService() {
        this.dao = new FichaInscricaoDAO();
    }

    public void salvar(FichaInscricao ficha) throws Exception {
        // Validações de Negócio
        if (ficha.getCurso() == null || ficha.getCurso().trim().isEmpty()) {
            throw new Exception("O campo 'Curso' é obrigatório.");
        }
        if (ficha.getHorario() == null || ficha.getHorario().trim().isEmpty()) {
            throw new Exception("Selecione um Horário.");
        }
        if (ficha.getNome() == null || ficha.getNome().trim().isEmpty()) {
            throw new Exception("O campo 'Nome' é obrigatório.");
        }
        if (ficha.getEmail() != null && !ficha.getEmail().trim().isEmpty() && !ficha.getEmail().contains("@")) {
            throw new Exception("O 'Email' fornecido é inválido.");
        }

        // Se passar nas validações, manda para a camada de persistência
        try {
            dao.salvar(ficha);
        } catch (IOException e) {
            throw new Exception("Erro técnico ao salvar a ficha: " + e.getMessage());
        }
    }

    public List<FichaInscricao> listarTodas() throws Exception {
        try {
            return dao.listarTodas();
        } catch (IOException e) {
            throw new Exception("Erro técnico ao listar fichas: " + e.getMessage());
        }
    }
}