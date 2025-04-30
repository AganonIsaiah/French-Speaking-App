package com.example.backend.controller;

import com.example.backend.service.AIChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class AIChatController {

    private final AIChatService chatService;

    public AIChatController(AIChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/generate/simple")
    public String generateSimpleResponse(@RequestBody String prompt) {
        return chatService.generateSimpleResponse(prompt);
    }
}
