package com.monkeysncode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.monkeysncode.entites.User;
import com.monkeysncode.servicies.UserService;

@Controller
public class AuthController {
	@Autowired
	UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Renderizza la pagina di login (con supporto per form e OAuth2)
    }
    

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";  // Renderizza la pagina di registrazione
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,Model model) {
    	boolean exists=userService.exists(user);
    	if(exists) {
    		model.addAttribute("exist", "Errore,Profilo esiste già");
    		return "register";
    	}
        userService.register(user);
        return "login";  // Dopo la registrazione, reindirizza alla pagina di login
    }
}
