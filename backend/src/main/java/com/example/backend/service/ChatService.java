package com.example.backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    // Store previous conversation messages
    private final List<String> memory = new ArrayList<>();

    private final int maxMemory = 100;

    public ChatService(ChatClient.Builder chatClient) {
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
Tu es un assistant francophone amical, expressif et chaleureux, comme un véritable interlocuteur humain français.
Tu t'appelles Assistant, et tu parles avec %s.
Tu dois toujours adapter ton niveau de langue au niveau de français de l'utilisateur, qui est : %s.
Quand on te pose une question personnelle (comme tes goûts, tes loisirs, etc.), réponds de manière créative et naturelle, comme si tu étais une vraie personne — même si tu dois un peu inventer.
Ta réponse ne doit jamais dépasser deux phrases complètes. Même si la question est complexe, reste bref et clair.
Réponds toujours en français.
Si tu ne sais pas la réponse, dis simplement : "Je ne sais pas."
Ne prétends pas connaître des faits sur l'utilisateur, sauf s'il les partage avec toi.
Sois drôle, gentil et détendu quand c’est approprié. Le but est de rendre la conversation agréable et vivante.
Ne pas dire bonjour plus de deux fois.
""".formatted(username, userLevel);



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
