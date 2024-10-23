# SecurityConfig

## Descrizione
La classe `SecurityConfig` è una configurazione per la sicurezza dell'applicazione, che utilizza Spring Security. Questa classe si occupa della gestione dell'autenticazione degli utenti, della protezione delle risorse, nonché dell'integrazione con i login OAuth2. Implementa misure di sicurezza per garantire che solo gli utenti autorizzati possano accedere a determinate parti dell'applicazione.

## Funzionalità Principali

### 1. Configurazione della Sicurezza

- **Protezione delle Risorse**:
  - La classe definisce regole di accesso per diverse URL. Ad esempio:
    - Le seguenti risorse sono accessibili a tutti senza autenticazione: 
      - Home page (`/`)
      - Pagina di login (`/login`)
      - Pagina di registrazione (`/register`)
      - Risorse statiche (`/style/**`, `/script/**`, `/image/**`, `/traduzioni/**`).
    - Le risorse sotto il percorso `/admin/**` sono accessibili solo agli utenti con il ruolo `ADMIN`.
    - Tutte le altre richieste richiedono autenticazione.

### 2. Gestione del Login

- **Login tramite Form**:
  - La classe configura una pagina di login personalizzata attraverso la quale gli utenti possono inserire le proprie credenziali (email e password).
  - In caso di successo, gli utenti vengono reindirizzati alla home page (`defaultSuccessUrl("/")`).
  - Il metodo `loginProcessingUrl("/login")` definisce l'URL tramite il quale vengono inviate le credenziali.
  - Gli attributi `usernameParameter("email")` e `passwordParameter("password")` specificano i nomi dei parametri da utilizzare nel modulo di accesso.

### 3. Integrazione OAuth2

- **Login OAuth2**:
  - Consente agli utenti di autenticarsi utilizzando google come provider esterno.
  - Specifica una pagina di login personalizzata e gestisce il successo dell'autenticazione in `oAuth2AuthenticationSuccessHandler()`.
  - La classe si occupa di salvare o aggiornare le informazioni degli utenti una volta autenticati, all’interno della sessione.

### 4. Logout Personalizzato

- La configurazione del logout comprende:
  - **Logout Success Url**: reindirizza gli utenti alla home page con un parametro che indica il logout avvenuto.
  - **Invalidazione della Sessione**: in caso di logout, la sessione dell'utente viene invalidata e i cookie vengono eliminati, migliorando la sicurezza.
  - **Gestore del Logout**: attraverso `addLogoutHandler`, la classe aggiorna lo stato dell'utente portandolo a offline quando si disconnette.

### 5. Gestione dello Stato Online

- Al momento della connessione (`login`):
  - Gli utenti vengono contrassegnati come "online", aggiornando il loro stato nel database.
- Al momento della disconnessione (`logout`):
  - L'utente viene contrassegnato come "offline", aggiornando nuovamente il suo stato nel database.

## Componenti Chiave

- **Password Encoder**:
  - La classe utilizza `BCryptPasswordEncoder` per cifrare le password degli utenti, aumentando la sicurezza delle informazioni sensibili.

- **Success Handlers**:
  La classe implementa due gestori di successo per la gestione dell'autenticazione:

  - **customAuthenticationSuccessHandler**:
    - È utilizzato per il login tramite form.
    - Recupera i dettagli dell'utente autenticato, come email e nome.
    - Imposta l'utente come "online" nel database e salva le informazioni necessarie nella sessione:
      - `name`: nome completo dell'utente.
      - `userId`: identificatore dell'utente.
      - `role`: ruoli di accesso dell'utente, ricavando le autorizzazioni associate.
    - Reindirizza gli utenti alla home page dopo un login riuscito.

  - **oAuth2AuthenticationSuccessHandler**:
    - È utilizzato per il login tramite OAuth2.
    - Recupera i dettagli dell’utente autenticato dal provider OAuth2.
    - Se l'utente è già registrato nel sistema, aggiorna il suo stato a "online" e salva i dati nel database.
    - Se l'utente non è registrato, crea un nuovo utente basato sulle informazioni ricevute dal provider OAuth2.
    - Imposta nella sessione le informazioni dell'utente:
      - `name`: nome dell'utente fornito dal provider OAuth2.
      - `userId` e `role`: recuperati per il corretto tracciamento e gestione degli accessi.
    - Reindirizza gli utenti alla home page dopo un accesso riuscito.

## Annotazioni Utilizzate

- `@Configuration`: Indica che la classe contiene configurazioni per il contesto di Spring.
- `@EnableWebSecurity`: Abilita la sicurezza web in Spring, attivando le protezioni necessarie per l'applicazione.

