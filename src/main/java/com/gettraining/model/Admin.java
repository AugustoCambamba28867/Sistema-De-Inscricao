package com.gettraining.model;

public class Admin {
    private int id;
    private String username;
    private String papel; // 'SUPER_ADMIN' or 'GESTOR'
    
    // Construtores, Getters e Setters
    public Admin() {}
    
    public Admin(int id, String username, String papel) {
        this.id = id;
        this.username = username;
        this.papel = papel;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
}
