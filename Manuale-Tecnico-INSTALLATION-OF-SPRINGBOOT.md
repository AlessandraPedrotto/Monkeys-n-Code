# Progetto Spring Boot: Prj_LBP_MONKEYSNCODE

Questo progetto è un'applicazione Spring Boot progettata per dimostrare l'integrazione di vari componenti di Spring, come Spring Data JPA, Thymeleaf, OAuth2 e altro. Utilizza MariaDB come database e supporta l'autenticazione tramite Google OAuth2.

## Indice

1. [Prerequisiti](#prerequisiti)
2. [Installazione](#installazione)
   - [Passo 1: Installa Java](#passo-1-installa-java)
   - [Passo 2: Installa Maven](#passo-2-installa-maven)
3. [Configurazione delle Application Properties](#configurazione-delle-application-properties)
4. [Dipendenze del progetto](#dipendenze-del-progetto)
5. [File pom.xml](#file-pomxml)
6. [Esecuzione dell'Applicazione](#esecuzione-dellapplicazione)
7. [Struttura del Progetto](#struttura-del-progetto)

---

## 1. Prerequisiti

- **Java Development Kit (JDK)** 8 o superiore
- **Apache Maven** 3.6 o superiore
- **MariaDB** installato e configurato
- **Google OAuth2** configurato per l'autenticazione (client ID e client secret)

## 2. Installazione

### Passo 1: Installa Java

1. Scarica il JDK dal [sito di Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) o da [AdoptOpenJDK](https://adoptopenjdk.net/).
2. Segui le istruzioni di installazione per il tuo sistema operativo.
3. Imposta la variabile d'ambiente `JAVA_HOME` per puntare alla tua installazione del JDK.

### Passo 2: Installa Maven

1. Scarica Maven dal [sito ufficiale](https://maven.apache.org/download.cgi).
2. Segui le istruzioni di installazione per il tuo sistema operativo.
3. Verifica l'installazione di Maven eseguendo:
   bash
   mvn -v

## 3. Configurazione delle Application Properties

- Il file application.properties contiene la configurazione per connettere l'applicazione al database e per gestire l'autenticazione tramite Google OAuth2. Ecco una panoramica delle proprietà configurate: properties

    # Nome dell'applicazione

    - spring.application.name=Prj_LBP_MONKEYSNCODE

    # Configurazione del database MariaDB
    - spring.datasource.username=root
    - spring.datasource.password=
    - spring.jpa.hibernate.ddl-auto=update
    - spring.jpa.show-sql=true
    - spring.sql.init.mode=always

    # Configurazione del server
    - server.port=8080

    # Configurazione di Thymeleaf per il rendering dei template HTML
    - spring.thymeleaf.prefix=classpath:/templates/
    - spring.thymeleaf.suffix=.html

    # Configurazione Google OAuth2 per l'autenticazione
    - spring.security.oauth2.client.registration.google.client-id=811839220245-e24nd5vuto8ecgbngio0fdsqm2tv41kt.apps.googleusercontent.com
    - spring.security.oauth2.client.registration.google.client-secret=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
    - spring.security.oauth2.client.registration.google.scope=profile,email

    # Livello di logging per il debugging di sicurezza
    - logging.level.org.springframework.security=TRACE

## 4. Dipendenze del progetto

1. **Spring Boot DevTools**

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>

Uso: Facilita lo sviluppo locale con funzionalità come il caricamento automatico delle modifiche e la disattivazione del cache per le risorse statiche.

2. **MariaDB JDBC Driver**
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <scope>runtime</scope>
</dependency>

Uso: Consente alla tua applicazione di connettersi a un database MariaDB, fornendo le classi necessarie per la comunicazione tramite JDBC.

3. **Spring Data JPA**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

Uso: Fornisce un'implementazione semplificata per l'accesso ai dati e la gestione delle entità tramite Java Persistence API (JPA).

4. **Spring Boot Starter FreeMarker**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>

Uso: Abilita il motore di templating FreeMarker per il rendering di viste dinamiche in HTML.

5. **Spring Boot Starter Thymeleaf**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

Uso: Integra Thymeleaf, un motore di template moderni e molto utilizzati per generare contenuti web dinamici.

6. **Spring Boot Starter OAuth2 Client**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

Uso: Fornisce il supporto per l'autenticazione e l'autorizzazione tramite OAuth2, consentendo l'integrazione con provider di autenticazione come Google.

7. **Spring Boot Starter Web**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

Uso: Include tutto il necessario per sviluppare applicazioni web, come controller REST, JSON e servizi HTTP.

8. **Spring Boot Starter AOP**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

Uso: Supporta la programmazione orientata agli aspetti (AOP), consentendo di separare le preoccupazioni come logging, sicurezza e gestione delle transazioni.

9. **Spring Boot Starter Test**
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

Uso: Include librerie per testare le applicazioni Spring Boot, come JUnit e Mockito, per facilitare il testing unitario e di integrazione.

10. **Spring Security Test**
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>

Uso: Fornisce strumenti per testare le funzionalità di sicurezza nelle applicazioni Spring, rendendo più semplice la verifica delle configurazioni di sicurezza.

## 4. File pom.xml

 - Il file pom.xml gestisce le dipendenze di Maven, specificando le librerie e i framework necessari per far funzionare correttamente l'applicazione Spring Boot. Di seguito sono riportate le principali dipendenze incluse:

 - Spring Boot DevTools: Fornisce strumenti di sviluppo aggiuntivi come il ricaricamento automatico delle modifiche al codice e la disattivazione della cache per le risorse statiche, migliorando la velocità di sviluppo.

 - MariaDB Java Client: Consente la connessione dell'applicazione al database MariaDB per la persistenza dei dati.

 - Spring Boot Starter Data JPA: Offre un'integrazione con Spring Data JPA e Hibernate per la gestione della persistenza dei dati tramite ORM (Object Relational Mapping).

 - Spring Boot Starter FreeMarker: Integra il motore di template FreeMarker, utile per la generazione di viste HTML dinamiche.

 - Spring Boot Starter Thymeleaf: Aggiunge il supporto per il motore di template Thymeleaf, usato per il rendering di template HTML.

 - Spring Boot Starter OAuth2 Client: Abilita l'autenticazione tramite OAuth2, in particolare per il login tramite Google, migliorando la sicurezza e semplificando la gestione dell'accesso degli utenti.

 - Spring Boot Starter Web: Rende l'applicazione adatta a gestire richieste HTTP e a sviluppare API REST.

 - Spring Boot Starter AOP: Abilita la Programmazione Orientata agli Aspetti (AOP), utile per la gestione di aspetti trasversali come il logging e la gestione delle eccezioni.

 - Spring Boot Starter Test: Fornisce una suite di strumenti per i test, tra cui JUnit e Mockito, per la scrittura e l'esecuzione di test unitari e di integrazione.

 - Spring Security Test: Facilita il testing delle funzionalità di sicurezza, consentendo di verificare l'autenticazione e l'autorizzazione nei test.


## 6. Esecuzione dell'Applicazione

Per avviare l'applicazione, assicurati che tutti i prerequisiti siano stati soddisfatti, come la configurazione del database MariaDB e delle credenziali Google OAuth2.

### Passi per l'esecuzione:

1. **Compilazione del progetto**:
   Prima di eseguire l'applicazione, assicurati che tutte le dipendenze siano scaricate e aggiornate. Esegui il seguente comando Maven dalla radice del progetto per compilare:

2. **Avvio dell'applicazione**: 
Una volta completata la compilazione, avvia l'applicazione usando "Run As" Spring Boot App
Questo avvierà l'applicazione sul server locale (porta predefinita: 8080 come specificato nel file application.properties).

3. **Verifica dell'esecuzione**: 
Apri il browser e naviga all'indirizzo http://localhost:8080 per vedere l'applicazione in esecuzione. L'autenticazione tramite Google OAuth2 sarà richiesta per accedere a determinate sezioni dell'applicazione.

## 7. Struttura del Progetto

Il progetto è organizzato seguendo una struttura a pacchetti per mantenere un'organizzazione chiara e modulare delle funzionalità.

Ecco una panoramica dei principali pacchetti:

1. Pacchetto application

Contiene la classe principale di avvio dell'applicazione:

    package com.monkeysncode;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.scheduling.annotation.EnableAsync;

    @SpringBootApplication
    @EnableAsync
    public class PrjLbpMonkeysncodeApplication {
        public static void main(String[] args) {
            SpringApplication.run(PrjLbpMonkeysncodeApplication.class, args);
        }
    }

La classe PrjLbpMonkeysncodeApplication è annotata con @SpringBootApplication per configurare automaticamente l'applicazione Spring Boot.
La configurazione @EnableAsync abilita l'uso di metodi asincroni nel progetto.

2.  Pacchetto config

Contiene le configurazioni dell'applicazione, come:

AsyncConfig: Configura i metodi asincroni per gestire le operazioni non bloccanti.
SecurityConfig: Configura la sicurezza dellapplicazione, come l'autenticazione tramite Google OAuth2.

3. Pacchetto controller
Contiene tutti i controller REST o MVC, che gestiscono le richieste HTTP in arrivo e instradano le risposte ai client. I controller definiscono i vari endpoint dell'applicazione e gestiscono le richieste di visualizzazione o di dati.

4. Pacchetto entities
Contiene tutte le entità dell'applicazione, che rappresentano le tabelle nel database MariaDB. Le classi in questo pacchetto sono annotate con @Entity e mappano le strutture dei dati con il database tramite JPA.

5. Pacchetto repos
Contiene i Data Access Objects (DAO), che rappresentano i repository per la gestione dei dati persistenti nel database. Le interfacce di questo pacchetto estendono JpaRepository per semplificare l'accesso ai dati e le operazioni CRUD.

6. Pacchetto services
Contiene le classi di servizio che implementano la logica aziendale dell'applicazione. I servizi espongono metodi per le operazioni principali utilizzate dai controller, fornendo un'astrazione rispetto alla gestione diretta dei dati nei repository.

7. Risorse statiche e template
Static: Contiene le immagini e le risorse statiche dell'applicazione.
Templates: Contiene i file HTML per il frontend dell'applicazione, gestiti tramite i motori di template FreeMarker e Thymeleaf.

8. Questa è la rappresentazione grafica del progetto

    src/
    ├── main/
    │   ├── java/com/monkeysncode/
    │   │   ├── application/                     # Classe principale di avvio dell'applicazione
    │   │   ├── config/                          # Configurazioni per async e sicurezza
    │   │   ├── controller/                      # Controller dell'applicazione
    │   │   ├── entities/                        # Classi delle entità del database
    │   │   ├── repos/                           # Interfacce di accesso al database (DAO)
    │   │   └── services/                        # Servizi con la logica di business
    │   ├── resources/
    │   │   ├── static/                          # File statici (es. immagini)
    │   │   ├── templates/                       # Template Thymeleaf (HTML)
    │   │   └── application.properties           # File di configurazione dell'applicazione
    └── pom.xml                                  # Configurazione del progetto e dipendenze
