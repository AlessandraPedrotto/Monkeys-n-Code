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
	    @RequestParam(defaultValue = "false") boolean grayFilter // Added grayFilter parameter to manage gray effect
	) {
	    
	    // Ensure that the block (blocco) value is at least 1
	    if (blocco < 1) { 
	        blocco = 1; // Set to 1 if the provided block is less than 1
	    }

	    // Verify and retrieve the authenticated user
	    User user = userService.userCheck(principal);
	    int totalCards = usercardService.getTotalCards(user.getId()); // Get the total number of cards owned by the user
	    
	    // Create a map to store filter parameters
	    HashMap<String, String> param = new HashMap<>();
	    param.put("set", set); // Add set parameter
	    param.put("types", types); // Add types parameter
	    param.put("name", name); // Add name parameter
	    param.put("rarity", rarity); // Add rarity parameter
	    param.put("supertype", supertype); // Add supertype parameter
	    param.put("subtypes", subtypes); // Add subtypes parameter
	    param.put("page", String.valueOf(page)); // Add current page number to the parameters
	    param.put("grayFilter", grayFilter ? "true" : "false"); // Add grayFilter status to the parameters

	    // Initialize the list of cards
	    List<Card> cards = new ArrayList<>();
	    
	    // Fetch the collection of cards owned by the user
	    HashMap<String, Integer> ownedCards = usercardService.getCollectionById(user.getId());
	    System.out.println(ownedCards); // Debugging line to print owned cards
	    
	    // Apply filtering based on the "owned" parameter
	    if (owned) {
	        // If the user owns the cards, filter based on the owned collection
	        cards = cardService.filterByParam(param, usercardService.getSortedCollection(user.getId(), sort, desc));
	    } else {
	        // Otherwise, filter the complete card list
	        cards = cardService.filterByParam(param, cardService.findAllSorted(sort, desc));
	    }

	    // Pagination: Display 100 cards per page
	    List<Card> allCards = cardService.getCardsByPage(cards, page, 100); 
	    int totalPages = (int) Math.ceil((double) cards.size() / 100); // Calculate total number of pages

	    // Ensure the current page is within valid bounds
	    if (page < 1) {
	        page = 1; // Set to the first page if the current page is less than 1
	    } else if (page > totalPages) {
	        page = totalPages; // Set to the last page if the current page exceeds bounds
	    }

	    // Manage page blocks (e.g., 5 pages per block)
	    int bloccoDimensione = 5; // Size of the block (number of pages per block)
	    int inizioPagina = (blocco - 1) * bloccoDimensione + 1; // Starting page number for the current block
	    int finePagina = Math.min(blocco * bloccoDimensione, totalPages); // Ending page number for the current block
	    int ultimoBlocco = (int) Math.ceil((double) totalPages / bloccoDimensione); // Total number of blocks

	    // Add necessary attributes to the model for rendering in the view
	    model.addAttribute("bloccoDimensione", bloccoDimensione); // Block size (e.g., 5 pages per block)
	    model.addAttribute("totalPages", totalPages); // Total number of pages
	    model.addAttribute("cards", allCards); // Filtered and paginated cards
	    model.addAttribute("ownedCards", ownedCards); // Cards owned by the user
	    model.addAttribute("from", from); // Original page or referrer
	    model.addAttribute("totalCards", totalCards); // Total number of cards the user owns
	    model.addAttribute("currentPage", page); // Current page number
	    model.addAttribute("inizioPagina", inizioPagina); // Start of the current block
	    model.addAttribute("finePagina", finePagina); // End of the current block
	    model.addAttribute("bloccoCorrente", blocco); // Current block number
	    model.addAttribute("ultimoBlocco", ultimoBlocco); // Last block number
	    model.addAttribute("grayFilter", grayFilter); // Add grayFilter to the model for handling gray effect in view

	    // Add filter parameters to the model to maintain their state
	    param.put("sort", sort); // Add sorting parameter
	    param.put("desc", desc ? "true" : "false"); // Add descending order parameter
	    param.put("owned", owned ? "true" : "false"); // Add ownership filter parameter
	    model.addAttribute("param", param); // Add all parameters to the model

	    // Return the view for displaying the cards
	    return "cards"; // Return the name of the view to render
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
