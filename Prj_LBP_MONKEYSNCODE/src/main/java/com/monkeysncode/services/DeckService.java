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
    public void saveOrUpdateDeck(String userId, String nameDeck, Long deckId, DeckImg deckImg) {
        // Retrieve the user from the database
    	User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User non trovato"));

        Deck deck;

        if (deckId == null) {
            // If no deck ID is provided, create a new deck
            deck = new Deck();
            deck.setUser(user);  // Associate the deck with the user
        } else {
            // If a deck ID is provided, update the existing deck
            deck = deckDAO.findById(deckId)
            	.orElseThrow(() -> new RuntimeException("Deck non trovato"));
        }

        // Set the name and image of the deck (if provided)
        deck.setNameDeck(nameDeck);
        if (deckImg != null) {
            deck.setDeckImg(deckImg);
        }

        // Save the deck (both for creation and update)
        deckDAO.save(deck);
    }

    // Method to delete a deck by its ID
    public boolean DeleteDeck(Long deckId) {
        Optional<Deck> deck = deckDAO.findById(deckId);
        if (deck.isPresent()) {
            deckDAO.delete(deck.get());
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
