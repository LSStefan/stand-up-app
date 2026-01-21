package com.example.stand_up_app.repository; // Modifică cu pachetul tău

import com.example.stand_up_app.model.Comediant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository dedicat operatiunilor asupra datelor artistilor.
 * Gestioneaza stocarea, actualizarea si extragerea informatiilor despre
 * comedianti din baza de date, asigurand datele necesare pentru paginile de prezentare.
 * * @author Stefanita Lican
 * @version 10 Ianuarie 2026
 */

@Repository
public class ComediantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Metoda care aduce toți comediantii pentru pagina principala
    public List<Comediant> findAll() {
        String sql = "SELECT * FROM Comedianti";

        List<Comediant> lista = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comediant.class));

        System.out.println("--- DEBUG REPOSITORY ---");
        System.out.println("Număr de rânduri găsite în DB: " + lista.size());

        for (Comediant c : lista) {
            System.out.println("Artist: " + c.getNumeScena() + " | Stil: " + c.getStil());
        }
        System.out.println("-------------------------");

        return lista;
    }

    // Metoda care cauta un singur artist pentru pagina de detalii
    public Comediant findById(Long id) {
        String sql = "SELECT * FROM Comedianti WHERE ComediantId = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Comediant c = new Comediant();
                c.setComediantId(rs.getLong("ComediantId"));
                c.setNumeScena(rs.getString("NumeScena"));
                c.setStil(rs.getString("Stil"));
                c.setImagineUrl(rs.getString("ImagineUrl"));
                c.setNume(rs.getString("Nume"));
                c.setPrenume(rs.getString("Prenume"));
                c.setDescriere(rs.getString("Descriere"));
                return c;
            }, id);
        } catch (Exception e) {
            System.out.println("EROARE JDBC: Nu s-a găsit artistul cu ID " + id);
            return null;
        }
    }

    public void save(Comediant c) {
        String sql = "INSERT INTO Comedianti (Nume, Prenume, NumeScena, Stil, ImagineUrl, Descriere) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                c.getNume(),
                c.getPrenume(),
                c.getNumeScena(),
                c.getStil(),
                c.getImagineUrl(),
                c.getDescriere() // <--- Noua coloană
        );
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM Comedianti WHERE ComediantID = ?";
        jdbcTemplate.update(sql, id);
    }
}