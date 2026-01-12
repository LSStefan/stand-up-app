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
}