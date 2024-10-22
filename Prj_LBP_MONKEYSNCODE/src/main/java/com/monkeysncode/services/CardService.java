package com.monkeysncode.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.monkeysncode.entites.Card;
import com.monkeysncode.repos.CardsDAO;

// Annotation to indicate that this class is a Spring service
@Service
public class CardService {
    
    // Injection of the repository to interact with the card database
    @Autowired
    private CardsDAO cardDAO;
    
    // Returns all the cards
    public List<Card> findALL(){
        return cardDAO.findAll();
    }
    
    // Returns all the cards sorted by a specified field
    public List<Card> findAllSorted(String sort, boolean desc){
        List<Card> cards = cardDAO.findAll(); // Retrieves all cards
        Comparator<Card> comparator; // Definition of a comparator for sorting
        
        // Sorting by level
        if(sort.equals("level")) {
            comparator = Comparator.comparingInt(card -> {
                String level = card.getLevel();
                // If the level is empty or invalid, return a default value
                if(level.isEmpty() && (card.getSupertypes().equals("Trainer") || card.getSupertypes().equals("Energy")))
                    return 0;
                else {
                    if(level.isEmpty())
                        return 1; // Handles empty levels
                    if(level.equalsIgnoreCase("X"))
                        return 1000; // Handles a special level
                }
                try {
                    return Integer.parseInt(level); // Returns the level as an integer
                } catch (NumberFormatException e) {
                    return 0; // Returns a default value in case of error
                }
            });
            if (desc) {
                comparator = comparator.reversed(); // Reverses the sorting if requested
            }

            Collections.sort(cards, comparator); // Sorts the cards
            return cards; // Returns the sorted list
        }
        
        // Sorting by national Pokédex number
        if(sort.equals("nationalPokedexNumbers")) {
            comparator = Comparator.comparingInt(card -> {
                String nationalPokedexNumbers = card.getNationalPokedexNumbers();
                // If the number is empty or invalid, return a default value
                if(nationalPokedexNumbers.isEmpty() && (card.getSupertypes().equals("Trainer") || card.getSupertypes().equals("Energy")))
                    return 1000;
                try {
                    return Integer.parseInt(nationalPokedexNumbers); // Returns the number as an integer
                } catch (NumberFormatException e) {
                    return 1000; // Returns a default value in case of error
                }
            });

            if (desc) {
                comparator = comparator.reversed(); // Reverses the sorting if requested
            }

            Collections.sort(cards, comparator); // Sorts the cards
            return cards; // Returns the sorted list
        } else {
            // Default sorting via the repository
            if(desc)
                return cardDAO.findAll(Sort.by(Sort.Direction.DESC, sort));
            else 
                return cardDAO.findAll(Sort.by(Sort.Direction.ASC, sort));
        }
    }
    
