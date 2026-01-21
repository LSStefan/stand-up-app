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
        // Varianta compatibilă cu versiuni vechi de SQL Server (2014/2016)
        String sql = "SELECT S.*, " +
                "STUFF((SELECT ', ' + A.NumeScena " +
                "       FROM Comedianti A " +
                "       JOIN [Comediant-Show] CS ON A.ComediantID = CS.ComediantID " +
                "       WHERE CS.ShowID = S.ShowID " +
                "       FOR XML PATH('')), 1, 2, '') AS NumeArtisti " +
                "FROM showuri S";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Show s = new Show();
                s.setShowID(rs.getInt("ShowID"));
                s.setTitlu(rs.getString("Titlu"));
                s.setData(rs.getDate("Data"));
                s.setNrBilete(rs.getInt("NrBilete"));
                s.setPret(rs.getInt("Pret"));
                s.setImagineUrl(rs.getString("ImagineUrl"));

                // Mapăm coloana virtuală NumeArtisti
                String artisti = rs.getString("NumeArtisti");
                s.setNumeArtisti(artisti != null ? artisti : "Line-up în curs de confirmare");

                return s;
            });
        } catch (Exception e) {
            System.err.println("EROARE la findAll: " + e.getMessage());
            return List.of();
        }
    }


    // Metoda actualizată pentru a include și imaginea
    public int save(Show s) {
        // Am adăugat ImagineUrl în lista de coloane și un al 5-lea semn de întrebare
        String sql = "INSERT INTO showuri (Titlu, Data, NrBilete, Pret, ImagineUrl) VALUES (?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                s.getTitlu(),
                s.getData(),
                s.getNrBilete(),
                s.getPret(),
                s.getImagineUrl() // <--- Noua valoare trimisă către DB
        );
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