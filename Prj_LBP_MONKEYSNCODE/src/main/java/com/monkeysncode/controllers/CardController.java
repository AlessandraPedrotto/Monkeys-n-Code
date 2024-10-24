package com.monkeysncode.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.monkeysncode.entites.Card;
import com.monkeysncode.entites.User;
import com.monkeysncode.services.CardService;
import com.monkeysncode.services.UserCardsService;
import com.monkeysncode.services.UserService;
import com.nimbusds.jose.shaded.gson.Gson;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CardController { // Controller who manages the user card collection
	
	@Autowired
	private CardService cardService;
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private UserCardsService usercardService;
	
	@GetMapping("/cards")
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
        @RequestParam(defaultValue = "1") int blocco){
		
		if(blocco < 1) { 
	    	blocco = 1;
	    }
    	
		User user = userService.userCheck(principal);
		int totalCards = usercardService.getTotalCards(user.getId());
		HashMap<String, String> param = new HashMap<String, String>(); 
	 	param.put("set", set); // Associate the value with the map key
	 	param.put("types", types);
	 	param.put("name", name);
	 	param.put("rarity", rarity);
	 	param.put("supertype", supertype);
	 	param.put("subtypes", subtypes);
	 	param.put("page", String.valueOf(page));
	 	
		List<Card> cards = new ArrayList<Card>();
		HashMap<String,Integer> ownedCards = usercardService.getCollectionById(user.getId()); // Creates the collection of all cards owned by the user
		System.out.println(ownedCards);
		Gson gson = new Gson();
		String json = gson.toJson(ownedCards);
		System.out.println(json);
		
		// Based on the owned parameter it takes the list from two different services
	 	if(owned == true)
	 		cards = cardService.filterByParam(param, usercardService.getSortedCollection(user.getId(),sort,desc));
	 	else cards = cardService.filterByParam(param, cardService.findAllSorted(sort,desc));
	 	
	 	// Pagination
	 	List<Card> allCards = cardService.getCardsByPage(cards, page, 100); // Make a list of how many cards should fit per page
	 	
	    int totalPages = (int) Math.ceil((double) cards.size() / 100);
	    
	    if (page < 1) 
	    {
	        page = 1;  // Set to first page if index is less than 1
	    } else if (page > totalPages) 
	    {
	        page = totalPages; // Set to last page if index is greater than maximum
	    }

	    // Page block management (15 pages per block)
	    int bloccoDimensione = 5;
	    int inizioPagina = (blocco - 1) * bloccoDimensione +1;
	    int finePagina = Math.min(blocco * bloccoDimensione, totalPages);
	    int ultimoBlocco = (int) Math.ceil((double) totalPages / bloccoDimensione);

	    // The parameters are added to the page cards
	    model.addAttribute("bloccoDimensione", bloccoDimensione);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("cards", allCards);
	    model.addAttribute("ownedCards", ownedCards);
	    model.addAttribute("from", from);
	    model.addAttribute("totalCards", totalCards);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("inizioPagina", inizioPagina);
	    model.addAttribute("finePagina", finePagina);
	    model.addAttribute("bloccoCorrente", blocco);
	    model.addAttribute("ultimoBlocco", ultimoBlocco); 
	    
	    param.put("sort", sort);
	 	param.put("desc", desc!= true ? "false" : "true");
	 	param.put("owned", owned!= true ? "false" : "true");
	 	model.addAttribute("param", param);

        return "cards";
	 }
	
	@PostMapping("/collection/add")
    @ResponseBody
    @Async
    public CompletableFuture<String> addCardToCollection(@AuthenticationPrincipal Object principal, @RequestParam String cardId) {
		User user = userService.userCheck(principal);
		Card card = cardService.findById(cardId);
		return usercardService.addOrRemoveCard(user, card, 1);
    }
	
	@PostMapping("/collection/remove")
    @ResponseBody
    @Async
    public CompletableFuture<String> removeCard(@AuthenticationPrincipal Object principal, @RequestParam String cardId) {
		User user = userService.userCheck(principal);
		Card card = cardService.findById(cardId);
		return usercardService.addOrRemoveCard(user, card, -1);
    }

	
	 @GetMapping("/card/{cardId}")
	 public String viewCard(@AuthenticationPrincipal Object principal, @PathVariable("cardId") String cardId, Model model) {

	     Optional<Card> cardOptional = cardService.getCardById(cardId);
	     User user = userService.userCheck(principal);
	     
	     // Check if the card is present, update the quantity otherwise print error
	     if (cardOptional.isPresent()) {
	         Card card = cardOptional.get();
	         model.addAttribute("card", card);
	         int quantity = usercardService.getQuantityByCardUser(user, card);
	         model.addAttribute("inCollection", quantity);
	         return "cardView"; 
	     } else {
	         model.addAttribute("errorMessage", "La carta con ID " + cardId + " non esiste.");
	         return "error";
	     }
	 }
}
