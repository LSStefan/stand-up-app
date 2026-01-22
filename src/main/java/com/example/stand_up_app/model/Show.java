package com.example.stand_up_app.model;

import java.util.Date;
/**
 * Clasa model destinata reprezentarii unui spectacol de stand-up.
 * Face legatura intre un artist, locatie si data desfasurarii,
 * fiind utilizata pentru gestionarea evenimentelor in panoul de administrare.
 * * @author Stefanita Lican
 * @version 10 Ianuarie 2026
 */
public class Show {
    private Integer showID;
    private String titlu;
    private Date data;
    private Integer nrBilete;
    private Integer pret;


    private String imagineUrl;



    private String numeArtisti;



    public String getImagineUrl() {
        return imagineUrl;
    }

    public void setImagineUrl(String imagineUrl) {
        this.imagineUrl = imagineUrl;
    }

    // Getter
    public String getNumeArtisti() {
        return numeArtisti;
    }

    // Setter
    public void setNumeArtisti(String numeArtisti) {
        this.numeArtisti = numeArtisti;
    }


    // Constructor gol
    public Show() {}


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