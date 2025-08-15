package com.example.backend.controller;

import com.example.backend.dto.ChatRequest;
import com.example.backend.service.GeminiChatService;
import com.example.backend.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiChatService geminiChatService;
    private final JwtUtils jwtUtils;

    public ChatController(GeminiChatService geminiChatService, JwtUtils jwtUtils) {
        this.geminiChatService = geminiChatService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/gemini")
    public ResponseEntity<?> sendGeminiChat(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ChatRequest chatRequest) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);

            if (!jwtUtils.validateJwtToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired JWT token");
            }

            String res = geminiChatService.genRes(chatRequest);
            System.out.println("Username: " + chatRequest.getUsername());
            System.out.println("Level: " + chatRequest.getLevel());
            System.out.println("Message: " + chatRequest.getMessage());
            System.out.println("Response: " + res);

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            System.err.println("Error processing Gemini chat request: " + e.getMessage());
            return ResponseEntity.status(500)
                    .body("Error processing chat request: " + e.getMessage());
        }
    }
}
