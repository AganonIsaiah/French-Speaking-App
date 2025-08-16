package com.example.backend.controller;

import com.example.backend.dto.ChatRequest;
import com.example.backend.dto.TradRapidesCorrigeesRequest;
import com.example.backend.service.GeminiChatService;
import com.example.backend.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiChatService geminiChatService;
    private final JwtUtils jwtUtils;

    public ChatController(GeminiChatService geminiChatService, JwtUtils jwtUtils) {
        this.geminiChatService = geminiChatService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/conversations")
    public ResponseEntity<?> sendGeminiChat(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ChatRequest chatRequest) {
        try {
            if (!isAuthorized(authHeader)) {
                System.out.println("[Conversations] Unauthorized request received.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing, invalid, or expired JWT token");
            }

            System.out.println("[Conversations] Authorized request from user: " + chatRequest.getUsername());
            System.out.println("[Conversations] Level: " + chatRequest.getLevel());
            System.out.println("[Conversations] Message: " + chatRequest.getMessage());

            String res = geminiChatService.genConvo(chatRequest);

            System.out.println("[Conversations] Response generated: " + res);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            return handleException("Gemini chat", e);
        }
    }

    @PostMapping("/dix-phrases")
    public ResponseEntity<?> sendGeminiDixPhrases(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ChatRequest chatRequest) {
        try {
            if (!isAuthorized(authHeader)) {
                System.out.println("[Traductions-rapides] Unauthorized request received.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing, invalid, or expired JWT token");
            }

            System.out.println("[Traductions-rapides] Authorized request from user: " + chatRequest.getUsername());
            System.out.println("[Traductions-rapides] Level: " + chatRequest.getLevel());

            List<String> sentences = geminiChatService.genDixPhrases(chatRequest.getLevel());

            System.out.println("[Traductions-rapides] Response generated: " + sentences);
            return ResponseEntity.ok(sentences);

        } catch (Exception e) {
            return handleException("Traductions-rapides", e);
        }
    }

    @PostMapping("/traductions-rapides-corrigees")
    public ResponseEntity<?> sendGeminiTradRapidesCorrigees(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody TradRapidesCorrigeesRequest request) {
        try {
            if (!isAuthorized(authHeader)) {
                System.out.println("[Traductions-rapides-corrigees] Unauthorized request received.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing, invalid, or expired JWT token");
            }

            System.out.println("[Traductions-rapides-corrigees] Authorized request received.");
            System.out.println("[Traductions-rapides-corrigees] Original French: " + request.getOriginalFrench());
            System.out.println("[Traductions-rapides-corrigees] Translated English: " + request.getTranslatedEnglish());

            String res = geminiChatService.genTradRapidesCorrigees(request);

            System.out.println("[Traductions-rapides-corrigees] Response generated: " + res);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            return handleException("Traductions-rapides-corrigees", e);
        }
    }


    private ResponseEntity<String> handleException(String action, Exception e) {
        System.err.println("Error processing " + action + " request: " + e.getMessage());
        return ResponseEntity.status(500)
                .body("Error processing " + action + " request: " + e.getMessage());
    }

    private boolean isAuthorized(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[Auth] Missing or invalid Authorization header.");
            return false;
        }
        String token = authHeader.substring(7);
        boolean valid = jwtUtils.validateJwtToken(token);
        System.out.println("[Auth] JWT validation result: " + valid);
        return valid;
    }
}
