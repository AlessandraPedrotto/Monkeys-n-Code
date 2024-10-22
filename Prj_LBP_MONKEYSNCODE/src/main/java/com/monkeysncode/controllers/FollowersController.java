package com.monkeysncode.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.monkeysncode.entites.DeckCards;
import com.monkeysncode.entites.User;
import com.monkeysncode.services.DeckCardsService;
import com.monkeysncode.services.DeckService;
import com.monkeysncode.services.UserService;

@RequestMapping("/user")
@Controller
public class FollowersController { // Controller that manages followers
	
	@Autowired
	UserService userService;
	@Autowired
	DeckCardsService deckCardsService;
	@Autowired
	DeckService deckService;
	
	@GetMapping("/{userId}") // Check if the user is logged in
	public String user(@AuthenticationPrincipal Object principal, @PathVariable String userId, Model model) {
		User user = userService.getUserById(userId);
		User loggedUser = userService.userCheck(principal);
		model.addAttribute("user", user);
		model.addAttribute("followers", userService.getNumFollowers(userId));
		model.addAttribute("following", userService.getNumFollowing(userId));
		model.addAttribute("isFollowing", userService.isFollowing(loggedUser.getId(), userId));
		if(loggedUser.getId().equals(userId)) {
			return "redirect:/profile";
		}
		return "user";
	}
	
	// Method that concerns when you press the follow button
	@PostMapping("/follow") 
    public String follow(@AuthenticationPrincipal Object principal, @RequestParam String user) {

    	User loggedUser = userService.userCheck(principal);
    	userService.followUser(loggedUser.getId(),user);
    	return "redirect:/user/" + user;
    	
    }
	
	// Method that shows followers
	@GetMapping("/{userId}/followers")
	public String seeFollowers(@PathVariable String userId, Model model) {
		model.addAttribute("followersList", userService.getFollowers(userId));
		return "seeUserFollowers";
	}

    // Method that shows the users you follow
	@GetMapping("/{userId}/following")
	public String seeFollowing(@PathVariable String userId, Model model) {
		Set<User> list = userService.getFollowing(userId);
		model.addAttribute("followingList", list);
		return "seeUserFollowing";
	}
	
	// Method that concerns when you press the unfollow button
	@PostMapping("/unfollow")
    public String unFollow(@AuthenticationPrincipal Object principal,@RequestParam String user) {

    	User loggedUser = userService.userCheck(principal);
    	userService.unfollowUser(loggedUser.getId(), user);
    	return "redirect:/user/"+user;
    	
    }
	
	@GetMapping("/deck/{deckId}")
	public String deckView(@PathVariable Long deckId,Model model) {
		List<DeckCards> originalCards = deckCardsService.getDeckCards(deckId);
        List<DeckCards> displayCards = new ArrayList<>();
        for (DeckCards card : originalCards) {
            for (int i = 0; i < card.getCardQuantity(); i++) {
                displayCards.add(card);
            }
        }
        model.addAttribute("deckName", deckService.getDeckById(deckId).get().getNameDeck());
        model.addAttribute("deckId", deckId);
        model.addAttribute("deckCards", displayCards);
		return "userDeckView";
	}
	
}