    // Filters the cards based on the provided parameters
    public List<Card> filterByParam(HashMap<String,String> filters, List<Card> lista){
        
        ArrayList<Card> lista2 = new ArrayList<>();
        
        // Checks if there are no filters
        if (checkHashNull(filters))
            return lista; // Returns the entire list if there are no filters
        
        // Applies the specified filters
        if(filters.get("set") != null && !filters.get("set").isEmpty()) {
            for (Card carta : lista){
                if(containsIgnoreCase(carta.getSet(), filters.get("set")))
                    lista2.add(carta); // Adds card if the set matches
            }
            lista = lista2; // Updates the list with the filtered cards
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(filters.get("types") != null && !filters.get("types").isEmpty()) {
            for (Card carta : lista){
                if(carta.getTypes().equals(filters.get("types")))
                    lista2.add(carta); // Adds card if the type matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(filters.get("name")!= null && !filters.get("name").isEmpty()) {
            for (Card carta : lista){
                if(containsIgnoreCase(carta.getName(), filters.get("name")))
                    lista2.add(carta); // Adds card if the name matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(filters.get("rarity") != null && !filters.get("rarity").isEmpty()) {
            for (Card carta : lista){
                if(carta.getRarity().equals(filters.get("rarity")))
                    lista2.add(carta); // Adds card if the rarity matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(filters.get("supertype") != null && !filters.get("supertype").isEmpty()) {
            for (Card carta : lista){
                if(carta.getSupertypes().equals(filters.get("supertype")))
                    lista2.add(carta); // Adds card if the supertype matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(filters.get("subtypes") != null && !filters.get("subtypes").isEmpty()) {
            for (Card carta : lista){
                if(carta.getSubtypes().equals(filters.get("subtypes")))
                    lista2.add(carta); // Adds card if the subtype matches
            }
            lista = lista2; // Updates the list
        }
        
        return lista; // Returns the filtered list
    }
    
    // Checks if all values in the filter are null
    private boolean checkHashNull(HashMap<String, String> filters) {
        int count=0;
        for (String i : filters.keySet()) {
            if(filters.get(i) == null)
                count++;
        }
        return count == filters.size(); // Returns true if all values are null
    }
    
    // Returns the filtered cards based on the provided parameters
    public List<Card> findByParam(String set, String types, String name, String rarity, String supertype, String subtypes, String sort, boolean desc){
        
        ArrayList<Card> lista = new ArrayList<>();
        // Retrieves all cards sorted, if requested
        if (sort != null)
            if(desc)
                lista = (ArrayList<Card>) cardDAO.findAll(Sort.by(Sort.Direction.DESC, sort));
            else 
                lista = (ArrayList<Card>) cardDAO.findAll(Sort.by(Sort.Direction.ASC, sort));
        else 
            lista = (ArrayList<Card>) cardDAO.findAll(Sort.unsorted());

        ArrayList<Card> lista2 = new ArrayList<>();
        
        // Checks how many parameters are not null
        if (checkNull(set, types, name, rarity, supertype, subtypes) == 0)
            return lista; // Returns the entire list if there are no filters
        
        // Applies the specified filters
        if(set != null && !set.isEmpty()) {
            for (Card carta : lista){
                if(containsIgnoreCase(carta.getSet(), set))
                    lista2.add(carta); // Adds card if the set matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(types != null && !types.isEmpty()) {
            for (Card carta : lista){
                if(carta.getTypes().equals(types))
                    lista2.add(carta); // Adds card if the type matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(name != null && !name.isEmpty()) {
            for (Card carta : lista){
                if(containsIgnoreCase(carta.getName(), name))
                    lista2.add(carta); // Adds card if the name matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(rarity != null && !rarity.isEmpty()) {
            for (Card carta : lista){
                if(carta.getRarity().equals(rarity))
                    lista2.add(carta); // Adds card if the rarity matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(supertype != null && !supertype.isEmpty()) {
            for (Card carta : lista){
                if(carta.getSupertypes().equals(supertype))
                    lista2.add(carta); // Adds card if the supertype matches
            }
            lista = lista2; // Updates the list
            lista2 = new ArrayList<>(); // Resets the temporary list
        }
        
        if(subtypes != null && !subtypes.isEmpty()) {
            for (Card carta : lista){
                if(carta.getSubtypes().equals(subtypes))
                    lista2.add(carta); // Adds card if the subtype matches
            }
            lista = lista2; // Updates the list
        }
        
        return lista; // Returns the filtered list
    }
    
    // Checks if a string contains another string in a case-insensitive manner
    private boolean containsIgnoreCase(String str, String check) {
        if (check == null || check.length() == 0)
            return true; // If the string to check is empty, return true
        return str.toLowerCase().contains(check.toLowerCase()); // Returns true if it contains
    }
    
    // Checks how many parameters are not null
    private int checkNull(String set, String types, String hp, String rarity, String supertype, String subtypes) {
        int i = 0;
        if (set != null)
            i++;
        if (types != null)
            i++;
        if (hp != null)
            i++;
        if (rarity != null)
            i++;
        if (supertype != null)
            i++;
        if (subtypes != null)
            i++;
        return i; // Returns the count of non-null parameters
    }
    
    // Returns a subset of cards based on the page and number of cards per page
    public List<Card> getCardsByPage(List<Card> allCards, int page, int cardsPerPage) {
        // Ensure that the page is not less than 1
        if (page < 1) {
            page = 1;
        }

        // Calculate the starting index
        int startIndex = (page - 1) * cardsPerPage;

        // If the starting index is beyond the size of the list, return an empty list
        if (startIndex >= allCards.size()) {
            return new ArrayList<>();
        }

        // Calculate the ending index
        int endIndex = Math.min(startIndex + cardsPerPage, allCards.size());

        // Return the subset of the list
        return new ArrayList<>(allCards.subList(startIndex, endIndex));
    }
    
    // Returns a card by its ID
    public Optional<Card> getCardById(String cardId){
        return cardDAO.findById(cardId); // Returns the Optional object containing the card
    }

    // Returns a card by its ID, or null if not found
    public Card findById(String cardId) {
        Optional<Card> card = cardDAO.findById(cardId);
        return card.orElse(null); // Returns the Card if present, otherwise null
    }
}
