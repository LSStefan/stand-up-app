package com.example.stand_up_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.stand_up_app.model.Utilizator;

import java.util.List;


/**
 * Repository pentru gestionarea persistentei datelor utilizatorilor (tabelul Clienti).
 * Ofera metode pentru validarea autentificarii, inregistrarea de noi conturi
 * si interogarea listei complete de membri ai platformei.
 * * @author Stefanita Lican
 *
 * @version 9 Ianuarie 2026
 */

@Repository
public class UtilizatorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean verificaLogin(String username, String parola) {
        String sql = "SELECT COUNT(*) FROM Clienti WHERE username = ? AND parola = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, parola);
        return count != null && count > 0;
    }

    public int salveazaUtilizator(String nume, String prenume, String email, String telefon, String username, String parola) {
        String sql = "INSERT INTO Clienti (nume, prenume, email, telefon, username, parola) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, nume, prenume, email, telefon, username, parola);
    }

    public List<Utilizator> findAll() {
        String sql = "SELECT * FROM Clienti";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Utilizator.class));
    }

    public Utilizator gasesteDupaUsername(String username) {
        String sql = "SELECT * FROM Clienti WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Utilizator.class), username);
        } catch (Exception e) {
            return null; // Returnăm null dacă nu găsim userul
        }
    }

    // 1. Interogare cu JOIN (Simple) - Total bani cheltuiți
    public Double getTotalCheltuit(String username) {
        // JOIN între Clienti (ClientId), Rezervari (ClientId) și Plati (RezervareID)
        // Am pus SumaPlata pentru că așa apare în poza ta în tabela Plati
        String sql = "SELECT SUM(P.SumaPlata) " +
                "FROM Clienti C " +
                "JOIN Rezervari R ON C.ClientId = R.ClientId " +
                "JOIN Plati P ON R.RezervareID = P.RezervareID " +
                "WHERE C.username = ?";
        Double rezultat = jdbcTemplate.queryForObject(sql, Double.class, username);
        return rezultat != null ? rezultat : 0.0;
    }

    // 2. Interogare cu Subcerere (Complexă) - Verifică status VIP
    public boolean esteUtilizatorVIP(String username) {
        // Folosim ClientId peste tot conform diagramei
        String sql = "SELECT COUNT(*) FROM Clienti C " +
                "WHERE C.username = ? AND " +
                "(SELECT SUM(NrBilete) FROM Rezervari WHERE ClientId = C.ClientId) > " +
                "(SELECT AVG(TotalBilete) FROM (SELECT SUM(NrBilete) as TotalBilete FROM Rezervari GROUP BY ClientId) AS Media)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    // 3. Interogare cu JOIN (Simple) - Număr spectacole la care are rezervări
    public Integer getNumarSpectacole(String username) {
        // Folosim NrBilete (din tabela Rezervari) și ClientId
        String sql = "SELECT COUNT(R.RezervareID) " +
                "FROM Rezervari R " +
                "JOIN Clienti C ON R.ClientId = C.ClientId " +
                "WHERE C.username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null ? count : 0;
    }

    public int actualizeazaProfil(int clientId, String nume, String prenume, String email, String telefon) {
        String sql = "UPDATE Clienti SET Nume = ?, Prenume = ?, Email = ?, Telefon = ? WHERE ClientId = ?";
        return jdbcTemplate.update(sql, nume, prenume, email, telefon, clientId);
    }

}