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
            // 1. Inserăm în tabela Rezervari (exact cum ai tu acum)
            String sqlInsert = "INSERT INTO Rezervari (ClientID, ShowID, NrBilete) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsert, clientId, showId, nrBilete);

            // 2. Scădem biletele din tabela Showuri
            // ATENȚIE: Verifică dacă tabela se numește "Showuri" și coloanele sunt "NrBilete" și "ShowID"
            String sqlUpdate = "UPDATE Showuri SET NrBilete = NrBilete - ? WHERE ShowID = ?";
            jdbcTemplate.update(sqlUpdate, nrBilete, showId);

            System.out.println(">>> Rezervare reușită și stoc actualizat!");
            return true;
        } catch (Exception e) {
            System.err.println("EROARE LA REZERVARE: " + e.getMessage());
            // Aruncăm o excepție pentru ca @Transactional să facă ROLLBACK dacă apare o eroare
            throw new RuntimeException("Eroare baza de date: " + e.getMessage());
        }
    }

    // Interogare SIMPLĂ (JOIN între Rezervări și Showuri)
    // Cerința 4: Minim 6 interogări simple
    public List<Map<String, Object>> getRezervariUtilizator(int clientId) {
        String sql = "SELECT R.RezervareID, S.Titlu, S.Pret, R.NrBilete, R.DataRezervare " +
                "FROM Rezervari R " +
                "JOIN Showuri S ON R.ShowID = S.ShowID " +
                "WHERE R.ClientId = ? " +
                "ORDER BY R.DataRezervare DESC";
        return jdbcTemplate.queryForList(sql, clientId);
    }

    // Interogare COMPLEXĂ (Subcerere)
    // Cerința 5: Minim 4 interogări complexe
    // Găsește rezervările care au un număr de bilete mai mare decât media tuturor rezervărilor
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
        // Folosim DOAR coloanele care apar în Screenshot (70): RezervareID, SumaPlata, Metoda
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