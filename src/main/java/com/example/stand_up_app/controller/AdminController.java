package com.example.stand_up_app.controller;

import com.example.stand_up_app.model.Comediant;
import com.example.stand_up_app.model.Show;
import com.example.stand_up_app.model.Utilizator;
import com.example.stand_up_app.repository.AdminRepository;
import com.example.stand_up_app.repository.ComediantRepository;
import com.example.stand_up_app.repository.ShowRepository;
import com.example.stand_up_app.repository.UtilizatorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** * Controller pentru gestionarea panoului de administrare.
 * Permite managementul artiștilor, spectacolelor și vizualizarea utilizatorilor.
 * @author Stefanita Lican
 * @version 10 Ianuarie 2026
 */

@Controller
@RequestMapping("/admin") //toate rutele vor incepe cu /admin
public class AdminController {

    @Autowired
    private ComediantRepository comediantRepo;

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private UtilizatorRepository utilizatorRepo;

    @Autowired
    private AdminRepository adminRepo;



    // Ruta: /admin
    @GetMapping
    public String adminDashboard(Model model, HttpSession session) {
        String user = (String) session.getAttribute("utilizatorLogat");

        if (user == null || !user.equalsIgnoreCase("admin")) {
            return "redirect:/login";
        }

        // Statistici de bază
        model.addAttribute("totalArtisti", comediantRepo.findAll().size());
        model.addAttribute("totalSpectacole", showRepo.findAll().size());

        // --- DATE PENTRU RAPOARTE COMPLEXE ---
        model.addAttribute("topArtisti", adminRepo.getTopArtisti());
        model.addAttribute("clientiPremium", adminRepo.getClientiPremium());
        model.addAttribute("showuriGoale", adminRepo.getShowuriFaraVanzari());
        model.addAttribute("jurnalVanzari", adminRepo.getJurnalCompletVanzari());
        model.addAttribute("ocupare", adminRepo.getGradOcupare());

        // --- DATE PENTRU DROPDOWN-URI (Alocare Many-to-Many) ---
        // Acestea trimit listele necesare pentru formularul de alocare
        model.addAttribute("listaComedianti", comediantRepo.findAll());
        model.addAttribute("listaShowuri", showRepo.findAll());

        return "admin";
    }

    // Ruta: /admin/spectacole
    @GetMapping("/spectacole")
    public String gestionareSpectacole(@RequestParam(required = false) String sort, Model model, HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        List<Show> spectacole = showRepo.findAll();

        // Sortare în memorie folosind Java Streams
        if ("pret".equals(sort)) {
            spectacole = spectacole.stream()
                    .sorted(Comparator.comparingInt(Show::getPret))
                    .collect(Collectors.toList());
        } else if ("data".equals(sort)) {
            spectacole = spectacole.stream()
                    .sorted(Comparator.comparing(Show::getData))
                    .collect(Collectors.toList());
        }

        model.addAttribute("lista", spectacole);
        model.addAttribute("listaArtisti", comediantRepo.findAll()); // Pentru dropdown
        return "admin_spectacole";
    }

    // Ruta actualizată pentru a salva și imaginea
    @PostMapping("/spectacole/salveaza")
    public String salveazaShow(@RequestParam String titlu,
                               @RequestParam String data,
                               @RequestParam Integer bilete,
                               @RequestParam Integer pret,
                               @RequestParam String urlPoza, // 1. Adăugăm parametrul nou
                               HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        Show s = new Show();
        s.setTitlu(titlu);
        s.setData(java.sql.Date.valueOf(data));
        s.setNrBilete(bilete);
        s.setPret(pret);
        s.setImagineUrl(urlPoza); // 2. Setăm URL-ul imaginii pe obiectul Show

        showRepo.save(s);
        return "redirect:/admin/spectacole";
    }

    // Ruta: /admin/spectacole/sterge/{id}
    @GetMapping("/spectacole/sterge/{id}")
    public String stergeShow(@PathVariable Integer id, HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        showRepo.deleteById(id);
        return "redirect:/admin/spectacole";
    }

    private boolean isUserAdmin(HttpSession session) {
        String user = (String) session.getAttribute("utilizatorLogat");
        return user != null && user.equalsIgnoreCase("admin");
    }

    // Afișarea listei de artiști
    @GetMapping("/artisti")
    public String gestionareArtisti(Model model, HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        model.addAttribute("listaArtisti", comediantRepo.findAll());
        return "admin_artisti";
    }

    // Salvare artist nou
    @PostMapping("/artisti/salveaza")
    public String salveazaArtist(@RequestParam String nume,
                                 @RequestParam String prenume,
                                 @RequestParam String numeScena,
                                 @RequestParam String stil,
                                 @RequestParam String urlPoza,
                                 HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        Comediant c = new Comediant();
        c.setNume(nume);
        c.setPrenume(prenume);
        c.setNumeScena(numeScena);
        c.setStil(stil);
        c.setImagineUrl(urlPoza);

        comediantRepo.save(c);
        return "redirect:/admin/artisti";
    }

    // Ștergere artist
    @GetMapping("/artisti/sterge/{id}")
    public String stergeArtist(@PathVariable Long id, HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        comediantRepo.deleteById(id);
        return "redirect:/admin/artisti";
    }

    @GetMapping("/utilizatori")
    public String listaUtilizatori(Model model, HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

        //Luam toți utilizatorii din baza de date
        List<Utilizator> toti = utilizatorRepo.findAll();

        // trimit lista catre html ca sa o afisez
        model.addAttribute("listaUtilizatori", toti);

        return "admin_useri";
    }

    // Punem ruta completa aici ca sa nu mai depindem de ce e scris deasupra clasei
    // RUTA SIMPLIFICATĂ
    @PostMapping("/aloca")
    public String alocaArtistLaShow(@RequestParam Integer comediantId, @RequestParam Integer showId) {
        System.out.println(">>> ALERTĂ: CERERE PRIMITĂ!");
        adminRepo.alocaArtistLaShow(comediantId, showId);
        return "redirect:/admin";
    }
}