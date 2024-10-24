

# Entità del Progetto

## Card
L'entità **Card** si collega alla tabella `cards` del database e rappresenta tutte le carte presenti in gioco. Alcuni attributi, come **abilities** e **attacks**, possono contenere informazioni dettagliate, risultando quindi più lunghi rispetto ad altri campi.

---

## Deck
L'entità **Deck** si collega alla tabella `decks` del database. La chiave primaria è generata utilizzando la strategia di Hibernate. Di seguito sono elencati gli attributi principali:

- **valid**: boolean, indica se un deck è valido o meno.
- **user**: l'ID dello user che possiede il deck.
- **deckImg**: l'ID dell'immagine associata al deck.

---

## DeckCards
Entità relazionale tra **Deck** e **Card** per gestire la relazione N:N tra i due. Gli attributi principali sono:

- **card**: l'ID della carta inclusa nel deck.
- **deck**: l'ID del deck.
- **cardQuantity**: quantità di una certa carta presente nel deck.

---

## DeckImg
Entità collegata a **Deck** per gestire le immagini dei mazzi.

- **imgDeckPath**: il percorso dell'immagine del deck.

---

## Role
Entità che gestisce i ruoli degli utenti nel sistema. I ruoli predefiniti presenti nel database sono:

- `ROLE_ADMIN`
- `ROLE_USER`

---

## User
L'entità **User** rappresenta gli utenti registrati. Ecco gli attributi principali:

- **id**, **name**, **email**, **password**: attributi classici per l'utente.
- **win** e **lose**: gestiscono rispettivamente le vittorie e le sconfitte dell'utente nella classifica.
- **decks**: relazione 1:N con i deck posseduti dall'utente.
- **userImg**: relazione N:1 con **UserImg**, che rappresenta l'immagine del profilo dell'utente.
- **roles**: relazione N:N con i ruoli; viene creata una tabella relazionale tra **User** e **Role**, con `fetch = FetchType.EAGER` per caricare immediatamente i ruoli ed evitare ritardi che potrebbero causare errori di login.
- **following** e **followers**: due `HashSet` che gestiscono la relazione con la tabella `follows`, utile per gestire seguaci e seguiti.

---

## UserCards
Entità relazionale tra **User** e **Card** per gestire la relazione N:N. L'attributo principale è:

- **cardQuantity**: indica la quantità di una determinata carta posseduta dall'utente.

---

## UserImg
Entità collegata a **User** per gestire le immagini che un utente può scegliere come immagine del profilo.

---

# Repository del Progetto

Tutte le repository estenderanno `JpaRepository`, fornendo metodi per interagire con le tabelle nel database.

---

## CardsDAO - Repository per la gestione delle carte
- **findALL**: restituisce una lista di tutte le carte nel database.

---

## DeckCardDAO - Repository per la gestione delle carte nel mazzo
- **findByDeck**: recupera tutte le associazioni di DeckCards associate a un mazzo specificato. È utile per ottenere tutte le carte che appartengono a un particolare mazzo.
- **findByCardIdAndDeckId**: restituisce un oggetto `Optional<DeckCards>` che contiene l'associazione di DeckCards se presente; altrimenti, è vuoto.

---

## DeckDAO - Repository per la gestione dei mazzi
- **findByUserId**: questo metodo recupera tutti i mazzi associati a un determinato utente, utile per ottenere la lista dei mazzi creati o posseduti dall'utente.
- **findByUserAndNameDeck**: recupera un mazzo specifico associato a un utente in base al nome del mazzo, utile per verificare se un mazzo esiste già per un utente.

---

## DeckImgDAO - Repository per la gestione delle immagini dei mazzi
- **findAll**: recupera tutte le immagini dei mazzi presenti nella tabella `deck_img`, utile per ottenere un elenco completo delle immagini che possono essere associate ai mazzi.

---

## RoleDAO - Repository per la gestione dei ruoli
- Utilizza solo i metodi di JPA.

---

## UserCardDAO - Repository per la gestione delle carte degli utenti
- **findByUserId**: recupera tutte le associazioni di carte per un utente specifico, identificato dal suo ID.
- **findByUserAndCard**: cerca una specifica associazione di carta per un utente; se trovata, restituisce un oggetto `UserCards`.
- **countTotalCardsByUserId**: utilizza una query personalizzata per calcolare il numero totale di carte possedute da un utente specifico, sommando le quantità di tutte le carte associate a quell'utente.

---

## UserDAO - Repository per la gestione degli utenti
- **findAll**: recupera tutti gli utenti presenti nel database.
- **findByEmail**: cerca un utente nel database utilizzando l'indirizzo email fornito.
- **findByEmailContainingIgnoreCase**: ricerca gli utenti il cui indirizzo email contiene la stringa fornita, ignorando la distinzione tra maiuscole e minuscole.
- **findById**: cerca un utente nel database utilizzando l'ID fornito.
- **existsById**: verifica se un utente esiste nel database utilizzando l'ID fornito, restituendo `true` o `false`.

---

## UserImgDAO - Repository per la gestione delle immagini utente
- **findAll**: recupera tutte le immagini assegnabili agli utenti presenti nel database.

