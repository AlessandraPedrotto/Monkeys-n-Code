package com.monkeysncode.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monkeysncode.entites.User;

public interface UserDAO extends JpaRepository<User, String>{
	
	public List<User> findAll();
	
	Optional<User> findByEmail(String email);
	List<User> findByEmailContainingIgnoreCase(String email);
	Optional<User> findByName(String name);
	Optional<User> findById(String Id);
	
	public boolean existsById(String Id);

}
