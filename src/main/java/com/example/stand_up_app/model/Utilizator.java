package com.example.stand_up_app.model;

public class Utilizator {
    private int id;
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    private String username;
    private String parola;

    // Constructor gol necesar pentru framework
    public Utilizator() {}

    // Getters si Setters (necesari pentru ca REST API-ul sa poata citi datele)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }
}