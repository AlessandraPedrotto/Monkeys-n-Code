Ecco un markdown leggibile e capibile riguardo il funzionamento dei controller.

## AdminController 

### Annotazioni

- **@Controller**: Indica che questa classe è un controller Spring, permettendo la gestione delle richieste HTTP e l'iniezione delle dipendenze.
  
- **@RequestMapping("/admin")**: Specifica che tutte le richieste con prefisso `/admin` verranno gestite da questo controller.

- **@Autowired**: Indica l'iniezione automatica delle dipendenze in Spring, facilitando l'inizializzazione automatica di componenti.

- **@GetMapping("/formStat")**: Gestisce le richieste HTTP `GET` per l'endpoint `/admin/formStat`.

- **@PostMapping("/formStat")**: Gestisce le richieste HTTP `POST` per l'endpoint `/admin/formStat`.

## Dipendenze

**`UserDAO`**: Oggetto per l'accesso ai dati degli utenti, utilizzato per operazioni di lettura e scrittura nel database utente.

## Metodi

### `getAssignStatsForm(@RequestParam(required = false)String query, Model model)`

```java
     public String getAssignStatsForm(@RequestParam(required = false) String query, Model model)
```
**Descrizione**: Recupera i dati delle statistiche utente per visualizzazione.  

**Parametri**: <br>

   - **query**: Variabile che viene utilizzata nel form come ricerca per email ed è opzionale <br>
   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. 

**Ritorna**: Restituisce la vista con le statistiche.

---

### `assignStatistics(@RequestParam String userId, @RequestParam int win @RequestParam int lose,Model model)`

```java
     public String assignStatistics(@RequestParam String userId,
                                    @RequestParam int win,
                                    @RequestParam int lose,
                                    Model model)
```
**Descrizione**: Aggiorna le statistiche di uno specifico utente.  

**Parametri**: <br>
 
   - **userId**: Id dell'user. <br>
   - **win**: Corrisponde alle vittorie dello user. <br>
   - **lose**: Corrisponde alle sconfitte dello user. <br>
   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. 

**Ritorna**: Restituisce un messaggio di successo se è andato a buon fine oppure un messaggio di errore.

---

## AuthController 

### Annotazioni

- **@Controller**: Indica che questa classe è un controller Spring, abilitando la gestione delle richieste HTTP e il supporto per l'iniezione delle dipendenze.
  
- **@RequestMapping("/admin")**: Definisce un prefisso `/admin` per tutte le rotte del controller.

- **@Autowired**: Indica a Spring di iniettare automaticamente un'istanza della dipendenza nei campi, nei costruttori o nei metodi di configurazione.

- **@GetMapping("/login")**: Mappa le richieste HTTP `GET` per il percorso `/login`, indirizzandole al metodo corrispondente.

- **@GetMapping("/register")**: Mappa le richieste HTTP `GET` per il percorso `/register`, indirizzandole al metodo di registrazione.

- **@PostMapping("/register")**: Mappa le richieste HTTP `POST` per il percorso `/register`, utilizzato per gestire i dati di registrazione inviati.

- **@ModelAttribute**: Lega un parametro di metodo o un oggetto restituito dal metodo a un attributo del modello con un nome specificato.

---

## Dipendenze

- **`UserService`**: Oggetto per la gestione delle operazioni dell'utente, come l'accesso e la registrazione, collegato tramite iniezione di dipendenza.

---

## Metodi

### `loginPage()`

```java
    public String loginPage()
```
**Descrizione**: Pagina di login.  

**Ritorna**: Reinderizza alla pagina di login.

---

### `registerPage(Model model)`

```java
       public String registerPage(Model model)
```
**Descrizione**: Pagina di registrazione.  

**Parametri**: <br>

   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. 

**Ritorna**: Reindirizza alla pagina di registrazione.

---

### `registerUser(@ModelAttribute User user, Model model)`

```java
       public String registerUser(@ModelAttribute User user, Model model)
```
**Descrizione**: Gestisce la registrazione utente.  

**Parametri**: <br>

   - **user**: Oggetto User associato tramite @ModelAttribute per mappare automaticamente i dati dal form HTML al modello. <br>
   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Reindirizza alla pagina di login.

