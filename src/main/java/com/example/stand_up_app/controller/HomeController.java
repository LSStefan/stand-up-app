package com.example.stand_up_app.controller;

import com.example.stand_up_app.model.Comediant;
import com.example.stand_up_app.repository.ComediantRepository;
import jakarta.servlet.http.HttpSession; // Import pentru sesiune
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ComediantRepository comediantRepo;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // 1. Gestionare Utilizator Logat
        // Luăm user-ul salvat în sesiune (numele trebuie să fie același ca în LoginController)
        String username = (String) session.getAttribute("utilizatorLogat");

        if (username == null || username.isEmpty()) {
            username = "Vizitator"; // Nume default dacă nu e nimeni logat
        }

        // Extragem prima literă pentru avatar
        String initiala = username.substring(0, 1).toUpperCase();

        // Trimitem datele despre user către HTML
        model.addAttribute("numeUtilizator", username);
        model.addAttribute("initiala", initiala);


        // 2. Gestionare Listă Comedianți (Codul tău original)
        List<Comediant> lista = comediantRepo.findAll();
        model.addAttribute("listaComedianti", lista);
        model.addAttribute("totalArtisti", lista.size());

        return "home";
    }

    @GetMapping({"/artist/{id}", "/artist/{id}"})
    public String paginaProfilArtist(@PathVariable("id") Long id, Model model, HttpSession session) {

        // APELĂM REPOSITORY-UL (FĂRĂ .orElse pentru că e JDBC)
        Comediant c = comediantRepo.findById(id);

        // DEBUG: Vedem în consolă dacă JDBC a extras datele
        if (c == null) {
            System.out.println("EROARE: JDBC nu a găsit artistul cu ID-ul " + id);
            return "redirect:/home";
        }

        // Trimitem datele către HTML
        model.addAttribute("artist", c);

        // Datele pentru Navbar (Nume și Inițială)
        String username = (String) session.getAttribute("utilizatorLogat");
        model.addAttribute("numeUtilizator", username != null ? username : "Vizitator");
        model.addAttribute("initiala", (username != null ? username.substring(0,1) : "V").toUpperCase());

        return "detalii_artist";
    }


}