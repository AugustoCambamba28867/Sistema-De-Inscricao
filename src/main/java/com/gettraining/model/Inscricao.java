package com.gettraining.model;

import java.time.LocalDateTime;

/**
 * Bean - Inscrição
 * Registo principal que agrega todos os dados de uma inscrição
 */
public class Inscricao {
    private int id;
    private Formando formando;
    private Curso curso;
    private EntidadePagadora entidade;
    private ResponsavelRH responsavelRH;
    private LocalDateTime dataInscricao;

    public Inscricao() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Formando getFormando() { return formando; }
    public void setFormando(Formando formando) { this.formando = formando; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public EntidadePagadora getEntidade() { return entidade; }
    public void setEntidade(EntidadePagadora entidade) { this.entidade = entidade; }

    public ResponsavelRH getResponsavelRH() { return responsavelRH; }
    public void setResponsavelRH(ResponsavelRH responsavelRH) { this.responsavelRH = responsavelRH; }

    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }
}
