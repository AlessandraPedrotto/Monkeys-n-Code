package com.monkeysncode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { // Controller who manages the home
	
	@GetMapping("/")
	public String Home() {
		return "home1";
	}
	
	@GetMapping("/secured")
	public String Secured() {
		return "Secured";
	}
	
}
