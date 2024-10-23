Ecco una versione migliorata e più leggibile del tuo Markdown. La formattazione è stata affinata per migliorare l'esperienza visiva e la chiarezza.


# Service del Progetto

## CardService

### Annotazioni
- **@Service**: Indica che questa classe è un componente di servizio in Spring, permettendo l'iniezione automatica delle dipendenze.

### Dipendenze
- `CardsDAO`: interfaccia del repository per l'accesso al database delle carte.

### Metodi

#### `findALL()`
```java
public List<Card> findALL()
```
**Descrizione**: Restituisce tutte le carte disponibili nel database.  
**Ritorna**: Una lista di oggetti Card.

---

#### `findAllSorted(String sort, boolean desc)`
```java
public List<Card> findAllSorted(String sort, boolean desc)
```
**Descrizione**: Restituisce tutte le carte ordinate in base a `sort` e `desc`.  
**Parametri**:
- `sort`: Campo di ordinamento (es. "level" o "nationalPokedexNumbers").
- `desc`: Booleano che determina se l'ordinamento deve essere in ordine decrescente.  
**Ritorna**: Una lista di oggetti Card ordinati.

---

#### `filterByParam(HashMap<String,String> filters, List<Card> lista)`
```java
public List<Card> filterByParam(HashMap<String,String> filters, List<Card> lista)
```
**Descrizione**: Filtra la lista delle carte in base ai parametri forniti.  
**Parametri**:
- `filters`: Mappa contenente i parametri di filtro (es. set, tipi, nome, rarità, supertipo, sottotipi).
- `lista`: Lista di carte da filtrare.  
**Ritorna**: Una lista di oggetti Card filtrati.

---

#### `findByParam(String set, String types, String name, String rarity, String supertype, String subtypes, String sort, boolean desc)`
```java
public List<Card> findByParam(String set, String types, String name, String rarity, String supertype, String subtypes, String sort, boolean desc)
```
**Descrizione**: Restituisce un elenco di carte filtrate in base a diversi parametri e ordinate se richiesto.  
**Parametri**:
- `set`: Il set della carta.
- `types`: Il tipo della carta.
- `name`: Il nome della carta.
- `rarity`: La rarità della carta.
- `supertype`: Il supertipo della carta.
- `subtypes`: I sottotipi della carta.
- `sort`: Campo di ordinamento.
- `desc`: Booleano per determinare l'ordinamento decrescente.  
**Ritorna**: Una lista di oggetti Card filtrati e ordinati.

---

#### `getCardsByPage(List<Card> allCards, int page, int cardsPerPage)`
```java
public List<Card> getCardsByPage(List<Card> allCards, int page, int cardsPerPage)
```
**Descrizione**: Restituisce un sottoinsieme di carte in base al numero di pagina e al numero di carte per pagina.  
**Parametri**:
- `allCards`: Lista di tutte le carte.
- `page`: Numero della pagina richiesta.
- `cardsPerPage`: Numero di carte per pagina.  
**Ritorna**: Una lista di oggetti Card per la pagina richiesta.

---

#### `getCardById(String cardId)`
```java
public Optional<Card> getCardById(String cardId)
```
**Descrizione**: Restituisce una carta in base al suo ID.  
**Parametri**:
- `cardId`: ID della carta.  
**Ritorna**: Un oggetto Optional<Card> che contiene la carta se trovata.

---

#### `findById(String cardId)`
```java
public Card findById(String cardId)
```
**Descrizione**: Restituisce una carta in base al suo ID, oppure null se non trovata.  
**Parametri**:
- `cardId`: ID della carta.  
**Ritorna**: Un oggetto Card se trovato, altrimenti null.

---

## DeckCardsService

### Iniezione delle Dipendenze
La classe utilizza l'iniezione delle dipendenze per accedere ai repository necessari per le operazioni CRUD sulle entità coinvolte:
- **DeckDAO**: per operazioni sui mazzi.
- **CardsDAO**: per operazioni sulle carte.
- **DeckCardDAO**: per operazioni sulle relazioni tra mazzi e carte.
- **UserCardsService**: per interagire con la collezione di carte di un utente.

