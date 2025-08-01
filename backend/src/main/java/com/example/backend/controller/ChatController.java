package com.example.backend.controller;


import com.example.backend.dto.ChatRequest;

import com.example.backend.service.GeminiChatService;
import com.example.backend.service.OllamaChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OllamaChatService chatService;
    private final GeminiChatService geminiChatService;

    public ChatController(OllamaChatService chatService, GeminiChatService geminiChatService) {
        this.chatService = chatService;
        this.geminiChatService = geminiChatService;
    }

    @PostMapping("/gemini")
    public String sendGeminiChat(@RequestBody ChatRequest chatRequest) {

        String res = geminiChatService.genRes(chatRequest.getMessage());
        System.out.println("Message: "+chatRequest.getMessage());
        System.out.println("Response: "+res);
        return res;
    }

    @PostMapping("/ollama")
    public String generateChat(@RequestBody ChatRequest chatRequest) {
        System.out.println("--------------\nUsername: " + chatRequest.getUsername());
        System.out.println("Level: "+chatRequest.getLevel());
        System.out.println("Message: "+chatRequest.getMessage());

        return chatService.generateResponse(chatRequest.getUsername(), chatRequest.getMessage(), chatRequest.getLevel());
    }


}