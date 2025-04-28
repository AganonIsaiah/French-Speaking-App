package com.example.backend.controller;

import com.example.backend.model.Account;
import com.example.backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Account user) {
        try {
            Account u = authService.registerUser(user);
            return ResponseEntity.ok(u);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account user) {
        Optional<Account> u = authService.loginUser(user.getUsername(), user.getPassword());
        if (u.isPresent()) return ResponseEntity.ok(u.get());
        
        return ResponseEntity.status(401).body("Invalid username or password.");
    }
}