### Metodi Principali

#### `SetCard(Long deckId, String cardId, int quantity)`
```java
@Async
public CompletableFuture<String> SetCard(Long deckId, String cardId, int quantity)
```
**Descrizione**: Aggiunge o aggiorna la relazione tra un mazzo e una carta. Se la carta esiste già nel mazzo, aggiorna la quantità; altrimenti, crea una nuova relazione. Restituisce un messaggio di successo o errore.

---

#### `RemoveCard(Long deckId, String cardId, int quantity)`
```java
@Async
public CompletableFuture<String> RemoveCard(Long deckId, String cardId, int quantity)
```
**Descrizione**: Rimuove una carta da un mazzo. Se la quantità scende a zero o meno, elimina completamente la relazione; altrimenti, aggiorna la quantità della carta nel mazzo. Restituisce un messaggio di successo o errore.

---

#### `validateDeck(Long deckId, User user)`
```java
public String validateDeck(Long deckId, User user)
```
**Descrizione**: Valida un mazzo secondo le seguenti regole:
- Verifica che il mazzo contenga esattamente 60 carte.
- Assicura che ci sia almeno un Pokémon di base.
- Controlla che non ci siano più di 4 copie di ciascuna carta (escluso "Energia").
- Verifica che ogni evoluzione abbia il suo corrispondente Pokémon di base.
- Controlla che l'utente possieda le carte nel mazzo.  
**Restituisce**: Una stringa con eventuali errori di validazione.

---

#### `getDeckCards(Long deckId)`
```java
public List<DeckCards> getDeckCards(Long deckId)
```
**Descrizione**: Recupera la lista delle carte associate a un mazzo dato il suo ID.  
**Restituisce**: Una lista vuota se il mazzo non esiste.

---

#### `deleteCardsFromDeck(Long deckId)`
```java
public void deleteCardsFromDeck(Long deckId)
```
**Descrizione**: Elimina tutte le carte da un mazzo specificato, iterando attraverso ciascuna carta e invocando il metodo `RemoveCard`.

---

#### `formatValidationErrors(List<String> violations)`
```java
private String formatValidationErrors(List<String> violations)
```
**Descrizione**: Questo metodo formatta gli errori di validazione in una stringa con un elenco puntato, utile per una visualizzazione chiara degli errori riscontrati nella validazione del mazzo.

---

## DeckImgService

### Annotazioni della Classe
- **@Service**: Indica che la classe è un componente di servizio nel contesto di Spring, abilitando a Spring di gestire il ciclo di vita di questo bean e l'iniezione delle dipendenze.

### Campi
- `imgDAO`: Un'istanza di `DeckImgDAO` utilizzata per eseguire operazioni sul database relative alle immagini dei mazzi.

### Metodi

#### `getAll()`
```java
public List<DeckImg> getAll()
```
**Descrizione**: Recupera tutte le immagini dei mazzi dal database.  
**Restituisce**: Una lista di oggetti `DeckImg`.

---

#### `getDeckImgById(Long id)`
```java
public Optional<DeckImg> getDeckImgById(Long id)
```
**Descrizione**: Recupera un'immagine del mazzo specifico in base al suo identificatore unico.  
**Parametri**:
- `id`: L'identificatore unico dell'immagine del mazzo da recuperare.  
**Restituisce**: Un `Optional<DeckImg>` che può contenere l'immagine del mazzo richiesta se trovata, o essere vuoto se non esiste alcuna immagine con l'ID fornito.

---

## DeckService

### Annotazioni della Classe
- **@Service**: Indica che la classe è un componente di servizio nel contesto di Spring.

### Campi
- `userDAO`: Un'istanza di `UserDAO` per accedere ai dati degli utenti.
- `deckDAO`: Un'istanza di `DeckDAO` per accedere ai dati dei mazzi.

### Costruttore
#### `DeckService(UserDAO userDAO, DeckDAO deckDAO)`
- **Descrizione**: Costruttore per l'iniezione delle dipendenze dei DAO.
- **Parametri**:
  - `userDAO`: L'istanza di `UserDAO` da utilizzare.
  - `deckDAO`: L'istanza di `DeckDAO` da utilizzare.

