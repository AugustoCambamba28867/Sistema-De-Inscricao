package com.gettraining.model;

import java.time.LocalDate;

/**
 * Bean - Entidade Curso
 * Representa um curso da GET Training Academy
 */
public class Curso {
    private int id;
    private String nome;
    private LocalDate periodo;    // data da formação
    private String horaInicio;    // HH:MM
    private String horaFim;       // HH:MM
    private String duracao;       // em horas ou dias (ex: "40 horas", "5 dias")

    public Curso() {}

    public Curso(int id, String nome, LocalDate periodo, String horaInicio, String horaFim, String duracao) {
        this.id = id;
        this.nome = nome;
        this.periodo = periodo;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.duracao = duracao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getPeriodo() { return periodo; }
    public void setPeriodo(LocalDate periodo) { this.periodo = periodo; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFim() { return horaFim; }
    public void setHoraFim(String horaFim) { this.horaFim = horaFim; }

    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }

    @Override
    public String toString() {
        return "Curso{id=" + id + ", nome='" + nome + "', periodo='" + periodo + "', " +
               "horaInicio='" + horaInicio + "', horaFim='" + horaFim + "', duracao='" + duracao + "'}";
    }
}
