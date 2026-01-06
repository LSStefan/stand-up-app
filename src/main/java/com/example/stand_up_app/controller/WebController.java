package com.example.stand_up_app.controller;

import com.example.stand_up_app.repository.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class WebController {

    @Autowired
    private UtilizatorRepository utilizatorRepository; // Legătura cu Repository-ul tău

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String proceseazaLogin(@RequestParam String username, @RequestParam String password) {

        System.out.println("DEBUG: Verificăm în baza de date utilizatorul: " + username);

        // Apelăm metoda ta din Repository
        boolean esteValid = utilizatorRepository.verificaLogin(username.trim(), password.trim());

        if (esteValid) {
            System.out.println("DEBUG: Succes! Utilizator găsit.");
            return "redirect:/dashboard";
        } else {
            System.out.println("DEBUG: Eșec! Username sau parolă incorectă în tabela Clienti.");
            return "redirect:/login?error=true";
        }
    }
    // Aceasta PROCESEAZĂ datele (merge când apeși butonul)
    @PostMapping("/register")
    public String proceseazaInregistrare(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String nume,
                                         @RequestParam String prenume,
                                         @RequestParam String email,
                                         @RequestParam String telefon) {

        // Aici apelezi metoda de salvare în baza de date
        utilizatorRepository.salveazaUtilizator(nume, prenume, email, telefon, username, password);

        return "redirect:/login?success=true";
    }

    @GetMapping("/dashboard")
    public String paginaDashboard() {
        return "dashboard";
    }

    @GetMapping({"/", "/index"})
    public String paginaHome() {
        return "index";
    }

    @GetMapping({"/register"})
    public String paginaRegister() {
        return "register";
    }

//    @GetMapping("/home")
//    public String dashboardSimplu(Model model) {
//        // Punem un nume de test ca să nu crape Thymeleaf-ul unde scrie ${username}
//        model.addAttribute("username", "Vizitator");
//        return "home";
//    }
}