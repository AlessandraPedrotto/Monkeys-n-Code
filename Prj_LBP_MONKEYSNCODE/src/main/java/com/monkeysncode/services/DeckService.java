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
    
    private final UserDAO userDAO;
    private final DeckDAO deckDAO;

    public DeckService(UserDAO userDAO, DeckDAO deckDAO) {
        this.deckDAO = deckDAO;
        this.userDAO = userDAO;
    }

    // Metodo per creare o aggiornare un deck
    public void saveOrUpdateDeck(String userId, String nameDeck, Long deckId, DeckImg deckImg) {
        // Recupera l'utente dal database
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User non trovato"));

        Deck deck;

        if (deckId == null) {
            // Se non viene fornito un ID del deck, crea un nuovo deck
            deck = new Deck();
            deck.setUser(user);  // Associa il mazzo all'utente
        } else {
            // Se viene fornito un ID del deck, aggiorna il deck esistente
            deck = deckDAO.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck non trovato"));
        }

        // Imposta il nome e l'immagine del deck (se fornita)
        deck.setNameDeck(nameDeck);
        if (deckImg != null) {
            deck.setDeckImg(deckImg);
        }

        // Salva il deck (sia per creazione che per aggiornamento)
        deckDAO.save(deck);
    }

    // Metodo per eliminare un deck
    public boolean DeleteDeck(Long deckId) {
        Optional<Deck> deck = deckDAO.findById(deckId);
        if (deck.isPresent()) {
            deckDAO.delete(deck.get());
            return true;
        }
        return false;
    }

    // Recupera un deck per ID
    public Optional<Deck> getDeckById(Long id) {
        return deckDAO.findById(id);
    }

    // Recupera un deck per nome e utente
    public Optional<Deck> getDeckByNameDeck(User user, String nameDeck) {
        return deckDAO.findByUserAndNameDeck(user, nameDeck);
    }
}
