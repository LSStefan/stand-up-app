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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Scanner;



/**
 * Controller responsabil cu gestionarea fluxului de autentificare.
 * Realizeaza validarea credentialelor, gestionarea sesiunilor utilizatorilor
 * si redirectionarea acestora catre interfata corespunzatoare rolului (Admin/Client).
 * * @author Stefanita Lican
 * @version 9 Ianuarie 2026
 */

@Controller
public class WebController {

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    @Autowired
    private ComediantRepository comediantRepo;

    @Autowired
    private ShowRepository showRepo;


    @GetMapping("/index")
    public String pornireAplicatie() {
        // Thymeleaf va cauta fișierul index.html in folderul templates
        return "index";
    }

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String proceseazaLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {

        System.out.println("DEBUG: Verificăm în baza de date utilizatorul: " + username);

        // verificam daca userul si parola exista in bd
        boolean esteValid = utilizatorRepository.verificaLogin(username.trim(), password.trim());

        if (esteValid) {
            // salvam numele în sesiune pentru a-l recunoaste pe paginile protejate
            session.setAttribute("utilizatorLogat", username);

            // Verificam daca cel care s-a logat este adminul
            if (username.equalsIgnoreCase("admin")) {
                System.out.println("DEBUG: Admin detectat. Redirecționare către Dashboard.");
                return "redirect:/admin"; // il trimitem la panoul de control
            }

            //Daca nu e admin, merge la pagina normală de utilizator
            return "redirect:/home";
        } else {
            // Dacă datele sunt greșite
            return "redirect:/login?error=true";
        }
    }


    @PostMapping("/register")
    public String proceseazaInregistrare(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String nume,
                                         @RequestParam String prenume,
                                         @RequestParam String email,
                                         @RequestParam String telefon,
                                         RedirectAttributes redirectAttributes) {

        boolean areErori = false;

        // Validare Email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            redirectAttributes.addFlashAttribute("errorEmail", "Format email invalid (ex: nume@yahoo.com)!");
            areErori = true;
        }

        // Validare Telefon
        if (!telefon.matches("^\\d{10}$")) {
            redirectAttributes.addFlashAttribute("errorTelefon", "Telefonul trebuie să aibă fix 10 cifre!");
            areErori = true;
        }

        // Validare Parola
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("errorPassword", "Parola este prea scurtă (minim 6 caractere)!");
            areErori = true;
        }

        // Daca am gasit cel putin o eroare, ne oprim si ne intoarcem la formular
        if (areErori) {
            return "redirect:/register";
        }

        try {
            utilizatorRepository.salveazaUtilizator(nume, prenume, email, telefon, username, password);
            return "redirect:/login?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorGeneral", "Username sau Email deja existente!");
            return "redirect:/register";
        }
    }

    @GetMapping("/dashboard")
    public String paginaDashboard() {
        return "dashboard";
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