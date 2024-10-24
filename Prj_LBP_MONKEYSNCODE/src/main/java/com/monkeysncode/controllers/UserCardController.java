package com.monkeysncode.controllers;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.monkeysncode.entites.User;
import com.monkeysncode.services.CardService;
import com.monkeysncode.services.UserCardsService;
import com.monkeysncode.services.UserService;
import com.monkeysncode.entites.Card;

@Controller
public class UserCardController {  // Controller who manages the card quantity
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private UserCardsService usercardsService;
	
	@Autowired
	private CardService cardService;
	
	// Endpoint to set the quantity of a card within a user's collection
	@PostMapping("/userCard")
	public String addCardToUser(
	        @AuthenticationPrincipal Object principal, 
	        @RequestParam String cardId, 
	        @RequestParam int quantity,
	        @RequestParam(required = false) String from,
	        @RequestParam(required = false) Boolean owned,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String set,
	        @RequestParam(required = false) String types,
	        @RequestParam(required = false) String rarity,
	        @RequestParam(required = false) String supertype,
	        @RequestParam(required = false) String subtypes,
	        @RequestParam(required = false) String sort,
	        @RequestParam(required = false) Boolean desc,
	        @RequestParam(required = false) Integer page) {

	    // Check the authenticated user
	    User user = userService.userCheck(principal);

	    // Find the card by its ID
	    Optional<Card> card = cardService.getCardById(cardId);

	    if (card.isPresent()) {
	        // Add or update the card in the user's collection
	        usercardsService.addOrUpdateCard(user, card.get(), quantity);

	        // Create the redirect URL with filter parameters
	        String redirectUrl = "redirect:/card/" + card.get().getId() +
	                             "?from=" + (from != null ? from : "") +
	                             "&owned=" + (owned != null ? owned : false) +
	                             "&name=" + (name != null ? name : "") +
	                             "&set=" + (set != null ? set : "") +
	                             "&types=" + (types != null ? types : "") +
	                             "&rarity=" + (rarity != null ? rarity : "") +
	                             "&supertype=" + (supertype != null ? supertype : "") +
	                             "&subtypes=" + (subtypes != null ? subtypes : "") +
	                             "&sort=" + (sort != null ? sort : "name") +
	                             "&desc=" + (desc != null ? desc : false) +
	                             "&page=" + (page != null ? page : 1);

	        // Redirect to the card page with filter parameters
	        return redirectUrl;
	    } else {
	        // Return "error" if the card is not found
	        return "Error";
	    }
	}


	@GetMapping("/users/{userId}/totalCards")
    public Integer getTotalCards(@PathVariable String userId) {
        return usercardsService.getTotalCards(userId);
    }
}
