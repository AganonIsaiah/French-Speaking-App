package com.example.backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class AIChatController {

    private final ChatClient chatclient;

    public AIChatController(ChatClient.Builder chatclient) {
        this.chatclient = chatclient.build();
    }

    @GetMapping("")
    public String chat() {
        return chatclient.prompt().user("parlez francaise avec moi").call().content();
    }


}
