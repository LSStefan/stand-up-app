package com.example.stand_up_app.controller;

import com.example.stand_up_app.model.Comediant;
import com.example.stand_up_app.model.Show;
import com.example.stand_up_app.repository.ComediantRepository;
import com.example.stand_up_app.repository.ShowRepository;
import jakarta.servlet.http.HttpSession; // Import pentru sesiune
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller destinat interfetei publice pentru clienti.
 * Gestioneaza afisarea listei de spectacole disponibile, detaliile artistilor
 * si paginile informative ale aplicatiei Stand-Up NOW.
 * * @author Stefanita Lican
 * @version 9 Ianuarie 2026
 */

@Controller
public class HomeController {

    @Autowired
    private ComediantRepository comediantRepo;
    @Autowired
    private ShowRepository showRepo;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // Gestionare Utilizator Logat
        // Luam user-ul salvat în sesiune
        String username = (String) session.getAttribute("utilizatorLogat");

        if (username == null || username.isEmpty()) {
            username = "Vizitator"; // Nume default dacă nu e nimeni logat
        }

        // Extragem prima litera pentru avatar
        String initiala = username.substring(0, 1).toUpperCase();

        // Trimitem datele despre user catre HTML
        model.addAttribute("numeUtilizator", username);
        model.addAttribute("initiala", initiala);


        // Gestionare lsita comedianti
        List<Comediant> lista = comediantRepo.findAll();
        model.addAttribute("listaComedianti", lista);
        model.addAttribute("totalArtisti", lista.size());

        List<Show> listaShowuri = showRepo.findAll();
        model.addAttribute("listaShowuri", listaShowuri);

        return "home";
    }

    @GetMapping({"/artist/{id}", "/artist/{id}"})
    public String paginaProfilArtist(@PathVariable("id") Long id, Model model, HttpSession session) {

        // apelam repoul pt functiile din el
        Comediant c = comediantRepo.findById(id);

        // DEBUG
        if (c == null) {
            System.out.println("EROARE: JDBC nu a găsit artistul cu ID-ul " + id);
            return "redirect:/home";
        }

        // Trimitem datele catre HTML
        model.addAttribute("artist", c);

        // Datele pentru Navbar (Nume și initiala)
        String username = (String) session.getAttribute("utilizatorLogat");
        model.addAttribute("numeUtilizator", username != null ? username : "Vizitator");
        model.addAttribute("initiala", (username != null ? username.substring(0,1) : "V").toUpperCase());

        return "detalii_artist";
    }





}