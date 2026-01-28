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


<img width="1917" height="1032" alt="Screenshot 2026-01-29 012353" src="https://github.com/user-attachments/assets/1cc7705c-9097-47d7-bea3-2fa34deeb79a" />
<img width="1919" height="1012" alt="Screenshot 2026-01-29 012432" src="https://github.com/user-attachments/assets/29056744-ba9a-4438-af59-26dd488f0ace" />
<img width="1919" height="1039" alt="Screenshot 2026-01-29 012448" src="https://github.com/user-attachments/assets/c97f18b8-9032-4d7d-aecc-f0a18cdff6c1" />
<img width="1919" height="1043" alt="Screenshot 2026-01-29 012536" src="https://github.com/user-attachments/assets/27a34929-88b7-456b-8b57-4f1249131f8c" />
<img width="1919" height="958" alt="Screenshot 2026-01-29 012556" src="https://github.com/user-attachments/assets/e21da4fd-264f-4ba8-b459-77b3a4b0e1d7" />
<img width="1919" height="1036" alt="Screenshot 2026-01-29 012652" src="https://github.com/user-attachments/assets/fc54611e-fb96-49d6<img width="1919" height="1023" alt="Screenshot 2026-01-29 012713" src="https://github.com/user-attachments/assets/86e3d348-3f19-492d-95ec-b7883c50adc5" />
-908b-24d5b493d830" />
<img width="1919" height="962" alt="Screenshot 2026-01-29 012753" src="https://github.com/user-attachments/assets/7d999c07-90ad-487b-88b4-b0ef3b0c3960" />
