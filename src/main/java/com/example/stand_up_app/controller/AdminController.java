package com.example.stand_up_app.controller;

import com.example.stand_up_app.model.Comediant;
import com.example.stand_up_app.model.Show;
import com.example.stand_up_app.model.Utilizator;
import com.example.stand_up_app.repository.ComediantRepository;
import com.example.stand_up_app.repository.ShowRepository;
import com.example.stand_up_app.repository.UtilizatorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin") // Toate rutele din acest controller vor începe cu /admin
public class AdminController {

    @Autowired
    private ComediantRepository comediantRepo;

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private UtilizatorRepository utilizatorRepo;

    // Ruta: /admin
    @GetMapping
    public String adminDashboard(Model model, HttpSession session) {
        // Verificăm dacă există cineva logat în sesiune
        String user = (String) session.getAttribute("utilizatorLogat");

        // Dacă sesiunea e goală SAU userul nu este "admin", dă-l afară!
        if (user == null || !user.equalsIgnoreCase("admin")) {
            return "redirect:/login"; // Îl trimite la login dacă încearcă să "fenteze" URL-ul
        }

        model.addAttribute("totalArtisti", comediantRepo.findAll().size());
        model.addAttribute("totalSpectacole", showRepo.findAll().size());
        return "admin";
    }

    // Ruta: /admin/spectacole
    @GetMapping("/spectacole")
    public String gestionareSpectacole(Model model, HttpSession session) {


        model.addAttribute("lista", showRepo.findAll());
        return "admin_spectacole";
    }

    // Ruta: /admin/spectacole/salveaza
    @PostMapping("/spectacole/salveaza")
    public String salveazaShow(@RequestParam String titlu, @RequestParam String data,
                               @RequestParam Integer bilete, @RequestParam Integer pret) {
        Show s = new Show();
        s.setTitlu(titlu);
        s.setData(java.sql.Date.valueOf(data));
        s.setNrBilete(bilete);
        s.setPret(pret);
        showRepo.save(s);
        return "redirect:/admin/spectacole";
    }

    // Ruta: /admin/spectacole/sterge/{id}
    @GetMapping("/spectacole/sterge/{id}")
    public String stergeShow(@PathVariable Integer id) {
        showRepo.deleteById(id);
        return "redirect:/admin/spectacole";
    }

    // Metodă utilitară privată pentru a nu repeta codul de verificare
    private boolean isUserAdmin(HttpSession session) {
        String user = (String) session.getAttribute("utilizatorLogat");
        return user != null && user.equalsIgnoreCase("admin");
    }


    // Afișarea listei de artiști
    @GetMapping("/artisti")
    public String gestionareArtisti(Model model, HttpSession session) {

        model.addAttribute("listaArtisti", comediantRepo.findAll());
        return "admin_artisti";
    }

    // Salvare artist nou
    @PostMapping("/artisti/salveaza")
    public String salveazaArtist(@RequestParam String nume,
                                 @RequestParam String prenume,
                                 @RequestParam String numeScena,
                                 @RequestParam String stil,
                                 @RequestParam String urlPoza) {
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
    public String stergeArtist(@PathVariable Long id) {
        comediantRepo.deleteById(id);
        return "redirect:/admin/artisti";
    }


    @GetMapping("/utilizatori")
    public String listaUtilizatori(Model model, HttpSession session) {
        // 1. Verificăm dacă e logat admin-ul

        // 2. Luăm toți utilizatorii din baza de date
        List<Utilizator> toti = utilizatorRepo.findAll();

        // 3. Trimitem lista către HTML
        model.addAttribute("listaUtilizatori", toti);

        return "admin_useri";
    }
}