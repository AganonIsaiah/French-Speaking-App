package com.example.backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaChatService {

   private final ChatClient chatClient;

    public OllamaChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String generateResponse(String prompt) {

        String systemPrompt = "Respond with a succinctly in french, respond with a suggestion response to this prompt: " + prompt;



        String response = chatClient.prompt()
                .system(systemPrompt)
                .call()
                .content();


        return response;
    }


}
