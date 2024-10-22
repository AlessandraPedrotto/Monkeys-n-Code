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

// Service class for managing deck-card relationships
@Service
public class DeckCardsService {
    
    // Injecting required DAOs for database operations
    @Autowired
    private DeckDAO deckDAO;
    
    @Autowired
    private CardsDAO cardDAO;
    
    @Autowired
    private DeckCardDAO deckCardDAO;
    
    @Autowired
    private UserCardsService userCardsService;
  
    // Asynchronous method to add or update the relationship between a deck and cards
    @Async
    public CompletableFuture<String> SetCard(Long deckId, String cardId, int quantity) {
        
        // Retrieve the deck by its ID
        Optional<Deck> deck = deckDAO.findById(deckId);
        
        if (deck.isPresent()) {
            // Check if the card exists
            Optional<Card> card = cardDAO.findById(cardId);

            if (card.isPresent()) {
                
                // Check if a relationship between the deck and the card already exists
                Optional<DeckCards> existingRelation = deckCardDAO.findByCardIdAndDeckId(cardId, deckId);
                DeckCards deckCard = new DeckCards();
                if (existingRelation.isPresent()) {
                    // If it exists, update the quantity
                    deckCard = existingRelation.get();
                    int newQuantity = deckCard.getCardQuantity() + quantity;
                    deckCard.setCardQuantity(newQuantity);
                } else {
                    // If it does not exist, create a new relationship
                    deckCard.setCard(card.get());
                    deckCard.setDeck(deck.get());
                    deckCard.setCardQuantity(quantity);
                }

                // Save the updated or newly created relationship
                this.deckCardDAO.save(deckCard);
                return CompletableFuture.completedFuture("Card added successfully!");
            }
        }
        
        // Return an error message if the deck does not exist
        return CompletableFuture.completedFuture("Deck error!");
    }
    
    // Asynchronous method to remove a card from the deck
    @Async
    public CompletableFuture<String> RemoveCard(Long deckId, String cardId, int quantity) {
        
        // Retrieve the deck by its ID
        Optional<Deck> deck = deckDAO.findById(deckId);
        
        if (deck.isPresent()) {
            // Check if the card exists
            Optional<Card> card = cardDAO.findById(cardId);

            if (card.isPresent()) {
                
                // Check if a relationship between the deck and the card exists
                Optional<DeckCards> existingRelation = this.deckCardDAO.findByCardIdAndDeckId(cardId, deckId);
                
                if (existingRelation.isPresent()) {
                    // If it exists, update the quantity
                    DeckCards deckCard = existingRelation.get();
                    int newQuantity = deckCard.getCardQuantity() - quantity;
                    if (newQuantity <= 0) {
                        // If quantity is zero or less, delete the relationship
                        deckCardDAO.delete(deckCard);
                    } else {
                        // Otherwise, save the updated quantity
                        deckCard.setCardQuantity(newQuantity);
                        this.deckCardDAO.save(deckCard);
                    }
                }
            }
            return CompletableFuture.completedFuture("Card removed successfully!");
        }
        // Return an error message if the deck does not exist
        return CompletableFuture.completedFuture("Deck error!");
    }
    
    // Method to format validation errors into a bullet-point list
    private String formatValidationErrors(List<String> violations) {
        if (violations.isEmpty()) {
            return "The deck is valid!";
        }

        StringBuilder formattedErrors = new StringBuilder("Errors found in deck validation:<br><br>");

        // Append each violation to the formatted string
        for (String violation : violations) {
            formattedErrors.append("• ").append(violation).append("<br>"); 
        }

        return formattedErrors.toString();
    }
    