### `isValidPassword(String password)`

```java
        private boolean isValidPassword(String password)
```
**Descrizione**: Gestisce la validazione della password.  

**Parametri**: <br>

   - **password**: Viene utilizzata come confronto per la regex.

**Ritorna**: true se la password rispetta la regex, false altrimenti.

---

## CardController 

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo la gestione delle richieste HTTP e l'iniezione delle dipendenze.

- **@Autowired**: Permette l'iniezione automatica delle dipendenze.

- **@GetMapping("/cards")**: Gestisce le richieste HTTP `GET` al percorso `/cards` per la visualizzazione delle carte.

- **@AuthenticationPrincipal**: Identifica l'utente autenticato o il sistema che accede a una risorsa protetta.

- **@RequestParam()**: Mappa un parametro della richiesta HTTP con un parametro del metodo.

- **@PostMapping("/collection/add")**: Gestisce le richieste HTTP `POST` per l’aggiunta di una carta alla collezione.

- **@PostMapping("/collection/remove")**: Gestisce le richieste HTTP `POST` per la rimozione di una carta dalla collezione.

- **@GetMapping("/card/{cardId}")**: Gestisce le richieste HTTP `GET` per visualizzare una carta specifica tramite `cardId`.

- **@Async**: Indica che il metodo viene eseguito in modo asincrono.

- **@ResponseBody**: Specifica che il valore restituito dal metodo è il corpo della risposta HTTP.

- **@PathVariable**: Mappa una variabile di percorso URL in un parametro del metodo.

---

## Dipendenze

- **`UserService`**: Istanza di `UserService` per la gestione dell’accesso e delle operazioni utente.

- **`CardService`**: Istanza di `CardService` per l'accesso e il filtraggio delle carte.

- **`UserCardsService`**: Istanza di `UserCardsService` per la gestione del

## Metodi

### `getCards( @AuthenticationPrincipal Object principal, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "false") boolean owned, @RequestParam(required = false) String from,@RequestParam(required = false) String set, @RequestParam(required = false) String types, @RequestParam(required = false) String name, @RequestParam(required = false) String rarity, @RequestParam(required = false) String supertype, @RequestParam(required = false) String subtypes, @RequestParam(required = false, defaultValue = "name") String sort, @RequestParam(defaultValue = "false") boolean desc, @RequestParam(defaultValue = "1") int blocco, @RequestParam(defaultValue = "true") boolean grayFilter )`

```java
    public String getCards(
	    @AuthenticationPrincipal Object principal,
	    Model model,
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "false") boolean owned,
	    @RequestParam(required = false) String from,
	    @RequestParam(required = false) String set,
	    @RequestParam(required = false) String types,
	    @RequestParam(required = false) String name,
	    @RequestParam(required = false) String rarity,
	    @RequestParam(required = false) String supertype,
	    @RequestParam(required = false) String subtypes,
	    @RequestParam(required = false, defaultValue = "name") String sort,
	    @RequestParam(defaultValue = "false") boolean desc,
	    @RequestParam(defaultValue = "1") int blocco,
	    @RequestParam(defaultValue = "true") boolean grayFilter
    )
```
**Descrizione**: Imposta i filtri per le carte.  

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
 - **page**: Singola pagina della collezione, impostata a 1. <br>
 - **owned**: Carte possedute, impostato a false. <br>
 - **from**: Ordine delle carte (nome, numero pokedex o livello), opzionale. <br>
 - **set**: Set da cui provengono le carte, opzionale.  <br>
 - **types**: Tipi delle carte, opzionale. <br>
 - **name**: Nome delle carte, opzionale. <br>
 - **rarity**: Rarità delle carte (ex, shiny), opzionale. <br>
 - **supertype**: Supertipo delle carte (pokemon, allenatore, energia), opzionale. <br>
 - **subtypes**: Sottotipo delle carte (base, speciale etc.), opzionale. <br>
 - **desc**: Tipo di ordinamento delle carte, impostato a false <br>
 - **blocco**: Paginazione delle carte, impostato a 1. <br>
 - **greyFilter**: Filtro grigio che viene inizialmente dato alle carte, impostato a true.

