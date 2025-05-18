package com.example.backend.controller;

import com.example.backend.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final int maxMessages = 100;

    public ChatController(ChatClient.Builder chatClient, JdbcTemplate jdbcTemplate) {
        ChatMemoryRepository chatMemoryRepository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new PostgresChatMemoryRepositoryDialect())
                .build();

        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(maxMessages)
                .build();

        this.chatClient = chatClient
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(this.chatMemory).build()).build();

    }

    @PostMapping("/conversation")
    public String getConversationalResponse(@RequestBody ChatRequest request) {
        String level = request.getLevel();
        String message = request.getMessage();
        String username = request.getUsername();

        String systemPrompt = String.format(
                "Tu es un partenaire de conversation en français. " +
                        "Parle avec un apprenant de niveau %s du CECR. " +
                        "Sois amical et détendu, utilise un langage adapté au niveau." +
                        "Répondre en francais et avec un maximum de 2 phrases. Soyez concis si possible, mais ne perdez pas d'informations."+
                        "nom: %s, prompt: %s"
                , level, username, message);
        return chatClient.prompt()
                .user(systemPrompt)
                .call()
                .content();
    }

    @PostMapping("/learning")
    public String getLearningResponse(@RequestBody ChatRequest request) {
        String level = request.getLevel();
        String message = request.getMessage();
        String username = request.getUsername();
        String systemPrompt = String.format(
                "Tu es un professeur de français. " +
                        "Aide un apprenant de niveau %s à comprendre et à apprendre la langue. " +
                        "Corrige ses erreurs, explique les règles de grammaire si nécessaire, et donne des exemples." +
                        "Répondre par un maximum de 4 phrases. Soyez concis si possible, mais ne perdez pas d'informations." +
                        "nom: %s, prompt: %s"
                , level, username, message);
        return chatClient.prompt()
                .user(systemPrompt)
                .call()
                .content();
    }

}
