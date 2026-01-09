package com.example.stand_up_app.controller;

import com.example.stand_up_app.repository.ComediantRepository;
import com.example.stand_up_app.repository.ShowRepository;
import com.example.stand_up_app.repository.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession; // Import pentru sesiune

import java.util.Scanner;





@Controller
public class WebController {

    @Autowired
    private UtilizatorRepository utilizatorRepository; // Legătura cu Repository-ul tău

    @Autowired
    private ComediantRepository comediantRepo; // Avem nevoie de el pentru statistici

    @Autowired
    private ShowRepository showRepo; // Avem nevoie de el pentru statistici

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String proceseazaLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {

        System.out.println("DEBUG: Verificăm în baza de date utilizatorul: " + username);

        // 1. Verificăm dacă user-ul și parola există în baza de date
        boolean esteValid = utilizatorRepository.verificaLogin(username.trim(), password.trim());

        if (esteValid) {
            // 2. Salvăm numele în sesiune pentru a-l recunoaște pe paginile protejate
            session.setAttribute("utilizatorLogat", username);

            // 3. Verificăm dacă cel care s-a logat este Adminul
            if (username.equalsIgnoreCase("admin")) {
                System.out.println("DEBUG: Admin detectat. Redirecționare către Dashboard.");
                return "redirect:/admin"; // Îl trimitem la panoul de control
            }

            // 4. Dacă nu e admin, merge la pagina normală de utilizator
            return "redirect:/home";
        } else {
            // Dacă datele sunt greșite
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