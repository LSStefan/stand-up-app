# Stand-Up NOW - Sistem de Gestiune Evenimente

Stand-Up NOW este o aplicație web completă dezvoltată în **Spring Boot** destinată managementului spectacolelor de comedie. Aplicația permite administrarea artiștilor, a evenimentelor și procesarea rezervărilor de bilete în timp real.

## 🚀 Tehnologii Utilizate

* **Backend:** Java 23, Spring Boot 3.5.9
* **Acces Date:** Spring JDBC (JdbcTemplate)
* **Bază de date:** Microsoft SQL Server (MSSQL)
* **Frontend:** Thymeleaf, Bootstrap 5, HTML5, CSS3
* **Securitate:** SQL Injection Prevention (Prepared Statements), Sesiuni HTTP

## 📋 Funcționalități Principale

### Administrare (Panou de Control)
* **Management Artiști:** CRUD complet (Creare, Citire, Actualizare, Ștergere) pentru comedianti, inclusiv gestionarea biografiilor.
* **Management Spectacole:** Adăugarea evenimentelor, setarea prețurilor și a capacității sălii.
* **Alocare Artiști:** Sistem de legătură Many-to-Many între artiști și spectacole.
* **Dashboard Statistici:** Analiză avansată a datelor prin interogări complexe.

### Utilizatori / Clienți
* **Vizualizare Evenimente:** Listă dinamică a spectacolelor disponibile.
* **Profil Artist:** Pagini dedicate cu descrieri preluate din baza de date.
* **Rezervare Bilete:** Proces automatizat de achiziție cu scăderea stocului în timp real.

## 📊 Analiza Datelor (SQL Business Intelligence)

Proiectul include interogări complexe pentru monitorizarea afacerii:
1.  **Top Artiști:** Clasament bazat pe volumul de bilete vândute.
2.  **Jurnal Vânzări:** Raport detaliat care conectează 5 tabele (Plăți, Rezervări, Clienți, Show-uri, Artiști).
3.  **Clienți Premium:** Identificarea utilizatorilor cu cheltuieli peste media sistemului.
4.  **Grad de Ocupare:** Calculul procentual al biletelor vândute față de capacitatea sălii.




## 🔒 Securitate

Aplicația implementează cele mai bune practici de securitate la nivel de bază de date prin utilizarea **parametrilor variabili (`?`)**, eliminând riscul de SQL Injection:

```java
String sql = "UPDATE Showuri SET NrBilete = NrBilete - ? WHERE ShowID = ?";
jdbcTemplate.update(sql, nrBilete, idShow);