**Ritorna**: Reinderizza alla pagina di visualizzazione delle carte.

---

### `CompletableFuture<String> addCardToCollection(@AuthenticationPrincipal Object principal, @RequestParam String cardId)`

```java
       public CompletableFuture<String> addCardToCollection(@AuthenticationPrincipal Object principal, @RequestParam String cardId)
```
**Descrizione**: Aggiunge carte alla collezione dell'utente specificato.  

**Parametri**: <br>

   - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente <br>
   - **cardId**: id della carta da aggiungere.

**Ritorna**: Restituisce la collezione con la carta aggiunta.

---

### `CompletableFuture<String> removeCard(@AuthenticationPrincipal Object principal, @RequestParam String cardId)`

```java
        public CompletableFuture<String> removeCard(@AuthenticationPrincipal Object principal, @RequestParam String cardId)
```
**Descrizione**: Rimuove carte alla collezione dell'utente specificato.  

**Parametri**: <br>

  - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
  - **cardId**: Id della carta da rimuovere.

**Ritorna**: Restituisce la collezione con la carta rimossa.

---

### `viewCard(@AuthenticationPrincipal Object principal, @PathVariable("cardId") String cardId, Model model)`

```java
         public String viewCard(@AuthenticationPrincipal Object principal, @PathVariable("cardId") String cardId, Model model)
```
**Descrizione**: Visualizza i dettagli di una carta specificata, inclusa la quantità posseduta, se applicabile..  

**Parametri**: <br>

   - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
   - **cardId**: Id della carta da visualizzare.

**Ritorna**: Restituisce i dettagli della carta se presente oppure restituisce un messaggio di errore se non trovata.

---

# ClassificationController 

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo la gestione delle richieste HTTP e l'iniezione delle dipendenze.

- **@Autowired**: Consente l'iniezione automatica delle dipendenze, facilitando l'uso di servizi come `UserService`.

- **@GetMapping("/classification")**: Gestisce le richieste HTTP `GET` al percorso `/classification` per la visualizzazione della classifica.

- **@PostMapping("/classification")**: Gestisce le richieste HTTP `POST` al percorso `/classification` per aggiornare le statistiche dell'utente nella classifica.

- **@AuthenticationPrincipal**: Recupera l'utente autenticato per l'accesso alle risorse protette.


---

## Dipendenze

- **`UserService`**: Gestisce l'accesso ai dati e le operazioni relative agli utenti.

---

## Metodi

### `showClassification(@AuthenticationPrincipal Object principal, Model model)`

```java
   public String showClassification(@AuthenticationPrincipal Object principal, Model model)
```
**Descrizione**: Recupera le statistiche aggiornate dell'utente per la classifica.  

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Reinderizza i dati all'interno della classifica.

---

### `postClassification(@AuthenticationPrincipal Object principal, Model model)`

```java
       public String postClassification(@AuthenticationPrincipal Object principal, Model model)
```
**Descrizione**: Aggiorna le statistiche dell'utente nella classifica e visualizza i dati aggiornati.  

**Parametri**: <br>

   - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
   - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.


**Ritorna**: Restituisce la vista con la classifica.

---

## CustomError

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo di gestire errori e risposte HTTP personalizzate.

- **@GetMapping("/error")**: Gestisce le richieste HTTP `GET` al percorso `/error`, utilizzato per reindirizzare l'utente alla pagina di errore quando si verifica un errore.

---
## Metodi

### `handleError(HttpServletRequest request)`

```java
  public String handleError(HttpServletRequest request)
```
**Descrizione**: Gestisce errori HTTP se la pagina non trovata.  

**Parametri**: <br>

 - **request**: Oggetto HttpServletRequest che fornisce informazioni dettagliate sulla richiesta HTTP, inclusi eventuali codici di stato di errore.
 
**Ritorna**: Reinderizza alla pagina Error per 404 Not Found oppure altre viste di errore diverso da 404.

---

## DeckController 

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@RequestMapping("/decks")**: Mappa le richieste al percorso `/decks`.

- **@Autowired**: Inietta automaticamente le dipendenze.

 - **@GetMapping, @PostMapping**: Gestiscono richieste HTTP `GET` o `POST` per percorsi specifici.

