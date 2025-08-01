package com.example.backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class GeminiChatService {

    private final Client chatClient;

    public GeminiChatService(Client chatClient) {
        this.chatClient = chatClient;
    }

    public String genRes(String prompt) {

        GenerateContentResponse res =
                chatClient.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null
                );

        return res.text();

    }
}
