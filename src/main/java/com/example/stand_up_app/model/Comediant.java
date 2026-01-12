package com.example.stand_up_app.model; // Modifică aici cu pachetul tău



/**
 * Clasa model care reprezinta un comediant in cadrul aplicatiei.
 * Contine informatii despre identitatea artistului, descrierea acestuia
 * si alte detalii relevante pentru prezentarea pe site.
 * * @author Stefanita Lican
 * @version 10 Ianuarie 2026
 */

public class Comediant {
    // Acestea trebuie să fie exact ca în Baza de Date
    private Long comediantId;
    private String nume;
    private String prenume;
    private String numeScena;
    private String stil;
    private String imagineUrl;

    // Constructor fara argumente (Esențial pentru Spring/JDBC)
    public Comediant() {
    }

    // 2. Constructor cu toate argumentele (Util când vrei să creezi tu unul rapid)
    public Comediant(Long comediantId, String nume, String prenume, String numeScena, String stil, String imagineUrl) {
        this.comediantId = comediantId;
        this.nume = nume;
        this.prenume = prenume;
        this.numeScena = numeScena;
        this.stil = stil;
        this.imagineUrl = imagineUrl;
    }

    // 3. Gettere și Settere (JDBC le folosește pentru a citi/scrie datele)
    public Long getComediantId() { return comediantId; }
    public void setComediantId(Long comediantId) { this.comediantId = comediantId; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getNumeScena() { return numeScena; }
    public void setNumeScena(String numeScena) { this.numeScena = numeScena; }

    public String getStil() { return stil; }
    public void setStil(String stil) { this.stil = stil; }

    public String getImagineUrl() { return imagineUrl; }
    public void setImagineUrl(String imagineUrl) { this.imagineUrl = imagineUrl; }

    public Comediant orElse(Object o) {
        return null;
    }
}