### Metodi

#### `saveOrUpdateDeck(String userId, String nameDeck, Long deckId, DeckImg deckImg)`
```java
public void saveOrUpdateDeck(String userId, String nameDeck, Long deckId, DeckImg deckImg)
```
**Descrizione**: Salva o aggiorna un mazzo, a seconda che venga fornito un ID del mazzo.  
**Parametri**:
- `userId`: L'ID dell'utente a cui è associato il mazzo.
- `nameDeck`: Il nome del mazzo.
- `deckId`: L'ID del mazzo da aggiornare; se è null, verrà creato un nuovo mazzo.
- `deckImg`: L'immagine del mazzo.

---

#### `DeleteDeck(Long deckId)`
```java
public boolean DeleteDeck(Long deckId)
```
**Descrizione**: Elimina un mazzo in base al suo ID.  
**Parametri**:
- `deckId`: L'ID del mazzo da eliminare.  
**Restituisce**: `true` se il mazzo è stato eliminato con successo; `false` altrimenti.

---

#### `getDeckById(Long id)`
```java
public Optional<Deck> getDeckById(Long id)
```
**Descrizione**: Recupera un mazzo in base al suo ID.  
**Parametri**:
- `id`: L'ID del mazzo da recuperare.  
**Restituisce**: Un `Optional<Deck>` che può contenere il mazzo se trovato, altrimenti sarà vuoto.

---

#### `getDeckByNameDeck(User user, String nameDeck)`
```java
public Optional<Deck> getDeckByNameDeck(User user, String nameDeck)
```
**Descrizione**: Recupera un mazzo in base al suo nome e all'utente associato.  
**Parametri**:
- `user`: L'utente a cui è associato il mazzo.
- `nameDeck`: Il nome del mazzo da recuperare.  
**Restituisce**: Un `Optional<Deck>` che può contenere il mazzo se trovato, altrimenti sarà vuoto.

---

## UserCardsService

### Panoramica
La classe `UserCardsService` gestisce le collezioni di carte degli utenti in un'applicazione di gioco di carte. Fornisce metodi per recuperare, ordinare, aggiungere e aggiornare le carte nella collezione degli utenti.

### Dipendenze
- **UserCardDAO**: Data Access Object per l'accesso e la gestione dei dati relativi alle collezioni di carte degli utenti.

### Costruttore
Annotato con **@Autowired**, consente a Spring di iniettare automaticamente il `UserCardDAO` necessario.

### Metodi

#### `int getTotalCards(String userId)`
```java
public int getTotalCards(String userId)
```
**Descrizione**: Restituisce il numero totale di carte possedute da un utente.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera ottenere il conteggio delle carte.  
**Restituisce**: Un intero che rappresenta il numero totale di carte possedute dall'utente.

---

#### `List<UserCards> getUserCollection(String userId)`
```java
public List<UserCards> getUserCollection(String userId)
```
**Descrizione**: Recupera l'intera collezione di carte per un utente specifico.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera recuperare la collezione di carte.  
**Restituisce**: Una lista di oggetti `UserCards` che rappresentano le carte possedute dall'utente.

---

#### `HashMap<String, Integer> getCollectionById(String userId)`
```java
public HashMap<String, Integer> getCollectionById(String userId)
```
**Descrizione**: Restituisce una mappa degli ID delle carte e delle loro quantità per un utente.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera ottenere la mappa delle carte.  
**Restituisce**: Una mappa che associa gli ID delle carte alle loro quantità.

---

#### `List<String> getCollectionBy(String userId)`
```java
public List<String> getCollectionBy(String userId)
```
**Descrizione**: Restituisce un elenco degli ID delle carte possedute dall'utente.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera ottenere l'elenco delle carte.  
**Restituisce**: Una lista di stringhe che rappresentano gli ID delle carte possedute dall'utente.

---

