package sdpfrequencia.modelo;

public class FichaInscricao {
    // Curso
    private String curso;
    private String horario;

    // Formando
    private String nome;
    private String morada;
    private String localidade;
    private String municipio;
    private String telefone;
    private String telemovel;
    private String email;
    private String nascimento;
    private String sexo;

    // Entidade Pagadora
    private String epNome;
    private String epMorada;
    private String epLocalidade;
    private String epMunicipio;
    private String epTelefone;
    private String epTelemovel;
    private String epFax;
    private String epEmail;
    private String epNif;

    // Responsável RH
    private String rhNome;
    private String rhTelefone;
    private String rhTelemovel;
    private String rhEmail;

    public FichaInscricao() {
    }

    // Getters and Setters

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNascimento() { return nascimento; }
    public void setNascimento(String nascimento) { this.nascimento = nascimento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEpNome() { return epNome; }
    public void setEpNome(String epNome) { this.epNome = epNome; }

    public String getEpMorada() { return epMorada; }
    public void setEpMorada(String epMorada) { this.epMorada = epMorada; }

    public String getEpLocalidade() { return epLocalidade; }
    public void setEpLocalidade(String epLocalidade) { this.epLocalidade = epLocalidade; }

    public String getEpMunicipio() { return epMunicipio; }
    public void setEpMunicipio(String epMunicipio) { this.epMunicipio = epMunicipio; }

    public String getEpTelefone() { return epTelefone; }
    public void setEpTelefone(String epTelefone) { this.epTelefone = epTelefone; }

    public String getEpTelemovel() { return epTelemovel; }
    public void setEpTelemovel(String epTelemovel) { this.epTelemovel = epTelemovel; }

    public String getEpFax() { return epFax; }
    public void setEpFax(String epFax) { this.epFax = epFax; }

    public String getEpEmail() { return epEmail; }
    public void setEpEmail(String epEmail) { this.epEmail = epEmail; }

    public String getEpNif() { return epNif; }
    public void setEpNif(String epNif) { this.epNif = epNif; }

    public String getRhNome() { return rhNome; }
    public void setRhNome(String rhNome) { this.rhNome = rhNome; }

    public String getRhTelefone() { return rhTelefone; }
    public void setRhTelefone(String rhTelefone) { this.rhTelefone = rhTelefone; }

    public String getRhTelemovel() { return rhTelemovel; }
    public void setRhTelemovel(String rhTelemovel) { this.rhTelemovel = rhTelemovel; }

    public String getRhEmail() { return rhEmail; }
    public void setRhEmail(String rhEmail) { this.rhEmail = rhEmail; }
}
