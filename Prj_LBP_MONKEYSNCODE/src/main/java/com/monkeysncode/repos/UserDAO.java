package com.monkeysncode.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monkeysncode.entites.User;

public interface UserDAO extends JpaRepository<User, String>{
	public List<User> findAll();

}