package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            return new LoginResponse(false, "Username invalid!", null);
        }

        User userAuth = user.get();
        if (!passwordEncoder.matches(request.getPassword(), userAuth.getPassword())) {
            return new LoginResponse(false, "Password invalid!", null);
        }

        UserResponse safeUser = UserResponse.builder()
                .id(userAuth.getId())
                .username(userAuth.getUsername())
                .email(userAuth.getEmail())
                .points(userAuth.getPoints())
                .proficiency(userAuth.getProficiency())
                .region(userAuth.getRegion())
                .build();

        return new LoginResponse(true, "Login successful!", safeUser);
    }
}
