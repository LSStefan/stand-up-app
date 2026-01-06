package com.example.stand_up_app.repository; // Modifică cu pachetul tău

import com.example.stand_up_app.model.Comediant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ComediantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Metoda care aduce toți comediantii pentru pagina principală
    public List<Comediant> findAll() {
        String sql = "SELECT * FROM Comedianti";

        List<Comediant> lista = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comediant.class));

        // PRINT ÎN CONSOLĂ
        System.out.println("--- DEBUG REPOSITORY ---");
        System.out.println("Număr de rânduri găsite în DB: " + lista.size());

        for (Comediant c : lista) {
            System.out.println("Artist: " + c.getNumeScena() + " | Stil: " + c.getStil());
        }
        System.out.println("-------------------------");

        return lista;
    }

    // Metoda care caută un singur artist pentru pagina de detalii
    public Comediant findById(Long id) {
        String sql = "SELECT * FROM Comedianti WHERE ComediantId = ?";

        return jdbcTemplate.queryForObject(sql,
                new BeanPropertyRowMapper<>(Comediant.class), id);
    }
}