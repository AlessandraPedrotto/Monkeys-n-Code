package com.monkeysncode.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.monkeysncode.entites.User;
import com.monkeysncode.repos.UserDAO;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/formStat")
    public String getAssignStatsForm(@RequestParam(required = false) String query, Model model) {
    	List<User> users = List.of();
    	if (query != null && !query.isEmpty()) {
            // Perform search by partial email match
        	users = userDAO.findByEmailContainingIgnoreCase(query);
            model.addAttribute("users", users);
        }
        model.addAttribute("query", query);
        
        return "formStat";  // Render the form-stat.html page
    }

    // Handle the form submission and update the user's statistics
    @PostMapping("/formStat")
    public String assignStatistics(@RequestParam String userId,
                                   @RequestParam int win,
                                   @RequestParam int lose,
                                   Model model) {
        try {
            // Find the user by ID
            Optional<User> userOpt = userDAO.findById(userId);
            if (!userOpt.isPresent()) {
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }

            User user = userOpt.get();
            // Calculate the points for wins and losses
            int winPoints = win * 50;   
            int losePoints = lose * -10; 
            // Add new wins to existing values
            user.setWin(user.getWin() + win);
            user.setLose(user.getLose() + lose);  // Assuming `user.getLose()` exists
            int totalPoints = (user.getWin() * 50) + (user.getLose() * -10);
            userDAO.save(user);

            // Add success message to the model
            model.addAttribute("success-stat", "fatto");
            model.addAttribute("user", user);
            model.addAttribute("totalPoints", totalPoints); 

        } catch (Exception e) {
            // Handle the exception and send the error message to the model
            model.addAttribute("error-stat", e.getMessage());
        }

        // Return to the form-stat.html page with success or error message
        return "formStat";
    }
}
