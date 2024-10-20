package com.monkeysncode.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

import com.monkeysncode.entites.Card;
import com.monkeysncode.entites.Deck;
import com.monkeysncode.entites.DeckCards;
import com.monkeysncode.entites.User;
import com.monkeysncode.repos.CardsDAO;
import com.monkeysncode.repos.DeckCardDAO;
import com.monkeysncode.repos.DeckDAO;
import java.util.Map;

@Service
public class DeckCardsService {
	@Autowired
	private  DeckDAO deckDAO;
	@Autowired
    private  CardsDAO cardDAO;
	@Autowired
    private  DeckCardDAO deckCardDAO;
	@Autowired
	private UserCardsService userCardsService;
  
    
    // metodo per aggiungere o aggiornare una nuova relazione tra deck e carte
    @Async
    public CompletableFuture<String> SetCard(Long deckId, String cardId, int quantity) {
    	
    	Optional<Deck> deck = deckDAO.findById(deckId);
    	
    	if (deck.isPresent()) {
    		// controllo che la carta esista
    		Optional<Card> card = cardDAO.findById(cardId);

    		if (card.isPresent()) {
    			
    			// controllo se esiste già un'associazione tra il deck e la carta
    			Optional<DeckCards> existingRelation = deckCardDAO.findByCardIdAndDeckId(cardId, deckId);
    			DeckCards deckCard = new DeckCards();
    			if (existingRelation.isPresent()) {
    				// se esiste aggiorno la nuova quantità
    				deckCard = existingRelation.get();
    				int newQuantity = deckCard.getCardQuantity() + quantity;
    				deckCard.setCardQuantity(newQuantity);
    			} else {
    				// se non esiste creo una nuova relazione
            		deckCard.setCard(card.get());
            		deckCard.setDeck(deck.get());
            		deckCard.setCardQuantity(quantity);
    			}

        		this.deckCardDAO.save(deckCard);
        		return CompletableFuture.completedFuture("Carta aggiunta con successo!");
    		}
    	}
    	
    	return CompletableFuture.completedFuture("Errore Mazzo!");
    }
    
    // metodo per rimuovere una carta dal mazzo
    @Async
    public CompletableFuture<String> RemoveCard(Long deckId, String cardId, int quantity) {
    	
    	Optional<Deck> deck = deckDAO.findById(deckId);
    	
    	if (deck.isPresent()) {
    		// controllo che la carta esista
    		Optional<Card> card = cardDAO.findById(cardId);

    		if (card.isPresent()) {
    			
    			// controllo se esiste già un'associazione tra il deck e la carta
    			Optional<DeckCards> existingRelation = this.deckCardDAO.findByCardIdAndDeckId(cardId, deckId);
    			
    			if (existingRelation.isPresent()) {
    				// se esiste aggiorno la nuova quantità
    				DeckCards deckCard = existingRelation.get();
    				int newQuantity = deckCard.getCardQuantity() - quantity;
    				if (newQuantity <= 0) {
    					deckCardDAO.delete(deckCard);
    				} else {
    					deckCard.setCardQuantity(newQuantity);
    					this.deckCardDAO.save(deckCard);
    				}
    			}
    		}
    		return CompletableFuture.completedFuture("Carta rimossa con successo!");
    	}
    	return CompletableFuture.completedFuture("Errore Mazzo!");
    	// bisognerà gestire l'errore per mancanza deck
    }
    
    
 // Metodo per formattare gli errori con un elenco puntato
    private String formatValidationErrors(List<String> violations) {
        if (violations.isEmpty()) {
            return "Il mazzo è valido!";
        }

        StringBuilder formattedErrors = new StringBuilder("Errori trovati nella validazione del mazzo:<br><br>");

        for (String violation : violations) {
            formattedErrors.append("• ").append(violation).append("<br>"); 
        }

        return formattedErrors.toString();
    	}
    
