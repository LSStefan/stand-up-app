package com.example.stand_up_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //top cei mai buni comedianti dupa nr de bilete vandute
    public List<Map<String, Object>> getTopArtisti() {
        String sql = "SELECT X.Nume, X.TotalBilete FROM (" +
                "  SELECT A.NumeScena AS Nume, SUM(R.NrBilete) AS TotalBilete " +
                "  FROM Comedianti A " +
                "  JOIN [Comediant-Show] CS ON A.ComediantID = CS.ComediantID " +
                "  JOIN Showuri S ON CS.ShowID = S.ShowID " +
                "  JOIN Rezervari R ON S.ShowID = R.ShowID " +
                "  GROUP BY A.NumeScena" +
                ") AS X WHERE X.TotalBilete > 0 ORDER BY X.TotalBilete DESC";
        return jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> getJurnalCompletVanzari() {
        String sql = "SELECT C.Username, A.NumeScena AS Artist, S.Titlu, P.SumaPlata, P.Metoda " +
                "FROM Plati P " +
                "JOIN Rezervari R ON P.RezervareID = R.RezervareID " +
                "JOIN Clienti C ON R.ClientID = C.ClientId " +
                "JOIN Showuri S ON R.ShowID = S.ShowID " +
                "JOIN [Comediant-Show] CS ON S.ShowID = CS.ShowID " +
                "JOIN Comedianti A ON CS.ComediantID = A.ComediantID " +
                "ORDER BY P.SumaPlata DESC";
        return jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> getGradOcupare() {
        String sql = "SELECT S.Titlu, S.NrBilete AS Capacitate, " +
                "(SELECT ISNULL(SUM(Rez.NrBilete), 0) FROM Rezervari Rez WHERE Rez.ShowID = S.ShowID) AS Vandute " +
                "FROM Showuri S";
        return jdbcTemplate.queryForList(sql);
    }

    //afiseaza clientii cu bani cheltuiti > average client

    public List<Map<String, Object>> getClientiPremium() {
        String sql = "SELECT Cl.Username, SUM(Pl.SumaPlata) AS Total " +
                "FROM Clienti Cl " +
                "JOIN Rezervari Re ON Cl.ClientId = Re.ClientID " +
                "JOIN Plati Pl ON Re.RezervareID = Pl.RezervareID " +
                "GROUP BY Cl.Username " +
                "HAVING SUM(Pl.SumaPlata) > (SELECT AVG(SumaPlata) FROM Plati)";
        return jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> getShowuriFaraVanzari() {
        String sql = "SELECT S.Titlu, S.NrBilete FROM Showuri S " +
                "WHERE NOT EXISTS (SELECT 1 FROM Rezervari R WHERE R.ShowID = S.ShowID)";
        return jdbcTemplate.queryForList(sql);
    }


    public void alocaArtistLaShow(Integer comediantId, Integer showId) {
        // Folosim parantezele pătrate pentru numele tabelei cu cratimă
        String sql = "INSERT INTO [Comediant-Show] (ComediantID, ShowID) VALUES (?, ?)";
        jdbcTemplate.update(sql, comediantId, showId);
    }
}