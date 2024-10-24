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
	    @RequestParam(defaultValue = "1") int blocco,
	    @RequestParam(defaultValue = "true") boolean grayFilter // Added grayFilter parameter to manage the gray effect
	) {
	    // Ensure the block is at least 1
	    if (blocco < 1) { 
	        blocco = 1;
	    }

	    // Check and retrieve the authenticated user
	    User user = userService.userCheck(principal);
	    int totalCards = usercardService.getTotalCards(user.getId());
	    
	    // Create a map to store the filter parameters
	    HashMap<String, String> param = new HashMap<>();
	    param.put("set", set);
	    param.put("types", types);
	    param.put("name", name);
	    param.put("rarity", rarity);
	    param.put("supertype", supertype);
	    param.put("subtypes", subtypes);
	    param.put("page", String.valueOf(page));

	    // Initialize card list
	    List<Card> cards = new ArrayList<>();
	    
	    // Fetch the collection of owned cards for the user
	    HashMap<String,Integer> ownedCards = usercardService.getCollectionById(user.getId());
	    System.out.println(ownedCards);
	    
	    // Apply the filter based on the "owned" parameter
	    if (owned) {
	        cards = cardService.filterByParam(param, usercardService.getSortedCollection(user.getId(), sort, desc));
	    } else {
	        cards = cardService.filterByParam(param, cardService.findAllSorted(sort, desc));
	    }

	    // Pagination: Display 100 cards per page
	    List<Card> allCards = cardService.getCardsByPage(cards, page, 100); 
	    int totalPages = (int) Math.ceil((double) cards.size() / 100);

	    // Ensure current page is within valid bounds
	    if (page < 1) {
	        page = 1;  // Set to first page
	    } else if (page > totalPages) {
	        page = totalPages;  // Set to last page if exceeding bounds
	    }

	    // Manage page blocks (e.g., 5 pages per block)
	    int bloccoDimensione = 5;
	    int inizioPagina = (blocco - 1) * bloccoDimensione + 1;
	    int finePagina = Math.min(blocco * bloccoDimensione, totalPages);
	    int ultimoBlocco = (int) Math.ceil((double) totalPages / bloccoDimensione);

	    // Add necessary attributes to the model for rendering in the view
	    model.addAttribute("bloccoDimensione", bloccoDimensione);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("cards", allCards); // Filtered and paginated cards
	    model.addAttribute("ownedCards", ownedCards); // Cards owned by the user
	    model.addAttribute("from", from);
	    model.addAttribute("totalCards", totalCards); // Total number of cards the user owns
	    model.addAttribute("currentPage", page); // Current page number
	    model.addAttribute("inizioPagina", inizioPagina); // Start of the current block
	    model.addAttribute("finePagina", finePagina); // End of the current block
	    model.addAttribute("bloccoCorrente", blocco); // Current block
	    model.addAttribute("ultimoBlocco", ultimoBlocco); // Last block
	    model.addAttribute("grayFilter", grayFilter); // Add grayFilter to the model to handle gray effect

	    // Add filter parameters to the model to maintain their state
	    param.put("sort", sort);
	    param.put("desc", desc ? "true" : "false");
	    param.put("owned", owned ? "true" : "false");
	    model.addAttribute("param", param);

	    // Return the view for displaying the cards
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
