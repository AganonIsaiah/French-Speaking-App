package com.example.backend.service;

import com.example.backend.model.Account;
import com.example.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AccountRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public Account registerUser(Account user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Optional<Account> loginUser(String username, String password) {
        Optional <Account> user = userRepo.findByUsername(username);
       
        if (user != null && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}