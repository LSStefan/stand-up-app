package com.example.stand_up_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

/**
 * Punctul de intrare in aplicatia Stand-Up.
 * Configurat pentru SQL Server 2014 folosind JDBC Pur (fara ORM/JPA).
 */
@SpringBootApplication(exclude = {
		HibernateJpaAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		JdbcRepositoriesAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class
})
public class StandUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(StandUpApplication.class, args);
		System.out.println("\n>>> Aplicatia Stand-Up a pornit cu succes!");
		System.out.println(">>> Acceseaza in browser: http://localhost:8080/login\n");
	}
}