#### `List<Card> getSortedCollection(String userId, String sort, boolean desc)`
```java
public List<Card> getSortedCollection(String userId, String sort, boolean desc)
```
**Descrizione**: Recupera la collezione di carte dell'utente, ordinata secondo un parametro specificato.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera recuperare la collezione.
- `sort`: Il criterio di ordinamento (ad esempio "name" o "level").
- `desc`: `true` se si desidera un ordinamento decrescente, `false` per un ordinamento crescente.  
**Restituisce**: Una lista di carte ordinate secondo i criteri specificati.

---

#### `void addOrUpdateCard(User user, Card card, int quantity)`
```java
public void addOrUpdateCard(User user, Card card, int quantity)
```
**Descrizione**: Aggiunge o aggiorna una carta nella collezione dell'utente.  
**Parametri**:
- `user`: L'utente per il quale si desidera aggiungere o aggiornare la carta.
- `card`: L'oggetto `Card` da aggiungere o aggiornare.
- `quantity`: La quantità della carta da aggiungere (o aggiornare).

---

#### `CompletableFuture<String> addOrRemoveCard(User user, Card card, int quantity)`
```java
public CompletableFuture<String> addOrRemoveCard(User user, Card card, int quantity)
```
**Descrizione**: Metodo asincrono per aggiungere o rimuovere una certa quantità di una carta per l'utente.  
**Parametri**:
- `user`: L'utente per il quale si desidera aggiungere o rimuovere la carta.
- `card`: La carta da aggiungere o rimuovere.
- `quantity`: La quantità da aggiungere (valore positivo) o da rimuovere (valore negativo).  
**Restituisce**: Un `CompletableFuture` contenente un messaggio di conferma dell'operazione.

---

#### `void cleanDb(String userId)`
```java
public void cleanDb(String userId)
```
**Descrizione**: Rimuove le carte con quantità zero dalla collezione dell'utente nel database.  
**Parametri**:
- `userId`: ID dell'utente per il quale si desidera pulire le carte.

---

#### `int getQuantityByCardUser(User user, Card card)`
```java
public int getQuantityByCardUser(User user, Card card)
```
**Descrizione**: Restituisce la quantità di una specifica carta posseduta dall'utente.  
**Parametri**:
- `user`: L'utente per il quale si desidera conoscere la quantità della carta.
- `card`: La carta di cui si vuole sapere la quantità.  
**Restituisce**: Un intero che rappresenta la quantità della carta, o 0 se la carta non è presente nella collezione dell'utente.

---

## UserService

### Struttura della Classe
La classe `UserService` utilizza diverse dipendenze come `UserDAO`, `UserImgDAO`, `UserCardDAO` e `PasswordEncoder` per interagire con il database e gestire la sicurezza delle password.

### Metodi della Classe

#### `loadUserByUsername(String email)`
```java
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
```
**Descrizione**: Carica un utente in base all'email fornita. Se l'utente non è trovato, viene sollevata un'eccezione `UsernameNotFoundException`.  
**Parametri**:
- `email`: Stringa che rappresenta l'indirizzo email dell'utente.  
**Restituisce**: Un oggetto `UserDetails` contenente le informazioni dell'utente.

---

#### `saveOrUpdateUser(OAuth2User oAuth2User)`
```java
public void saveOrUpdateUser(OAuth2User oAuth2User)
```
**Descrizione**: Salva o aggiorna le informazioni dell'utente ottenute da un provider OAuth2. Se l'utente esiste già, vengono aggiornati i suoi dati; altrimenti, viene creato un nuovo utente.  
**Parametri**:
- `oAuth2User`: Oggetto che rappresenta le informazioni dell'utente recuperate da un provider OAuth2.

---

#### `register(User user)`
```java
public void register(User user)
```
**Descrizione**: Registra un nuovo utente, cifrando la sua password prima di salvarlo nel database. Viene anche assegnata un'immagine predefinita all'utente.  
**Parametri**:
- `user`: Oggetto `User` contenente le informazioni dell'utente da registrare.

---