- **@AuthenticationPrincipal**: Permette di autenticare il sistema o utente che cerca di accedere a una risorsa o servizio.

- **@RequestParam, @PathVariable**: Mappano parametri di richiesta e variabili URI.

- **@PathVariable** : Indica che un parametro del metodo deve essere associato a una variabile modello URI

- **@Async**: Indica che il metodo sarà eseguito in modo asincrono.

- **@ResponseBody**: Indica che il metodo restituisce direttamente il contenuto della risposta.


## Dipendenze

- `DeckCardsService`: Istanza di DeckCardsService che gestisce le carte dentro ai deck.

- `UserCardsService`: Istanza di UserCardsService che gestisce le collezioni di carte dell'utente.

- `CardService`: Istanza di CardService che gestisce i filtri delle carte.

- `UserService`: Istanza di UserService che gestisce l'accesso dell'utente.

- `DeckService`: Istanza di DeckService che gestisce i deck dell'utente.

- `DeckImgService`: Istanza di DeckImgService che gestisce le immagini dei deck.

## Metodi

### `String decks(@AuthenticationPrincipal Object principal, Model model)`

```java
    public String decks(@AuthenticationPrincipal Object principal, Model model)
```
**Descrizione**: Effettua la validazione per ogni deck.  

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Restituisce la visualizzazione dei deck se la validazione è andata a buon fine, altrimenti restituisce messaggio di errore.

---

### `create(Model model)`

```java
   public String create(Model model) 
```
**Descrizione**: Recupera le immagini del profilo dal DB.

**Parametri**: <br>
    - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Reindirizza le immagini all'interno dell'html.

---

### `createPost(@AuthenticationPrincipal Object principal, @RequestParam String deckName, @RequestParam Long deckImgId)`

```java
    public String createPost(@AuthenticationPrincipal Object principal, @RequestParam String deckName, @RequestParam Long deckImgId) 
```
**Descrizione**: Crea un deck con immagine profilo.

**Parametri**: <br>

  - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Reindirizza all'html dopo aver selezionato l'immagine e salvato le modifiche, altrimenti da errore.

---

### `deleteDeck(@AuthenticationPrincipal Object principal, @RequestParam String deckName, @RequestParam Long deckImgId)`

```java
    public String deleteDeck(@PathVariable("deckId") Long deckId, Model model)
```
**Descrizione**: Permette di cancellare un deck tramite Id.

**Parametri**: <br>
   
  - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
  - **deckId**: Id deck.

**Ritorna**: Reindirizza alla pagina di cancellazione.

---

### `deleteDeckConfirm(@RequestParam Long deckId, @RequestParam boolean confirm)`

```java
    public String deleteDeckConfirm(@RequestParam Long deckId, @RequestParam boolean confirm)
```
**Descrizione**: Conferma cancellazione del deck.

**Parametri**: <br>

   - **confirm**: Variabile di conferma per la cancellazione. <br>
   - **deckId**: Id deck.

**Ritorna**: Ritorna alla visualizzazione dei Deck.

---

### `String viewDeck( @AuthenticationPrincipal Object principal, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "false") boolean owned, @RequestParam(required = false) String from,@RequestParam(required = false) String set, @RequestParam(required = false) String types, @RequestParam(required = false) String name, @RequestParam(required = false) String rarity, @RequestParam(required = false) String supertype, @RequestParam(required = false) String subtypes, @RequestParam(required = false, defaultValue = "name") String sort, @RequestParam(defaultValue = "false") boolean desc, @RequestParam(defaultValue = "1") int blocco`

