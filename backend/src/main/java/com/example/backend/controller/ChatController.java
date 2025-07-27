package com.example.backend.controller;


import com.example.backend.dto.ChatRequest;
import com.example.backend.service.ChatService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/generate")
    public String generateChat(@RequestBody ChatRequest chatRequest) {
        System.out.println("--------------\nUsername: " + chatRequest.getUsername());
        System.out.println("Level: "+chatRequest.getLevel());
        System.out.println("Message: "+chatRequest.getMessage());

        return chatService.generateResponse(chatRequest.getUsername(), chatRequest.getMessage(), chatRequest.getLevel());
    }


}