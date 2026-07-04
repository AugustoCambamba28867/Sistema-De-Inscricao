package com.gettraining.model;

/**
 * Bean - Entidade Curso
 * Representa um curso da GET Training Academy
 */
public class Curso {
    private int id;
    private String nome;
    private String horario; // Manhã, Tarde, Fim de Tarde, Sábado

    public Curso() {}

    public Curso(int id, String nome, String horario) {
        this.id = id;
        this.nome = nome;
        this.horario = horario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    @Override
    public String toString() {
        return "Curso{id=" + id + ", nome='" + nome + "', horario='" + horario + "'}";
    }
}
