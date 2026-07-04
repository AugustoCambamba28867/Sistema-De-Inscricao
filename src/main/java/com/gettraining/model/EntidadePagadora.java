package com.gettraining.model;

/**
 * Bean - Entidade Pagadora
 * Empresa ou entidade que paga o curso do formando
 */
public class EntidadePagadora {
    private int id;
    private String nome;
    private String morada;
    private String localidade;
    private String municipio;
    private String telefone;
    private String telemovel;
    private String fax;
    private String email;
    private String nif;
    private int formandoId;

    public EntidadePagadora() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public String getLocalidade() { return localidade; }
    public void setLocalidade(String localidade) { this.localidade = localidade; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTelemovel() { return telemovel; }
    public void setTelemovel(String telemovel) { this.telemovel = telemovel; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public int getFormandoId() { return formandoId; }
    public void setFormandoId(int formandoId) { this.formandoId = formandoId; }
}
