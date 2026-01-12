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

    // Ruta: /admin
    @GetMapping
    public String adminDashboard(Model model, HttpSession session) {
        // Verificam daca e cineva logat in sesiune
        String user = (String) session.getAttribute("utilizatorLogat");

        // Daca sesiunea e goala sau userul nu este "admin" nu are acces
        if (user == null || !user.equalsIgnoreCase("admin")) {
            return "redirect:/login"; // Îl trimite la login dacă încearcă să "fenteze" URL-ul
        }

        model.addAttribute("totalArtisti", comediantRepo.findAll().size());
        model.addAttribute("totalSpectacole", showRepo.findAll().size());
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

    // Ruta: /admin/spectacole/salveaza
    @PostMapping("/spectacole/salveaza")
    public String salveazaShow(@RequestParam String titlu, @RequestParam String data,
                               @RequestParam Integer bilete, @RequestParam Integer pret,
                               HttpSession session) {
        if (!isUserAdmin(session)) return "redirect:/login";

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
}