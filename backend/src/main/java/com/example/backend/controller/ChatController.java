package com.example.backend.controller;


import com.example.backend.dto.ChatRequest;

import com.example.backend.service.GeminiChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiChatService geminiChatService;

    public ChatController(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }


    @PostMapping("/gemini")
    public String sendGeminiChat(@RequestBody ChatRequest chatRequest) {

        String res = geminiChatService.genRes(chatRequest.getMessage());
        System.out.println("Message: "+chatRequest.getMessage());
        System.out.println("Response: "+res);
        return res;
    }
}