```java
    String viewDeck( @AuthenticationPrincipal Object principal, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "false") boolean owned, @RequestParam(required = false) String from,@RequestParam(required = false) String set, @RequestParam(required = false) String types, @RequestParam(required = false) String name, @RequestParam(required = false) String rarity, @RequestParam(required = false) String supertype, @RequestParam(required = false) String subtypes, @RequestParam(required = false, defaultValue = "name") String sort, @RequestParam(defaultValue = "false") boolean desc, @RequestParam(defaultValue = "1") int blocco, )
```
**Descrizione**: Visualizzazione delle carte nel deck con filtri e della paginazione.

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
 - **page**: Singola pagina della collezione, impostata a 1. <br>
 - **owned**: Carte possedute, impostato a false. <br>
 - **from**: Ordine delle carte (nome, numero pokedex o livello), opzionale. <br>
 - **set**: Set da cui provengono le carte, opzionale. <br>
 - **types**: Tipi delle carte, opzionale. <br>
 - **name**: Nome delle carte, opzionale. <br>
 - **rarity**: Rarità delle carte (ex, shiny), opzionale. <br>
 - **supertype**: Supertipo delle carte (pokemon, allenatore, energia), opzionale. <br>
 - **subtypes**: Sottotipo delle carte (base, speciale etc.), opzionale. <br>
 - **desc**: Tipo di ordinamento delle carte, impostato a false. <br>
 - **blocco**: Paginazione delle carte, impostato a 1.

**Ritorna**:  Vista aggiornata del deck filtrato.

---

### `addCard(@RequestParam Long deckId, @RequestParam String cardId)`

```java
   public CompletableFuture<String> addCard(@RequestParam Long deckId, @RequestParam String cardId)
```
**Descrizione**: Permette di aggiungere nel deck una carta in modo asincrono

**Parametri**: <br>

 - **deckId**: Id deck <br>
 - **cardId**: Id card

**Ritorna**: Restituisce il deck aggiornato con la carta aggiunta

---

### `removeCard(@RequestParam Long deckId, @RequestParam String cardId)`

```java
    public CompletableFuture<String> removeCard(@RequestParam Long deckId, @RequestParam String cardId)
```
**Descrizione**: Permette di rimuovere una carta dal deck in modo asincrono

**Parametri**: <br>

 - **deckId**: Id deck <br>
 - **cardId**: Id card

**Ritorna**: Restituisce il deck aggiornato con la carta rimossa

---

### `validate(@RequestParam Long deckIdValidate, @AuthenticationPrincipal Object principal)`

```java
    @ResponseBody String validate(@RequestParam Long deckIdValidate, @AuthenticationPrincipal Object principal)
```
**Descrizione**: Permette di convalidare il deck

**Parametri**: <br>

 - **deckIdValidate**: Id deck da convalidare <br>
 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente.

**Ritorna**: Restituisce il deck convalidato

---

### `editDeck(@PathVariable("deckId") Long deckId, Model model)`

```java
     public String editDeck(@PathVariable("deckId") Long deckId, Model model)
```
**Descrizione**: Permette di modificare il deck

**Parametri**: <br>

 - **deckId**: Id deck <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Restituisce la vista per modificare il deck

---

### ` updateDeck(@AuthenticationPrincipal Object principal, @RequestParam("deckId") Long deckId,@RequestParam("deckName") String deckName, @RequestParam("deckImgId") Long deckImgId)`

```java
     public String updateDeck(@AuthenticationPrincipal Object principal, @RequestParam("deckId") Long deckId,@RequestParam("deckName") String deckName, @RequestParam("deckImgId") Long deckImgId)
```
**Descrizione**: Permette di aggiornare il deck

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **deckId**: Id deck <br>
 - **deckName**: Nome deck <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Restituisce la vista con il deck aggiornato

---

## DeckRulesController

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@GetMapping(/deckRules)**: Mappa le richieste HTTP GET all'URL `/deckRules`, consentendo l'accesso alla vista di regolamento dei deck.

### Metodi

### `mostraDeckRules()`

```java
    public String mostraDeckRules()
```
**Descrizione**: Gestisce la visualizzazione delle regole per la creazione e gestione dei deck.

**Ritorna**: Restituisce la vista con il regolamento del Deck

---

## FollowersController

### Annotazioni
- **@RequestMapping("/user")**: Specifica che tutti gli endpoint in questa classe saranno relativi al percorso `/user`.

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@Autowired**:  Consente l'iniezione automatica delle dipendenze in Spring per i campi, i costruttori o i metodi setter.

- **@GetMapping(/{userId})**:ssocia una richiesta HTTP `GET` a un endpoint che include l'`userId`.

