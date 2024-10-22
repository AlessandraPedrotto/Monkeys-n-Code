package com.monkeysncode.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.monkeysncode.entites.Deck;
import com.monkeysncode.entites.DeckImg;
import com.monkeysncode.entites.User;

import com.monkeysncode.repos.DeckDAO;
import com.monkeysncode.repos.UserDAO;

@Service
public class DeckService {
    
    private final UserDAO userDAO;  // DAO for user data access
    private final DeckDAO deckDAO;  // DAO for deck data access
    
    // Constructor for dependency injection of DAOs
    public DeckService(UserDAO userDAO, DeckDAO deckDAO) {
        this.deckDAO = deckDAO;
        this.userDAO = userDAO;
    }

    // Method for saving or updating a deck, depending on whether the deck ID is provided
    public void saveOrUpdateDeck(String userId, String nameDeck, Optional<Long> deckId, Optional<DeckImg> deckImg) {
        
        // Retrieve user and create a new deck or update an existing one
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (deckId.isEmpty()) {
            // Create new deck if no ID is provided
            Deck newDeck = new Deck();
            deckImg.ifPresent(newDeck::setDeckImg); 
            newDeck.setNameDeck(nameDeck);           
            newDeck.setUser(user);                  
            deckDAO.save(newDeck);                  
        } else {
            // Update existing deck if ID is provided
            Deck existingDeck = deckDAO.findById(deckId.get())
                .orElseThrow(() -> new RuntimeException("Deck not found"));

            deckImg.ifPresent(existingDeck::setDeckImg);  
            existingDeck.setNameDeck(nameDeck);           
            deckDAO.save(existingDeck);                  
        }
    }

    // Method to delete a deck by its ID
    public boolean DeleteDeck(Long deckId) {
        Optional<Deck> deck = this.deckDAO.findById(deckId); 
        if (deck.isPresent()) {
            this.deckDAO.delete(deck.get());  
            return true;
        }
        return false; 
    }

    // Method to retrieve a deck by its ID
    public Optional<Deck> getDeckById(Long id) {
        return deckDAO.findById(id); 
    }

    // Method to retrieve a deck by its name and the associated user
    public Optional<Deck> getDeckByNameDeck(User user, String nameDeck) {
        return deckDAO.findByUserAndNameDeck(user, nameDeck); 
    }
}