#### `assignRoles(User user, List<Role> roles)`
```java
public void assignRoles(User user, List<Role> roles)
```
**Descrizione**: Assegna ruoli a un utente. Deve avere almeno 1 ruolo e al massimo 2.  
**Parametri**:
- `user`: Oggetto `User` al quale assegnare i ruoli.
- `roles`: Lista di ruoli da assegnare all'utente.

---

#### `exists(User user)`
```java
public boolean exists(User user)
```
**Descrizione**: Controlla se un'email esiste già nel database e restituisce `true` o `false`.  
**Parametri**:
- `user`: Oggetto `User` con l'email da verificare.  
**Restituisce**: Booleano che indica se l'email esiste (`true`) o meno (`false`).

---

#### `getUserById(String id)`
```java
public User getUserById(String id)
```
**Descrizione**: Recupera un utente tramite il suo ID. Restituisce `null` se non viene trovato alcun utente.  
**Parametri**:
- `id`: Stringa che rappresenta l'ID dell'utente.  
**Restituisce**: Oggetto `User` se trovato, altrimenti `null`.

---

#### `changePassword(String userId, String oldPassword, String newPassword)`
```java
public void changePassword(String userId, String oldPassword, String newPassword) throws Exception
```
**Descrizione**: Cambia la password dell'utente, dopo aver verificato la correttezza della vecchia password. Se la vecchia password non è corretta, viene sollevata un'eccezione.  
**Parametri**:
- `userId`: ID dell'utente il cui password deve essere cambiata.
- `oldPassword`: La vecchia password da verificare.
- `newPassword`: La nuova password da impostare.

---

#### `findByEmail(String email)`
```java
public User findByEmail(String email)
```
**Descrizione**: Trova un utente tramite l'email fornita. Restituisce `null` se non viene trovato alcun utente.  
**Parametri**:
- `email`: Stringa che rappresenta l'email da cercare.  
**Restituisce**: Oggetto `User` se trovato, altrimenti `null`.

---

#### `findByName(String name)`
```java
public User findByName(String name)
```
**Descrizione**: Trova un utente attraverso il suo nome.  
**Parametri**:
- `name`: Stringa che rappresenta il nome dell'utente.  
**Restituisce**: Oggetto `User` se trovato, altrimenti `null`.

---

#### `findById(String Id)`
```java
public User findById(String Id)
```
**Descrizione**: Trova un utente tramite il suo ID. Restituisce `null` se non viene trovato alcun utente.  
**Parametri**:
- `Id`: Stringa che rappresenta l'ID dell'utente.  
**Restituisce**: Oggetto `User` se trovato, altrimenti `null`.

---

#### `DeleteUser(String id)`
```java
public void DeleteUser(String id) throws UsernameNotFoundException
```
**Descrizione**: Elimina un utente e tutti i dati associati come mazzi e carte collezionabili.  
**Parametri**:
- `id`: Stringa che rappresenta l'ID dell'utente da eliminare.

---

#### `userCheck(Object principal)`
```java
public User userCheck(Object principal)
```
**Descrizione**: Verifica l'utente in base all'oggetto `principal`.  
**Parametri**:
- `principal`: Oggetto che rappresenta il principale (utente loggato).  
**Restituisce**: Oggetto `User` se trovato.

---

#### `getAllUserImg()`
```java
public List<UserImg> getAllUserImg()
```
**Descrizione**: Recupera tutte le immagini utente disponibili dal database.  
**Restituisce**: Lista di oggetti `UserImg`.

---

#### `getUserImgById(Long id)`
```java
public Optional<UserImg> getUserImgById(Long id)
```
**Descrizione**: Recupera un'immagine utente tramite il suo ID.  
**Parametri**:
- `id`: ID dell'immagine da recuperare.  
**Restituisce**: Oggetto `Optional<UserImg>` con l'immagine se trovata.

---

#### `updateProfileImage(String id, Long userImgId)`
```java
public void updateProfileImage(String id, Long userImgId) throws Exception
```
**Descrizione**: Aggiorna l'immagine del profilo di un utente, a condizione che sia l'utente che l'immagine siano trovati nel database.  
**Parametri**:
- `id`: ID dell'utente da aggiornare.
- `userImgId`: ID dell'immagine da assegnare.

