package com.example.backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaChatService {

   private final ChatClient chatClient;

    // Store previous conversation messages
    private final List<String> memory = new ArrayList<>();

    private final int maxMemory = 100;

    public OllamaChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String generateResponse(String username, String userPrompt, String userLevel) {
        // Add user's message with their username
        memory.add(username + " (niveau " + userLevel + "): " + userPrompt);

        StringBuilder conversation = new StringBuilder("Voici la conversation jusqu'à présent :");
        for (String msg : memory) {
            conversation.append("\n").append(msg);
        }


        String systemPrompt = """
Tu es un assistant francophone chaleureux et naturel, qui parle avec %s et adapte son français au niveau %s de l'utilisateur.  
Réponds toujours en français, de façon claire, concise et agréable, en une phrase ou une phrase et demie maximum.  
Si tu ne sais pas, dis simplement : "Je ne sais pas."  
Sois créatif et naturel, comme une vraie personne, mais reste bref et ne dépasse jamais 1,5 phrase.  
""".formatted(username, userLevel);


//        System.out.println("Conversation: " + conversation);

        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(conversation.toString())
                .call()
                .content();

        memory.add("Assistant: " + response);
        trimMemory();

        return response;
    }


    private void trimMemory() {
        int maxEntries = maxMemory;
        if (memory.size() > maxEntries) {
            memory.subList(0, memory.size() - maxEntries).clear();
        }
    }
}
