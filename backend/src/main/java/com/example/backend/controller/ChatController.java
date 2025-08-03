package com.example.backend.controller;


import com.example.backend.dto.ChatRequest;

import com.example.backend.service.GeminiChatService;
import com.example.backend.service.OllamaChatService;
import com.google.genai.Chat;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @PostMapping("/assist")
    public String generateChat(@RequestBody ChatRequest chatRequest) {

        System.out.println("MessageTESTING: "+chatRequest.getMessage());
        return chatService.generateResponse(chatRequest.getMessage());
    }


}