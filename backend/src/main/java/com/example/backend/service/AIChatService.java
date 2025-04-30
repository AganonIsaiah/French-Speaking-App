package com.example.backend.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIChatService {

    private final ChatClient chatClient;

    public AIChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String generateResponse(String prompt) {
        return chatClient.prompt().user(prompt).call().content();
    }

}
