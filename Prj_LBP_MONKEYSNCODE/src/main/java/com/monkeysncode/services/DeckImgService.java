package com.monkeysncode.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monkeysncode.entites.DeckImg;
import com.monkeysncode.repos.DeckImgDAO;

// Service class for managing deck images
@Service
public class DeckImgService {
    
    // Injecting the DeckImgDAO for database operations related to deck images
    @Autowired
    DeckImgDAO imgDAO;
    
    // Method to retrieve all deck images from the database
    public List<DeckImg> getAll() {
        return imgDAO.findAll(); // Calls the DAO method to fetch all DeckImg records
    }
    
    // Method to retrieve a specific deck image by its ID
    public Optional<DeckImg> getDeckImgById(Long id) {
        return imgDAO.findById(id); // Calls the DAO method to find a DeckImg by its ID
    }
}