- **@AuthenticationPrincipal**: Permette di autenticare il sistema o utente che cerca di accedere a una risorsa o servizio.

- **@PathVariable** : Mappa i parametri del metodo a variabili definite nell’URI dell'endpoint.

### Dipendenze

- `DeckCardsService`: Istanza di DeckCardsService che gestisce le carte dentro ai deck.

- `UserService`: Istanza di UserService che gestisce l'accesso dell'utente.

- `DeckService`: Istanza di DeckService che gestisce i deck dell'utente.

### Metodi

### `user(@AuthenticationPrincipal Object principal, @PathVariable String userId, Model model)`

```java
    public String user(@AuthenticationPrincipal Object principal, @PathVariable String userId, Model model)
```
**Descrizione**: Se l'utente è gia registrato allora mi reindirizza al profilo dell'utente, altrimenti mi reindirizza al profilo dell'admin.

**Parametri**: <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **userId**: Id user <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. 

**Ritorna**: Restituisce la vista con del profilo con aggiunta il conteggio di seguiti e follower.

---

### `follow(@AuthenticationPrincipal Object principal, @RequestParam String user)`

```java
    public String follow(@AuthenticationPrincipal Object principal, @RequestParam String user)
```
**Descrizione**: Gestisce l’azione quando viene premuto il tasto "follow".

**Parametri**: <br>

 - **principal**: Identifica l'utente autenticato che effettua l'azione di follow. <br>
 - **user**: Id dell'utente che verrà seguito.

**Ritorna**: Restituisce la vista aggiornata dell'utente seguito

---

### `seeFollowers(@PathVariable String userId, Model model)`

```java
    public String seeFollowers(@PathVariable String userId, Model model)
```
**Descrizione**: Permette di visualizzare i follower di un utente specificato

**Parametri**: <br>

- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
- **userId**: Id dell'utente di cui visualizzare i follower.

**Ritorna**: La vista con l'elenco dei follower dell'utente.

---

### `seeFollowing(@PathVariable String userId, Model model)`

```java
    public String seeFollowing(@PathVariable String userId, Model model)
```
**Descrizione**:  Consente la visualizzazione degli utenti seguiti dall'utente specificato.

**Parametri**: <br>

- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
- **userId**:  Id dell'utente di cui visualizzare i seguiti.

**Ritorna**: La vista con l'elenco degli utenti seguiti.

---

### `String unFollow(@AuthenticationPrincipal Object principal, @RequestParam String user)`

```java
    public String unFollow(@AuthenticationPrincipal Object principal, @RequestParam String user)
```
**Descrizione**: Gestisce la logica quando viene premuto il tasto "unfollow".

**Parametri**: <br>

- **principal**: Identifica l'utente autenticato che esegue l'azione di unfollow. <br>
- **user**: Id dell'utente che verrà smesso di seguire.

**Ritorna**: Restituisce la vista aggiornata dell'utente che è stato smesso di seguire.

---

### `deckView(@PathVariable Long deckId,Model model) `

```java
   public String deckView(@PathVariable Long deckId,Model model)
```
**Descrizione**: Visualizazione delle carte nel deck degli utenti seguiti.

**Parametri**: <br>

- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
- **deckId**: Id deck

**Ritorna**: Restituisce la visualizzazione del deck.

---

## HomeController

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@GetMapping("/")**: Mappa le richieste HTTP `GET` all'URL `root`.

- **@GetMapping("secured")**: Mappa le richieste HTTP `GET` all'URL `/secured`.

## Metodi

### `Home()`

```java
    public String Home()
```
**Descrizione**: Homepage.

**Ritorna**: Restituisce la vista della homepage.

---

### `Secured()`

```java
    public String Secured()
```
**Descrizione**: Pagina con un link

**Ritorna**: Restituisce un link che reindirizza alla collezione di carte.

---

## UserCardController

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@RequestParam**: Mappa parametri di richiesta

- **@Autowired**:  Consente l'iniezione automatica delle dipendenze in Spring per i campi, i costruttori o i metodi setter.

- **@PostMapping("/userCard")**: Mappa le richieste HTTP `POST` all'URL `/userCard`.

