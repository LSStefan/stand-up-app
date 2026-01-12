package com.example.stand_up_app.controller;

import com.example.stand_up_app.repository.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller de tip REST destinat operatiunilor asincrone de inregistrare.
 * Gestioneaza primirea datelor in format JSON de la interfata de utilizator
 * si coordoneaza procesul de salvare a noilor clienti in baza de date.
 * * @author Stefanita Lican
 * @version 9 Ianuarie 2026
 */

@RestController
@RequestMapping("/api") // Toate rutele de aici încep cu /api (ex: /api/register)
public class UtilizatorRestController {

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    /**
     * Endpoint pentru inregistrarea unui utilizator nou.
     * Acesta primeste datele sub forma de JSON din frontend.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> date) {
        try {
            // Extragem datele din Map-ul primit de la JavaScript/Frontend
            String nume = date.get("nume");
            String prenume = date.get("prenume");
            String email = date.get("email");
            String telefon = date.get("telefon");
            String username = date.get("username");
            String parola = date.get("parola");

            // Salvăm în baza de date folosind repository-ul tău
            utilizatorRepository.salveazaUtilizator(
                    nume,
                    prenume,
                    email,
                    telefon,
                    username,
                    parola
            );

            // Returnăm un răspuns de succes
            return ResponseEntity.ok().body(Map.of(
                    "status", "succes",
                    "mesaj", "Utilizator creat cu succes!"
            ));

        } catch (Exception e) {
            // În caz de eroare (ex: email duplicat), trimitem eroarea înapoi
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "eroare",
                    "mesaj", "Eroare la salvare: " + e.getMessage()
            ));
        }
    }
}