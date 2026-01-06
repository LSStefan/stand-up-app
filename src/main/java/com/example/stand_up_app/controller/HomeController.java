package com.example.stand_up_app.controller; // Pune pachetul tău aici

import com.example.stand_up_app.model.Comediant;
import com.example.stand_up_app.repository.ComediantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ComediantRepository comediantRepo;

    @GetMapping("/home")
    public String home(Model model) {
        // Luăm lista din baza de date
        List<Comediant> lista = comediantRepo.findAll();

        // Trimitem datele către HTML (home.html)
        model.addAttribute("listaComedianti", lista);
        model.addAttribute("totalArtisti", lista.size());

        // Returnează numele fișierului HTML din folderul templates
        return "home";
    }
}