- **@GetMapping("/users/{userId}/totalCards")**:  Mappa le richieste HTTP `GET` per il conteggio totale delle carte possedute da un utente specifico tramite `userId`.

### Dipendenze

- `UserCardsService`: Istanza di UserCardsService che gestisce le collezioni di carte dell'utente.

- `CardService`: Istanza di CardService che gestisce i filtri delle carte.

- `UserService`: Istanza di UserService che gestisce l'accesso dell'utente.

## Metodi

### `addCardToUser()`

```java
    public String addCardToUser(@AuthenticationPrincipal Object principal,
	    Model model,
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "false") boolean owned,
	    @RequestParam(required = false) String from,
	    @RequestParam(required = false) String set,
	    @RequestParam(required = false) String types,
	    @RequestParam(required = false) String name,
	    @RequestParam(required = false) String rarity,
	    @RequestParam(required = false) String supertype,
	    @RequestParam(required = false) String subtypes,
	    @RequestParam(required = false, defaultValue = "name") String sort,
	    @RequestParam(defaultValue = "false") boolean desc,
	    @RequestParam(defaultValue = "1") int blocco,
	    @RequestParam(defaultValue = "true") boolean grayFilter)
```
**Descrizione**: Aggiunge una carta alla collezione dell'utente utilizzando una serie di filtri di ricerca per visualizzare e selezionare le carte.

**Parametri** <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
 - **page**: Singola pagina della collezione, impostata a 1. <br>
 - **owned**: Carte possedute, impostato a false. <br>
 - **from**: Ordine delle carte (nome, numero pokedex o livello), opzionale. <br>
 - **set**: Set da cui provengono le carte, opzionale. <br>
 - **types**: Tipi delle carte, opzionale. <br>
 - **name**: Nome delle carte, opzionale. <br>
 - **rarity**: Rarità delle carte (ex, shiny), opzionale. <br>
 - **supertype**: Supertipo delle carte (pokemon, allenatore, energia), opzionale. <br>
 - **subtypes**: Sottotipo delle carte (base, speciale etc.), opzionale. <br>
 - **desc**: Tipo di ordinamento delle carte, impostato a false. <br>
 - **blocco**: Paginazione delle carte, impostato a 1. <br>
 - **greyFilter**: Filtro grigio che viene inizialmente dato alle carte, impostato a true.

**Ritorna**: Restituisce la vista delle carte filtrate altrimenti errore.

---

### `getTotalCards(@PathVariable String userId)`

```java
    public Integer getTotalCards(@PathVariable String userId)
```
**Descrizione**: Calcola e restituisce il totale delle carte possedute da uno specifico utente.

**Parametri** <br>

- **userId**: Id dell'utente di cui si vogliono contare le carte possedute.
 
**Ritorna**: Restituisce un valore intero che rappresenta il totale delle carte possedute dall'utente.

---

## UserController

### Annotazioni

- **@Controller**: Indica che questa classe è un controller in Spring, permettendo l'iniezione automatica delle dipendenze.

- **@RequestMapping("/profile")**: Specifica che tutti gli endpoint in questa classe saranno relativi al percorso `/profile`.

- **@Autowired**:  Consente l'iniezione automatica delle dipendenze in Spring per i campi, i costruttori o i metodi setter.

- **@GetMapping e @PostMapping**: Mappano le richieste HTTP `GET` e `POST` ai rispettivi URL.

- **@AuthenticationPrincipal**: Permette di autenticare il sistema o utente che cerca di accedere a una risorsa o servizio.

- **@RequestParam**: Mappa parametri di richiesta.

### Dipendenze

- `UserService`: Istanza di UserService che gestisce l'accesso dell'utente.

- `UserCardDAO`: Istanza di UserCardDAO che gestisce la collezione delle carte.

### Metodi

 `showProfile(@AuthenticationPrincipal Object principal, Model model)  throws Exception`

```java
    public String showProfile(@AuthenticationPrincipal Object principal, Model model) throws Exception
```
**Descrizione**: Mostra il profilo dell'utente specificato (Deck, statistiche, email etc)

**Parametri** <br>

 - **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
 - **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.
 
**Ritorna**: Restituisce la vista del profilo

