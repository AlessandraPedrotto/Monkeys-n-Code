package com.monkeysncode.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.monkeysncode.entites.User;
import com.monkeysncode.services.UserService;

@Controller
public class ClassificationController { // Controller that manage the classification
	
	@Autowired
	UserService userService;
	
	// Get the data for classification
	@GetMapping("/classification")
	public String showClassification(@AuthenticationPrincipal Object principal, Model model)
	{
		User user = userService.userCheck(principal);
		
		List<User> allUsers = userService.getAllUsersOrderedByWin();
	    
		model.addAttribute("idUser", user.getId());
		model.addAttribute("username", user.getName());
		model.addAttribute("mail", user.getEmail());
		model.addAttribute("winUser", user.getWin());
		model.addAttribute("loseUser", user.getLose());
		model.addAttribute("listUsers", allUsers);
		
		return "userClassification";
		
	}
	
	// Shows the classification
	@PostMapping("/classification")
	public String postClassification(@AuthenticationPrincipal Object principal, Model model)
	{
		User user = userService.userCheck(principal);
		
		List<User> allUsers = userService.getAllUsersOrderedByWin();
	    
		model.addAttribute("idUser", user.getId());
		model.addAttribute("username", user.getName());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("winUser", user.getWin());
		model.addAttribute("loseUser", user.getLose());
		model.addAttribute("listUsers", allUsers);
		
		
		return "userClassification";
		
	}
}
