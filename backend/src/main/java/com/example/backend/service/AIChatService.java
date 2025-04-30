package com.example.backend.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**

 curl -X POST http://localhost:8080/api/chat/generate \
      -H "Content-Type: application/json" \
      -d "\"parlez francaise avec moi\""

 */

@Service
public class AIChatService {

    private final ChatClient chatClient;

    public AIChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String generateResponse(String userPrompt) {
        String formattedPrompt = """
            Répondez en français avec **au maximum deux phrases**. Voici le message de l'utilisateur :
            %s
        """.formatted(userPrompt);

        return chatClient.prompt()
                .user(formattedPrompt)
                .call()
                .content();
    }

}
