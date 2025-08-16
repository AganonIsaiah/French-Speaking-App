package com.example.backend.controller;

import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.SignupRequest;
import com.example.backend.dto.SignupResponse;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserPrincipal;
import com.example.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody SignupRequest signUpRequest) {
        System.out.println("Signup request received for username: " + signUpRequest.getUsername());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new SignupResponse("Error: Username is already taken!", null));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new SignupResponse("Error: Email is already in use!", null));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getRegion(),
                signUpRequest.getLevel()
        );

        userRepository.save(user);

        System.out.println("User registered successfully: " + user.getUsername());

        return ResponseEntity.ok(new SignupResponse("User registered successfully!", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt for username: " + loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            System.out.println("User logged in successfully: " + userPrincipal.getUsername());

            AuthResponse.UserDTO userDTO = new AuthResponse.UserDTO(
                    userPrincipal.getUsername(),
                    userPrincipal.getEmail(),
                    userPrincipal.getRegion(),
                    userPrincipal.getLevel()
            );

            return ResponseEntity.ok(new AuthResponse(jwt, "Successful login", userDTO));
        } catch (Exception e) {
            System.out.println("Authentication failed for user: " + loginRequest.getUsername() + ". Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error: Invalid username or password!"));
        }
    }

}