---

#### `getUserProfileImage(String id)`
```java
public long getUserProfileImage(String id) throws Exception
```
**Descrizione**: Recupera l'immagine del profilo dell'utente e restituisce il suo ID.  
**Parametri**:
- `id`: ID dell'utente di cui si desidera ottenere l'immagine.  
**Restituisce**: ID dell'immagine dell'utente.

---

#### `getUserRoles(String idUser)`
```java
public List<Role> getUserRoles(String idUser)
```
**Descrizione**: Recupera i ruoli associati a un utente basato sul suo ID.  
**Parametri**:
- `idUser`: ID dell'utente di cui si vogliono ottenere i ruoli.  
**Restituisce**: Lista di oggetti `Role` associati all'utente.

---

#### `updateNickname(String userId, String newNickname)`
```java
public void updateNickname(String userId, String newNickname)
```
**Descrizione**: Aggiorna il soprannome di un utente, sollevando un'eccezione se il nuovo soprannome è vuoto.  
**Parametri**:
- `userId`: ID dell'utente cui si desidera aggiornare il soprannome.
- `newNickname`: Nuovo soprannome da impostare.

---

#### `followUser(String loggedUser, String followingId)`
```java
public void followUser(String loggedUser, String followingId)
```
**Descrizione**: Consente a un utente di seguire un altro utente.  
**Parametri**:
- `loggedUser`: ID dell'utente che segue.
- `followingId`: ID dell'utente da seguire.

---

#### `unfollowUser(String loggedUser, String followingId)`
```java
public void unfollowUser(String loggedUser, String followingId)
```
**Descrizione**: Consente a un utente di smettere di seguire un altro utente.  
**Parametri**:
- `loggedUser`: ID dell'utente che smette di seguire.
- `followingId`: ID dell'utente da cui si smette di seguire.

---

#### `isFollowing(String loggedUser, String followingId)`
```java
public boolean isFollowing(String loggedUser, String followingId)
```
**Descrizione**: Verifica se un utente sta seguendo un altro utente specificato.  
**Parametri**:
- `loggedUser`: ID dell'utente che vuole verificare.
- `followingId`: ID dell'utente da controllare.  
**Restituisce**: Booleano che indica se il `loggedUser` segue o meno `followingId`.

---

#### `getFollowers(String userId)`
```java
public Set<User> getFollowers(String userId)
```
**Descrizione**: Recupera la lista dei follower per un utente specificato.  
**Parametri**:
- `userId`: ID dell'utente di cui si vogliono ottenere i follower.  
**Restituisce**: Insieme di oggetti `User` che rappresentano i follower dell'utente.

---

#### `getNumFollowers(String userId)`
```java
public int getNumFollowers(String userId)
```
**Descrizione**: Restituisce il numero di follower di un certo utente.  
**Parametri**:
- `userId`: ID dell'utente di cui si vuole conoscere il numero di follower.  
**Restituisce**: Numero intero di follower.

---

#### `getFollowing(String userId)`
```java
public Set<User> getFollowing(String userId)
```
**Descrizione**: Restituisce la lista degli utenti seguiti da un utente specificato.  
**Parametri**:
- `userId`: ID dell'utente di cui si vogliono ottenere gli utenti seguiti.  
**Restituisce**: Insieme di oggetti `User` che rappresentano gli utenti seguiti.

---

#### `getNumFollowing(String userId)`
```java
public int getNumFollowing(String userId)
```
**Descrizione**: Restituisce il numero di utenti seguiti da un certo utente.  
**Parametri**:
- `userId`: ID dell'utente di cui si vuole conoscere il numero di seguiti.  
**Restituisce**: Numero intero di seguiti.

---

#### `getAllUsersOrderedByWin()`
```java
public List<User> getAllUsersOrderedByWin()
```
**Descrizione**: Recupera tutti gli utenti ordinati per il numero di vittorie in ordine decrescente.  
**Restituisce**: Lista di oggetti `User` ordinata per vittorie.