    // Validate the deck against specific rules
    public String validateDeck(Long deckId, User user) {
        List<DeckCards> deckCards = getDeckCards(deckId); // Retrieve the cards in the deck
        List<String> violations = new ArrayList<>(); // List to collect violations
        List<String> missingCards = new ArrayList<>(); // List for cards not owned by the user

        // Check if the deck is empty
        if (deckCards.isEmpty()) {
            violations.add("The deck is empty."); 
        }

        // Check that the deck does not exceed the maximum limit of 60 cards
        int totalCards = deckCards.stream().mapToInt(DeckCards::getCardQuantity).sum();
        if (totalCards != 60) {
            violations.add("The deck must contain exactly 60 cards. Cards in the deck: " + totalCards);
        }

        // Initialize counter for basic Pokémon and a map for tracking copies of cards
        int basicPokemonCount = 0;
        Map<String, Integer> cardCountMap = new HashMap<>();
        Set<String> evolutionCards = new HashSet<>();

        for (DeckCards deckCard : deckCards) {
            Card card = deckCard.getCard();
            String cardName = card.getName();
            String cardSupertype = card.getSupertypes();

            // Check if the card is a basic Pokémon
            if ("Pokémon".equals(cardSupertype)) {
                if (card.getEvolvesFrom() == null || card.getEvolvesFrom().isEmpty()) {
                    basicPokemonCount += deckCard.getCardQuantity(); // Count total basic Pokémon
                } else {
                    evolutionCards.add(card.getEvolvesFrom()); // Add required basic Pokémon name
                }
            }

            // Count copies of cards, excluding "Energy" type from the 4-copy limit
            String cardType = card.getSupertypes();

            if (!"Energy".equals(cardType)) {
                cardCountMap.put(cardName, cardCountMap.getOrDefault(cardName, 0) + deckCard.getCardQuantity());
            }

            // Check if the user has enough copies of the card
            int userCardQuantity = userCardsService.getQuantityByCardUser(user, card); // Use the user object

            if (userCardQuantity < deckCard.getCardQuantity()) {
                missingCards.add(cardName); // Add name of the card not owned
            }
        }

        // Validation rules
        if (basicPokemonCount == 0) {
            violations.add("The deck must include at least one basic Pokémon.");
        }

        // Ensure no card (excluding Energy) has more than 4 copies
        boolean noExcessCopies = cardCountMap.values().stream().allMatch(count -> count <= 4);
        if (!noExcessCopies) {
            violations.add("You cannot have more than 4 copies of a card (except Energy).");
        }

        // Verify that every evolution has its corresponding basic Pokémon
        List<String> missingBases = new ArrayList<>(); // Initialize list for missing basic Pokémon
        for (String basePokemon : evolutionCards) {
            if (!cardCountMap.containsKey(basePokemon)) {
                missingBases.add(basePokemon); // Add missing basic Pokémon
            }
        }

        if (!missingBases.isEmpty()) {
            violations.add("Some evolutions do not have the corresponding basic Pokémon: " + String.join(", ", missingBases));
        }

        // If there are missing cards, add a personalized message
        if (!missingCards.isEmpty()) {
            if (missingCards.size() == 1) {
                violations.add("The deck includes " + missingCards.get(0) + " which you do not yet have in your collection.");
            } else {
                violations.add("The deck contains cards that you do not own."); 
            }
        }

        // Return the list of violations with formatted output
        return formatValidationErrors(violations);
    }

    // Retrieve the list of cards given the deck ID
    public List<DeckCards> getDeckCards(Long deckId) {
        Optional<Deck> deck = this.deckDAO.findById(deckId);
        
        if (deck.isPresent()) {
            return this.deckCardDAO.findByDeck(deck.get());
        } else {
            return new ArrayList<DeckCards>(); // Return an empty list if the deck does not exist
        }
    }
    
    // Method to delete all cards from a deck
    public void deleteCardsFromDeck(Long deckId) {
        List<DeckCards> cardList = getDeckCards(deckId);
        // Loop through each card in the deck and remove it
        for (DeckCards deckCards : cardList) {
            RemoveCard(deckId, deckCards.getCard().getId(), deckCards.getCardQuantity());
        }
    }
}
