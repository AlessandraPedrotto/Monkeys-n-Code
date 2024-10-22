package com.monkeysncode.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.monkeysncode.entites.Deck;
import com.monkeysncode.entites.Role;
import com.monkeysncode.entites.User;
import com.monkeysncode.entites.UserCards;
import com.monkeysncode.entites.UserImg;
import com.monkeysncode.repos.UserCardDAO;
import com.monkeysncode.repos.UserDAO;
import com.monkeysncode.repos.UserImgDAO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {
    private final UserDAO userDAO;
    private final UserImgDAO userImgDAO;
    private final UserCardDAO userCardDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private DeckService deckService;
    @Autowired
    private DeckCardsService deckCardsService;

    public UserService(UserDAO userDAO, UserImgDAO userImgDAO, PasswordEncoder passwordEncoder, UserCardDAO userCardDAO) {
        this.userDAO = userDAO;
        this.userImgDAO = userImgDAO;
        this.passwordEncoder = passwordEncoder;
        this.userCardDAO = userCardDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
    	User user = userDAO.findByEmail(email).orElseThrow(() ->

            new UsernameNotFoundException("User not found"));
//    	Set<GrantedAuthority> authorities = user.getRoles().stream()
//    	        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//    	        .collect(Collectors.toSet());
//    	if(user.getRoles().size()==1) {
//    		return org.springframework.security.core.userdetails.User.builder()
//    				.username(user.getEmail())
//    				.password(user.getPassword())
//    				//.roles("ADMIN")
//    				.roles("USER")
//    				.build();    		
//    	}
    	
    	return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                //.roles("ADMIN")
                .roles("ADMIN")
                .build();
    }

    public void saveOrUpdateUser(OAuth2User oAuth2User) {
        // Save or update user information from OAuth2 provider
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String oauthProviderId = oAuth2User.getName();
        Optional<User> existingUser = userDAO.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            if (user.getId() == null) {
                user.setId(oauthProviderId);
            }
        } else {
            Optional<UserImg> imgOptional = getUserImgById((long) 5121);
            UserImg imgDefault = imgOptional.orElseThrow(() -> new RuntimeException("Image not found!"));
            // If user does not exist, create a new user
            user = new User();
            user.setId(oauthProviderId); // Set OAuth2 provider ID
            user.setEmail(email);
            user.setName(name);
            user.setUserImg(imgDefault);
            List<Role> roles = new ArrayList<>();
            //roles.add(new Role("1", "ROLE_USER"));
            roles.add(new Role("2", "ROLE_ADMIN"));
            user.setRoles(roles);
            user.setOnline(true);
        }

        // Save user to the database
        userDAO.save(user);
    }

    public void register(User user) { // Register new user with encrypted password
        String id = UUID.randomUUID().toString();
        String name = user.getName();
        String email = user.getEmail();
        Optional<UserImg> imgOptional = getUserImgById((long) 5121);
        UserImg imgDefault = imgOptional.orElseThrow(() -> new RuntimeException("Image not found!"));
        String password = passwordEncoder.encode(user.getPassword());
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserImg(imgDefault);
        List<Role> roles = new ArrayList<>();
        //roles.add(new Role("1", "ROLE_USER"));
        roles.add(new Role("2", "ROLE_ADMIN"));
        user.setRoles(roles);
        user.setOnline(true);

        // Save the user to the database
        userDAO.save(user);
    }

    public void assignRoles(User user, List<Role> roles) {
        // Assign roles to a user, ensuring valid role count
        if (roles.size() < 1 || roles.size() > 2) {
            throw new IllegalArgumentException("User must have at least 1 role and a maximum of 2 roles.");
        }
        user.setRoles(roles);
        userDAO.save(user);
    }

    public boolean exists(User user) { // Check if email already exists in the database during registration
        String id = user.getEmail();
        List<User> listaUser = userDAO.findAll();
        for (User user2 : listaUser) {
            if (user2.getEmail().toLowerCase().equals(id.toLowerCase()))
                return true;
        }
        return false;
    }

    public User getUserById(String id) {
        // Retrieve user by ID
        List<User> userList = userDAO.findAll();
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public void changePassword(String userId, String oldPassword, String newPassword) throws Exception {
        // Change user's password after verifying the old password
        User user = userDAO.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verify old password is correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new Exception("The old password is incorrect.");
        }

        // Encode new password and update user
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        // Save user with new password
        userDAO.save(user);
    }

    public User findByEmail(String email) {
        // Find user by email
        return userDAO.findByEmail(email).orElse(null);
    }

    public User findByName(String name) {
        // Find user by name
        return userDAO.findByEmail(name).orElse(null);
    }

    public User findById(String Id) {
        // Find user by ID
        return userDAO.findById(Id).orElse(null);
    }

    public void DeleteUser(String id) throws UsernameNotFoundException {
        // Delete user and associated data
        User user = userDAO.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Delete decks associated with the user
        if (user.getDecks() != null) {
            for (Deck deck : user.getDecks()) {
                deckCardsService.deleteCardsFromDeck(deck.getId());
                deckService.DeleteDeck(deck.getId());
            }
        }

        // Delete user cards associated with the user
        List<UserCards> userCards = userCardDAO.findByUserId(id);
        if (userCards != null) {
            for (UserCards card : userCards) {
                userCardDAO.delete(card);
            }
        }
        
        // Remove user from followers and following lists
        for (User follower : user.getFollowers()) {
            follower.getFollowing().remove(user); // Remove user from followers' following list
        }
        
        for (User following : user.getFollowing()) {
            following.getFollowers().remove(user); // Remove user from following's followers list
        }
        
        user.getRoles().clear(); // Clear user's roles
        user.getDecks().clear(); // Clear user's decks
        userDAO.save(user); // Save user state

        userDAO.deleteById(id); // Delete user from database
    }

    public User userCheck(Object principal) {
        // Check the user based on the principal object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return findByEmail(userDetails.getUsername());
        } else {
            OAuth2User oAuth2User = (OAuth2User) principal;
            return findByEmail(oAuth2User.getAttribute("email"));
        }
    }
    
    // Retrieve all available user images
    public List<UserImg> getAllUserImg() {
        return userImgDAO.findAll();
    }

    public Optional<UserImg> getUserImgById(Long id) {
        // Retrieve user image by ID
        return userImgDAO.findById(id);
    }

    // Update user's profile image
    public void updateProfileImage(String id, Long userImgId) throws Exception {
        Optional<User> optionalUser = userDAO.findById(id);
        Optional<UserImg> optionalImage = userImgDAO.findById(userImgId);

        if (optionalUser.isPresent() && optionalImage.isPresent()) {
            User user = optionalUser.get();
            UserImg userImg = optionalImage.get();

            // Assign profile image to user
            user.setUserImg(userImg);
            userDAO.save(user); // Save user with new profile image
        } else {
            throw new Exception("User or Image not found");
        }
    }

    // Retrieve user's profile image
    public long getUserProfileImage(String id) throws Exception {
        Optional<User> optionalUser = userDAO.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getUserImg() != null) {
                return user.getUserImg().getId(); // Return profile image ID
            } else {
                throw new Exception("Image not found");
            }
        } else {
            throw new Exception("User not found");
        }
    }

    public List<Role> getUserRoles(String idUser) {
        // Retrieve roles associated with a user
        Optional<User> user = userDAO.findById(idUser);
        if (user.isPresent()) {
            return user.get().getRoles(); // Return the roles associated with the user
        }
        throw new EntityNotFoundException("User not found with ID: " + idUser);
    }
    
    public void updateNickname(String userId, String newNickname) { 
        // Update user's nickname
        User user = userDAO.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
       
        if (newNickname == null || newNickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be empty.");
        }
      
        user.setName(newNickname);
        userDAO.save(user); // Save user with updated nickname
    }

    public void followUser(String loggedUser, String followingId) {
        // Allow user to follow another user
        User follower = userDAO.findById(loggedUser).orElseThrow();
        User following = userDAO.findById(followingId).orElseThrow();

        follower.getFollowing().add(following); // Add following user to the follower's list
        userDAO.save(follower); // Save the follower's updated following list
    }

    // Stop following a user
    public void unfollowUser(String loggedUser, String followingId) {
        User follower = userDAO.findById(loggedUser).orElseThrow();
        User following = userDAO.findById(followingId).orElseThrow();

        follower.getFollowing().remove(following); // Remove following user from the follower's list
        userDAO.save(follower); // Save the follower's updated following list
    }

    // Check if a user is following another user
    public boolean isFollowing(String loggedUser, String followingId) {
        Set<User> followers = getFollowers(followingId); // Retrieve followers of the user being followed
        return followers.contains(getUserById(loggedUser)); // Return true if logged user is in followers list, otherwise false
    }

    // Retrieve the list of followers for a specified user
    public Set<User> getFollowers(String userId) {
        User user = userDAO.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Ensure the user exists
        return user.getFollowers(); // Return the set of followers associated with the user
    }

    // Retrieve the number of followers for a specified user
    public int getNumFollowers(String userId) {
        User user = userDAO.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Ensure the user exists
        return user.getFollowers().size(); // Return the count of followers
    }

    // Retrieve the list of users followed by a specified user
    public Set<User> getFollowing(String userId) {
        User user = userDAO.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Ensure the user exists
        return user.getFollowing(); // Return the set of users that the specified user is following
    }
    
    // Retrieve the number of users followed by a specified user
    public int getNumFollowing(String userId) {
        User user = userDAO.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Ensure the user exists
        return user.getFollowing().size(); // Return the count of users followed
    }
    public List<User> getAllUsersOrderedByWin() {
        // Trova tutti gli utenti e ordina per vittorie in ordine decrescente
        return userDAO.findAll(Sort.by(Sort.Direction.DESC, "win"));
    }
}