    public String validateDeck(Long deckId, User user) {
        List<DeckCards> deckCards = getDeckCards(deckId);
        List<String> violations = new ArrayList<>(); // Lista per raccogliere le violazioni
        List<String> missingCards = new ArrayList<>(); // Lista per le carte non possedute dall'utente

        // Controllo se il mazzo è vuoto
        if (deckCards.isEmpty()) {
            violations.add("Il mazzo è vuoto."); 
        }

        // Controllo che il mazzo non superi il limite massimo di 60 carte
        int totalCards = deckCards.stream().mapToInt(DeckCards::getCardQuantity).sum();
        if (totalCards != 60) {
            violations.add("Il mazzo deve contenere esattamente 60 carte. Carte nel mazzo: " + totalCards);
        }

        // Inizializzo il contatore per i Pokémon base e una mappa per tenere traccia delle copie delle carte
        int basicPokemonCount = 0;
        Map<String, Integer> cardCountMap = new HashMap<>();
        Set<String> evolutionCards = new HashSet<>();

        for (DeckCards deckCard : deckCards) {
            Card card = deckCard.getCard();
            String cardName = card.getName();
            String cardSupertype = card.getSupertypes();

            // Controllo se la carta è un Pokémon base
            if ("Pokémon".equals(cardSupertype)) {
                if (card.getEvolvesFrom() == null || card.getEvolvesFrom().isEmpty()) {
                    basicPokemonCount += deckCard.getCardQuantity(); // Conta il numero totale di Pokémon base
                } else {
                    evolutionCards.add(card.getEvolvesFrom()); // Aggiungo il nome del Pokémon base richiesto
                }
            }

            // Conto le copie delle carte, escludendo il tipo "Energy" dal limite di 4 copie
            String cardType = card.getSupertypes();

            if (!"Energy".equals(cardType)) {
                cardCountMap.put(cardName, cardCountMap.getOrDefault(cardName, 0) + deckCard.getCardQuantity());
            }

            // Controllo se l'utente ha abbastanza copie della carta
            int userCardQuantity = userCardsService.getQuantityByCardUser(user, card); // Usa l'oggetto user

            if (userCardQuantity < deckCard.getCardQuantity()) {
                missingCards.add(cardName); // Aggiungi il nome della carta non posseduta
            }
        }

        // Validazione delle regole
        if (basicPokemonCount == 0) {
            violations.add("Il mazzo deve includere almeno un Pokémon base.");
        }

        // Verifica che nessuna carta (escluse le Energy) abbia più di 4 copie
        boolean noExcessCopies = cardCountMap.values().stream().allMatch(count -> count <= 4);
        if (!noExcessCopies) {
            violations.add("Non è possibile avere più di 4 copie per carta (eccetto Energy).");
        }

        // Verifica che ogni evoluzione abbia il suo Pokémon base
        List<String> missingBases = new ArrayList<>(); // Inizializza la lista per i Pokémon base mancanti
        for (String basePokemon : evolutionCards) {
            if (!cardCountMap.containsKey(basePokemon)) {
                missingBases.add(basePokemon); // Aggiunge il Pokémon base mancante
            }
        }

        if (!missingBases.isEmpty()) {
            violations.add("Alcune evoluzioni non hanno il Pokémon base corrispondente: " + String.join(", ", missingBases));
        }

        // Se ci sono carte mancanti, aggiungi il messaggio personalizzato
        if (!missingCards.isEmpty()) {
            if (missingCards.size() == 1) {
                violations.add("Nel mazzo è presente " + missingCards.get(0) + " che non hai ancora nella tua collezione.");
            } else {
                violations.add("Nel mazzo sono presenti carte che non hai nella tua collezione."); 
            }
        }

        // Restituisci la lista delle violazioni con formattazione ordinata
        return formatValidationErrors(violations);
    }





    // recupero la lista di carte dato il deck ID
    public List<DeckCards> getDeckCards(Long deckId) {
    	Optional<Deck> deck = this.deckDAO.findById(deckId);
    	
    	if (deck.isPresent()) {
    		return this.deckCardDAO.findByDeck(deck.get());
    	} else {
    		return new ArrayList<DeckCards>();
    	}
    	
    }
    
    public void deleteCardsFromDeck(Long deckId) {
    	List<DeckCards> lista = getDeckCards(deckId);
		for (DeckCards deckCards : lista) {
			RemoveCard(deckId, deckCards.getCard().getId(), deckCards.getCardQuantity());
		}
    }
}
