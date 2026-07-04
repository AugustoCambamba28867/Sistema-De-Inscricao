package com.gettraining.model;

/**
 * Bean - Responsável de RH
 * Contacto de recursos humanos associado ao formando
 */
public class ResponsavelRH {
    private int id;
    private String nome;
    private String telefone;
    private String telemovel;
    private String email;
    private int formandoId;

    public ResponsavelRH() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTelemovel() { return telemovel; }
    public void setTelemovel(String telemovel) { this.telemovel = telemovel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getFormandoId() { return formandoId; }
    public void setFormandoId(int formandoId) { this.formandoId = formandoId; }
}
