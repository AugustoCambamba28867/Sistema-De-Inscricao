package com.gettraining.model;

public class Admin {
    private int id;
    private String username;
    
    // Construtores, Getters e Setters
    public Admin() {}
    
    public Admin(int id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
