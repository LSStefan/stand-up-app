package com.example.stand_up_app.repository;

import com.example.stand_up_app.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ShowRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Show> findAll() {
        String sql = "SELECT * FROM showuri";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Show s = new Show();
                // Debug: Printăm în consolă ce citim ca să vedem unde se oprește
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
            e.printStackTrace(); // Asta îți va arăta rândul exact în consolă
            return List.of(); // Returnăm o listă goală în caz de eroare, ca să nu crape toată pagina
        }
    }


    // Metoda pentru adăugare
    public int save(Show s) {
        String sql = "INSERT INTO showuri (Titlu, Data, NrBilete, Pret) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, s.getTitlu(), s.getData(), s.getNrBilete(), s.getPret());
    }

    public void deleteById(Integer id) {
        // 1. Ștergem plățile asociate rezervărilor acestui show
        String sqlPlati = "DELETE FROM Plati WHERE RezervareID IN (SELECT RezervareID FROM Rezervari WHERE ShowID = ?)";
        jdbcTemplate.update(sqlPlati, id);

        // 2. Ștergem rezervările acestui show
        String sqlRezervari = "DELETE FROM Rezervari WHERE ShowID = ?";
        jdbcTemplate.update(sqlRezervari, id);

        // 3. În sfârșit, ștergem show-ul
        String sqlShow = "DELETE FROM showuri WHERE ShowID = ?";
        jdbcTemplate.update(sqlShow, id);
    }


}