---

 `updateProfileImage( @RequestParam("userId") String userId, @RequestParam("id") Long imageId, RedirectAttributes redirectAttributes)`

```java
   public String updateProfileImage( @RequestParam("userId") String userId, @RequestParam("id") Long imageId, RedirectAttributes redirectAttributes)
```
**Descrizione**: Aggiorna l'immagine profilo.

**Parametri** <br>

- **userId**: Id dell'utente specificato <br>
- **imageId**: Id dell'immagine del profilo. <br>
- **redirectAttributes**: Attributi che vengono usati per gestire messaggi di errore o di successo.

**Ritorna**: Restituisce la vista del profilo con l'immagine aggiornata.

---

`setImg(Model model)`

```java
   public String setImg(Model model)
```
**Descrizione**: Imposta un immagine profilo presa da una lista random.

**Parametri** <br>

- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Restituisce la vista del profilo con l'immagine selezionata.

---

 `changePasswordView(@AuthenticationPrincipal Object principal, Model model)`

```java
   public String changePasswordView(@AuthenticationPrincipal Object principal, Model model)
```
**Descrizione**: Mostra la pagina per cambiare password.

**Parametri** <br>

- **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Restituisce la vista per il cambio password.

---

 `changePassword(@AuthenticationPrincipal Object principal,@RequestParam Map<String, String> formData,  RedirectAttributes redirectAttributes, HttpSession session)`

```java
     public String changePassword(@AuthenticationPrincipal Object principal,
                                 @RequestParam Map<String, String> formData, 
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session)
```
**Descrizione**: Controlla la password nuova se viene inserita correttamente

**Parametri** <br>

- **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
- **formData**: Struttura dati che raccoglie i dati da un modulo HTML tramite POST. <br>
- **redirectAttributes**: Attributi che vengono usati per gestire messaggi di errore o di successo. <br>
- **session**: Gestisce la sessione utente.

**Ritorna**: Reindirizza la vista per il login se la nuova password è scritta correttamente altrimenti compare messaggio di errore e reindirizza alla vista del cambio password.

---

 `deleteUserView(@AuthenticationPrincipal Object principal, Model model)`

```java
     public String deleteUserView(@AuthenticationPrincipal Object principal, Model model)
```
**Descrizione**: Mostra pagina di eliminazione profilo e quello che stai per cancellare dal profilo.

**Parametri** <br>

- **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html.

**Ritorna**: Reindirizza la vista per eliminazione account.

---

`deleteUser(@RequestParam String userId)`

```java
    public String deleteUser(@RequestParam String userId)
```
**Descrizione**: Conferma per cancellare il profilo.

**Parametri** <br>

- **userId**: Id utente a cui si sta cancellando il profilo.

**Ritorna**: Reindirizza la vista di logout.

---

`getProfile(Model model, @AuthenticationPrincipal Object principal, HttpServletRequest request)`

```java
      public String getProfile(Model model, @AuthenticationPrincipal Object principal, HttpServletRequest request)
```
**Descrizione**: Modifica username

**Parametri** <br>

- **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
- **model**: Oggetto che viene usato per visualizzare i dati nella pagina html. <br>
- **request**: Oggetto HttpServletRequest che fornisce informazioni dettagliate sulla richiesta HTTP, inclusi eventuali codici di stato di errore.

**Ritorna**: Reindirizza la vista per modificare l'username.

---

`updateNickname(@AuthenticationPrincipal Object principal, @RequestBody Map<String, String> requestBody,HttpServletRequest request)`

```java
       public ResponseEntity<String> updateNickname(@AuthenticationPrincipal Object principal, @RequestBody Map<String, String> requestBody,
       HttpServletRequest request)
```
**Descrizione**: Aggiorna l'username

**Parametri** <br>

- **principal**: Oggetto che contiene le informazioni per l'accesso dell'utente. <br>
- **request**: Oggetto HttpServletRequest che fornisce informazioni dettagliate sulla richiesta HTTP, inclusi eventuali codici di stato di errore. <br>
- **requestBody**: Restituisce direttamente un map.

**Ritorna**: Reindirizza la vista del profilo con username aggiornato.

---
