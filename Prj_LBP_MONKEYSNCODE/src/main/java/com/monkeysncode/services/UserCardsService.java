package com.monkeysncode.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monkeysncode.entites.Card;
import com.monkeysncode.entites.User;
import com.monkeysncode.entites.UserCards;
import com.monkeysncode.repos.UserCardDAO;

@Service
public class UserCardsService {
    
    @Autowired
    private UserCardDAO dao;  // DAO for accessing user card collection

    // Method to get the total number of cards for a user
    public int getTotalCards(String userId) {
        List<UserCards> userCards = dao.findByUserId(userId);

        if (userCards == null || userCards.isEmpty()) {
            return 0;  // Return 0 if the user has no cards
        }

        return userCards.stream().mapToInt(UserCards::getCardQuantity).sum();  // Sum of all card quantities
    }
    
    // Method to retrieve the user's card collection
    public List<UserCards> getUserCollection(String userId) {
        return dao.findByUserId(userId);  // Return all cards for the given user
    }
    
    // Method to get a list of card IDs owned by the user
    public List<String> getCollectionById(String userId) {
        List<UserCards> userCards = dao.findByUserId(userId);
        List<String> ids = new ArrayList<>();
        for (UserCards userCard : userCards) {
            ids.add(userCard.getCard().getId());  // Add card ID to the list
        }
        return ids;
    }

    // Method to get the user's card collection, sorted by a specified parameter
    public List<Card> getSortedCollection(String userId, String sort, boolean desc) {
        List<UserCards> userCards = dao.findByUserId(userId);
        List<Card> cards = new ArrayList<>();
        for (UserCards userCard : userCards) {
            cards.add(userCard.getCard());  // Add card to the list
        }
        cards = sortByParam(cards, sort, desc);  // Sort the cards by the given parameter
        return cards;
    }
    
    // Method to sort cards based on a specific attribute (name, level, Pokedex number, etc.)
    public List<Card> sortByParam(List<Card> cards, String sortBy, boolean desc) {
        Comparator<Card> comparator;

        // Determine the comparator based on the sortBy attribute
        switch (sortBy) {
            case "name":
                comparator = Comparator.comparing(Card::getName);  // Sort by card name
                break;
            case "level":
                comparator = Comparator.comparingInt(card -> {
                    String level = card.getLevel();
                    // Handle cards with empty or invalid levels
                    if (level.isEmpty() && (card.getSupertypes().equals("Trainer") || card.getSupertypes().equals("Energy")))
                        return 0;
                    if (level.isEmpty()) return 1;
                    if (level.equalsIgnoreCase("X")) return 1000;  // Special handling for level "X"
                    return Integer.parseInt(level);
                });
                comparator = comparator.reversed();  // Reverse the sorting for level
                break;
            case "nationalPokedexNumbers":
                comparator = Comparator.comparingInt(card -> {
                    String nationalPokedexNumbers = card.getNationalPokedexNumbers();
                    // Handle invalid or empty Pokedex numbers
                    if (nationalPokedexNumbers.isEmpty() && (card.getSupertypes().equals("Trainer") || card.getSupertypes().equals("Energy")))
                        return 1000;
                    return Integer.parseInt(nationalPokedexNumbers);
                });
                break;
            case "rarity":
                comparator = Comparator.comparing(Card::getRarity);  // Sort by card rarity
                break;
            default:
                throw new IllegalArgumentException("Invalid sort attribute: " + sortBy);  // Handle invalid sorting attribute
        }

        // Reverse the order if desc is true
        if (desc) {
            comparator = comparator.reversed();
        }

        Collections.sort(cards, comparator);  // Sort the list of cards
        return cards;
    }
    
    // Method to add or update a card for the user
    public void addOrUpdateCard(User user, Card card, int quantity) {
        UserCards collection = dao
                .findByUserAndCard(user, card)
                .orElse(new UserCards());  // Retrieve existing or create a new card for the user
        collection.setUser(user);
        collection.setCard(card);
        collection.setCardQuantity(quantity);  // Set the quantity of the card

        dao.save(collection);  // Save to the database
        cleanDb(user.getId());  // Clean up database by removing cards with zero quantity
    }
    
    // Method to add or remove a certain quantity of a card for the user
    public void addOrRemoveCard(User user, Card card, int quantity) {
        UserCards collection = dao
                .findByUserAndCard(user, card)
                .orElse(new UserCards());
        int prevQuantity = collection.getCardQuantity();  // Get previous card quantity
        collection.setUser(user);
        collection.setCard(card);
        collection.setCardQuantity(prevQuantity + quantity);  // Update card quantity

        dao.save(collection);  // Save changes to the database
        cleanDb(user.getId());  // Clean up cards with zero quantity
    }
    
    // Method to remove cards with zero quantity from the database
    public void cleanDb(String userId) {
        List<UserCards> list = dao.findByUserId(userId);  // Retrieve user's card list
        for (UserCards userCards : list) {
            if (userCards.getCardQuantity() <= 0) {
                dao.delete(userCards);  // Delete cards with zero quantity
            }
        }
    }

    // Method to get the quantity of a specific card for a user
    public int getQuantityByCardUser(User user, Card card) {
        Optional<UserCards> userCards = dao.findByUserAndCard(user, card);
        return userCards.map(UserCards::getCardQuantity).orElse(0);  // Return quantity or 0 if not found
    }
}
