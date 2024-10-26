##Chi siamo

Siamo un gruppo di studenti del ITS ITC Piemonte e stiamo frequentando il corso di Software Developer. Attualmente siamo al primo anno e ci è stato chiesto di sviluppare un'applicazione per un gioco di carte collezionabili.
Ci chiamiamo Monkey's n Code e il team è formato da:

- **Grumeza Catalin**
- **Pedrotto Alessandra**
- **Pelle Alexander**
- **Sicura Samuele**
- **Suppa Alessio**
- **Xing Daniele**

##L'applicazione

###PokeDecks - Organizza, colleziona e domina

*Scopo dell'applicazione:*

Il cliente ha richiesto che venisse creata un'applicazione dedicata al gioco di carte Pokémon che permettesse di fare team building ai suoi dipendenti, unendoli tramite un'attività esterna al lavoro. Ci ha fornito un file .csv che conteneva tutte le informazioni sulle carte con relative immagini.
Durante il colloquio ci è stato spiegato quali fossero i punti fondamentali da dover portare a termine:

- Importare nel database i dati di tutte le carte esistenti
- Ricercare sul database le carte in base a dei filtri
- Registrare degli utenti
- Segnare quali carte e in quante quantità si possiedono
- Salvare dei mazzi facendo attenzione a rispettare i limiti imposti dal regolamento ufficiale
- Sviluppare un’interfaccia frontend essenziale che permetta di utilizzare le funzionalità backend
- L’applicazione dovrà inoltre prevedere almeno la lingua inglese oltre a quella italiana
- Sviluppare l’applicazione in Java

*Come abbiamo soddisfatto la richiesta:*
Oltre alle richieste base, abbiamo deciso di aggiungere altre funzionalità per arricchire il progetto e renderlo più unico. 
Partendo dalla base abbiamo:

1. **Registrazione Utenti:** Abbiamo implementato un'iscrizione "classica" con nickname (non univoco), email e password.

2. **Gestione Collezione Carte:** Gli utenti possono aggiungere carte alla propria collezione personale, specificando la quantità posseduta tramite dei pulsanti + e - posizionati sopra ad ogni carta oppure cliccando sopra l'immagine si aprirà la pagina dettaglio con le caratteristiche specifiche di quella carta e sotto una barra per poter inserire delle quantità più grandi.
   
3. **Creazione Mazzi:** Gli utenti possono creare e salvare mazzi di carte e grazie alla validazione si potrà vedere se rispetta o meno il regolamento ufficiale. I mazzi possono essere creati anche con carte che non si possiedono e una volta convalidato verranno evidenziate le carte che mancano nella collezione.

4. **Ricerca Avanzata:** Abbiamo implementato filtri di ricerca che consentono agli utenti di trovare rapidamente carte specifiche in base a vari criteri: tipo, secondo tipo, super tipo, sottotipo, rarità, nome e set.

5. **Lingua:** Tra le opzioni di lingua sono stati inseriti tedesco, francese e spagnolo (oltre ad inglese e italiano).

Passando invece alle funzionalità aggiuntive ci sono:

1. **Sistema di Follow:** Gli utenti possono seguire i propri amici, creando una rete sociale all'interno dell'applicazione. Questa funzionalità include un sistema di "follower" e "following" per facilitare l'interazione.

2. **Profili pubblici:** Nel profilo pubblico è possibile visualizzare le statistiche di quel specifico utente, i mazzi creati da lui, nickname, immagine profilo, stato online/offline e il followers.

3. **Profili privati:** Nel profilo privato è possibile visualizzare tutte le cose di quello pubblico, ma in aggiunta si avranno dei tasti che serviranno a: visualizzare/modificare la propria collezione, cambiare password, eliminare l'account, modificare il nickname e l'immagine profilo. 

4. **Ruoli:** Per poter gestire al meglio l'applicazione abbiamo inserito la figura dell'admin. Esso ha accesso a tutte le funzionalità dell'utente ma in più può registrare le statistiche di ogniuno.
  
5. **Statistiche e Classifiche:** Per gli admin, abbiamo integrato un sistema per registrare le statistiche degli utenti,vittorie e sconfitte, i punti totali vengono calcolati in automatico. Queste informazioni vengono utilizzate per generare una classifica competitiva basata sui punti.

###Tecnologie utilizzate
- **Frontend:** Javascript, HTML e CSS (libreria Bootstrap)
- **Backend:** Java
- **Database:** Mariadb
