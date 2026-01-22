package com.example.stand_up_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Repository pentru gestionarea rezervărilor.
 * Demonstrează operații de INSERT și interogări complexe cu JOIN.
 */
@Repository
public class RezervareRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean creeazaRezervare(int clientId, int showId, int nrBilete) {
        try {

            String sqlInsert = "INSERT INTO Rezervari (ClientID, ShowID, NrBilete) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsert, clientId, showId, nrBilete);

            // Scadem nr bilete din tabela Showuri

            String sqlUpdate = "UPDATE Showuri SET NrBilete = NrBilete - ? WHERE ShowID = ?";
            jdbcTemplate.update(sqlUpdate, nrBilete, showId);

            System.out.println(">>> Rezervare reușită și stoc actualizat!");
            return true;
        } catch (Exception e) {
            System.err.println("EROARE LA REZERVARE: " + e.getMessage());
            throw new RuntimeException("Eroare baza de date: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getRezervariUtilizator(int clientId) {
        String sql = "SELECT R.RezervareID, S.Titlu, S.Pret, R.NrBilete, R.DataRezervare " +
                "FROM Rezervari R " +
                "JOIN Showuri S ON R.ShowID = S.ShowID " +
                "WHERE R.ClientId = ? " +
                "ORDER BY R.DataRezervare DESC";
        return jdbcTemplate.queryForList(sql, clientId);
    }

    public List<Map<String, Object>> getRezervariPesteMedie() {
        String sql = "SELECT * FROM Rezervari " +
                "WHERE NrBilete > (SELECT AVG(NrBilete) FROM Rezervari)";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getBileteleMele(int clientId) {
        String sql = "SELECT R.RezervareID, S.Titlu, S.Pret AS PretUnitar, R.NrBilete, (R.NrBilete * S.Pret) AS PretTotal, " +
                "CASE WHEN P.PlataID IS NOT NULL THEN 'PLATIT' ELSE 'NEPLATIT' END AS Status " +
                "FROM Rezervari R " +
                "JOIN Showuri S ON R.ShowID = S.ShowID " +
                "LEFT JOIN Plati P ON R.RezervareID = P.RezervareID " +
                "WHERE R.ClientID = ? " +
                "ORDER BY R.RezervareID DESC";

        return jdbcTemplate.queryForList(sql, clientId);
    }


    public void platesteRezervarea(int rezervareId, double suma, Integer clientId) {
        String sqlPlata = "INSERT INTO Plati (RezervareID, SumaPlata, Metoda) VALUES (?, ?, 'Card')";

        try {
            jdbcTemplate.update(sqlPlata, rezervareId, suma);
            System.out.println(">>> Plata înregistrată în DB!");
        } catch (Exception e) {
            System.err.println("EROARE SQL LA PLATĂ: " + e.getMessage());
            throw e;
        }
    }
}