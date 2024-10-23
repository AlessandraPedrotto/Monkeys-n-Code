# Entità del Progetto

## Card

L'entità **Card** si collega alla tabella `cards` del database e rappresenta tutte le carte presenti in gioco. Alcuni attributi, come **abilities** e **attacks**, hanno una lunghezza maggiore rispetto agli altri campi, poiché possono contenere informazioni molto dettagliate.

## Deck

L'entità **Deck** si collega alla tabella `decks` del database. La chiave primaria è generata utilizzando la strategia di Hibernate. Di seguito gli attributi principali:

- **valid**: boolean, indica se un deck è valido o meno.
- **user**: l'ID dello user che possiede il deck.
- **deckImg**: l'ID dell'immagine associata al deck.

## DeckCards

Entità relazionale tra **Deck** e **Card** per gestire la relazione N:N tra i due. Di seguito gli attributi principali:

- **card**: l'ID della carta inclusa nel deck.
- **deck**: l'ID del deck.
- **cardQuantity**: quantità di una certa carta presente nel deck.

## DeckImg

Entità collegata a **Deck** per gestire le immagini dei mazzi.

- **imgDeckPath**: il percorso dell'immagine del deck.

## Role

Entità che gestisce i ruoli degli utenti nel sistema. I ruoli predefiniti presenti nel database sono:

- `ROLE_ADMIN`
- `ROLE_USER`

## User

L'entità **User** rappresenta gli utenti registrati. Ecco gli attributi principali:

- **id**, **name**, **email**, **password**: attributi classici per l'utente.
- **win** e **lose**: gestiscono rispettivamente le vittorie e le sconfitte dell'utente nella classifica.
- **decks**: relazione 1:N con i deck posseduti dall'utente.
- **userImg**: relazione N:1 con **UserImg**, che rappresenta l'immagine del profilo dell'utente.
- **roles**: relazione N:N con i ruoli. Viene creata una tabella relazionale tra **User** e **Role**, con `fetch = FetchType.EAGER` per caricare immediatamente i ruoli ed evitare ritardi che potrebbero causare errori di login.
- **following** e **followers**: due `HashSet` che gestiscono la relazione con la tabella `follows`, utile per gestire seguaci e seguiti.

## UserCards 

Entità relazionale tra **User** e **Card** per gestire la relazione N:N. Di seguito l'attributo principale:

- **cardQuantity**: indica la quantità di una determinata carta posseduta dall'utente.

## UserImg

Entità collegata a **User** per gestire le immagini che un utente può scegliere come immagine del profilo.


# Repository del Progetto


Tutte le repository  estenderanno `JpaRepository`, fornendo metodi per interagire con la tabelle nel database.
# CardsDAO - Repository per la gestione delle carte

**FindALL** restituisce una lista di tutte le carte del Database.


# DeckCardDAO - Repository per la gestione delle carte nel mazzo
 **FindByDeck** recupera tutte le associazioni di DeckCards associate a un mazzo specificato. È utile per ottenere tutte le carte che appartengono a un particolare mazzo.

**findByCardIdAndDeckId** restituisce un oggetto Optional<DeckCards> che contiene l'associazione di DeckCards se presente; altrimenti, è vuoto.



# DeckDAO - Repository per la gestione dei mazzi
**findByUserId** Questo metodo recupera tutti i mazzi associati a un determinato utente. È utile per ottenere la lista dei mazzi che un utente ha creato o posseduto.

**findByUserAndNameDeck** Questo metodo recupera un mazzo specifico associato a un utente in base al nome del mazzo. È utile per verificare se un determinato mazzo esiste già per un utente.

# DeckImgDAO - Repository per la gestione delle immagini dei mazzi
**findAll** Questo metodo recupera tutte le immagini dei mazzi presenti nella tabella deck_img. È utile per ottenere un elenco completo delle immagini che possono essere associate mazzi.

# RoleDAO - Repository per la gestione dei ruoli
Usa solo i metodi di JPA.

# UserCardDAO - Repository per la gestione delle carte degli utenti
**findByUserId** Questo metodo recupera tutte le associazioni di carte per un utente specifico, identificato dal suo ID.

**findByUserAndCard** Questo metodo cerca una specifica associazione di carta per un utente. Se trovata, restituisce un oggetto UserCards.

**countTotalCardsByUserId**  Questo metodo utilizza una query personalizzata per calcolare il numero totale di carte possedute da un utente specifico, sommando le quantità di tutte le carte associate a quell'utente.

# UserDAO - Repository per la gestione degli utenti

**findAll**  Questo metodo recupera tutti gli utenti presenti nel database.

**findByEmail** Questo metodo cerca un utente nel database utilizzando l'indirizzo email fornito.

**findByEmailContainingIgnoreCase**  Questo metodo ricerca gli utenti il cui indirizzo email contiene la stringa fornita, ignorando la distinzione tra maiuscole e minuscole.

**findById** Questo metodo cerca un utente nel database utilizzando l'ID fornito.

**existsById** Questo metodo verifica se un utente esiste nel database utilizzando l'ID fornito restituendo o true o false.

# UserImgDAO - Repository per la gestione delle immagini utente

**findAll** Questo metodo recupera tutte le immagini assegnabili agli utenti presenti nel database.
 