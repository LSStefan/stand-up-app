package com.example.stand_up_app.controller;

import com.example.stand_up_app.repository.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UtilizatorRestController {

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    // Endpoint pentru LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dateLogin) {
        String user = dateLogin.get("username");
        String pass = dateLogin.get("parola");

        boolean succes = utilizatorRepository.verificaLogin(user, pass);

        if (succes) {
            return ResponseEntity.ok().body(Map.of("status", "succes", "mesaj", "Autentificare reusita!"));
        } else {
            return ResponseEntity.status(401).body(Map.of("status", "eroare", "mesaj", "Username sau parola gresite!"));
        }
    }

    // Endpoint pentru SIGN-UP (Inregistrare)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> date) {
        try {
            utilizatorRepository.salveazaUtilizator(
                    date.get("nume"),
                    date.get("prenume"),
                    date.get("email"),
                    date.get("telefon"),
                    date.get("username"),
                    date.get("parola")
            );
            return ResponseEntity.ok().body(Map.of("status", "succes", "mesaj", "Utilizator creat!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "eroare", "mesaj", e.getMessage()));
        }
    }
}