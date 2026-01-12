package com.example.stand_up_app.repository;

import com.example.stand_up_app.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Repository responsabil pentru managementul evenimentelor (spectacolelor).
 * Realizeaza operatiuni de salvare, stergere si listare a show-urilor,
 * facilitand organizarea acestora in functie de data si disponibilitate.
 * * @author Stefanita Lican
 * @version 10 Ianuarie 2026
 */

@Repository
public class ShowRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Show> findAll() {
        String sql = "SELECT * FROM showuri";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Show s = new Show();
                // Debug
                System.out.println("Citesc show-ul: " + rs.getString("Titlu"));

                s.setShowID(rs.getInt("ShowID"));
                s.setTitlu(rs.getString("Titlu"));
                s.setData(rs.getDate("Data"));
                s.setNrBilete(rs.getInt("NrBilete"));
                s.setPret(rs.getInt("Pret"));
                return s;
            });
        } catch (Exception e) {
            System.err.println("EROARE CRITICĂ la findAll Spectacole: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Returnam o lista goala în caz de eroare, ca sa nu crape toata pagina
        }
    }


    // Metoda pentru adăugare
    public int save(Show s) {
        String sql = "INSERT INTO showuri (Titlu, Data, NrBilete, Pret) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, s.getTitlu(), s.getData(), s.getNrBilete(), s.getPret());
    }

    public void deleteById(Integer id) {
        String sqlPlati = "DELETE FROM Plati WHERE RezervareID IN (SELECT RezervareID FROM Rezervari WHERE ShowID = ?)";
        jdbcTemplate.update(sqlPlati, id);

        String sqlRezervari = "DELETE FROM Rezervari WHERE ShowID = ?";
        jdbcTemplate.update(sqlRezervari, id);

        // Sterge showul
        String sqlShow = "DELETE FROM showuri WHERE ShowID = ?";
        jdbcTemplate.update(sqlShow, id);
    }


}