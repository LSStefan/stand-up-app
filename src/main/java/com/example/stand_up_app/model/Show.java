package com.example.stand_up_app.model;

import java.util.Date;

public class Show {
    private Integer showID; // Mapare pentru ShowID
    private String titlu;   // Mapare pentru Titlu
    private Date data;      // Mapare pentru Data (SQL type: date)
    private Integer nrBilete; // Mapare pentru NrBilete
    private Integer pret;   // Mapare pentru Pret

    // Constructor gol OBLIGATORIU
    public Show() {}

    // Getters și Setters cu nume camelCase corect
    public Integer getShowID() { return showID; }
    public void setShowID(Integer showID) { this.showID = showID; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Integer getNrBilete() { return nrBilete; }
    public void setNrBilete(Integer nrBilete) { this.nrBilete = nrBilete; }

    public Integer getPret() { return pret; }
    public void setPret(Integer pret) { this.pret = pret; }
}